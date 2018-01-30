package main;

import java.text.NumberFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import data.CoveredAreas;
import data.ISP;
import data.ISPDAO;

public class Cache implements AffichableSurIRC {
	/**
	 * Cette classe sert de cache pour les infos de DB
	 * Elle implemente le Design Pattern Singleton dans la mesure où cette classe doit être l'unique instance de référence peu importe le Thread l'utilisant, et que le cache est unique.
	 */

	public static volatile Cache instance = null;
	private Date lastCacheUpdate;
	private List<ISP> cache;
	public static final long TIME_BETWEEN_RELOADS = 360000;

	private Cache() {
		ISPDAO idao = ISPDAO.getInstance();
		try {
			cache = idao.getISPs();
			lastCacheUpdate = new Date();
		} catch (Exception e) {
			System.err.println("Le cache n'as pas pu être géré à cause de l'exception suivante");
			e.printStackTrace();
		}
	}


	public  Date getLastCacheUpdate() {
		return lastCacheUpdate;
	}


	/**
	 * Méthode du design patern singleton. Permet de récuperer et si besoin d'initialiser, l'unique instance de la classe.
	 * @return Instance du Cache
	 */
	public final static Cache getInstance() {
		if (Cache.instance == null) {
			synchronized (ISPDAO.class) {
				if(Cache.instance == null) {
					Cache.instance = new Cache();


				}
			}
		}
		return Cache.instance;
	}

	/**
	 * Cette methode vient mettre à jour le cache des FAI. Pour cela, il récupere l'instance du DAO et récupère la liste des ISP. Si tout cse passe correctement, il supprime le cache précédent et le remplace par les nouvelles valeurs. Sinon, il maaintient le cache précédent.
	 * @return True si l'operation s'est passée correctement, False sinon et affiche la cause
	 */
	public boolean reload() {
		try {
			ISPDAO idao = ISPDAO.getInstance();
			List<ISP> l = idao.getISPs();
			if(l != null) {
				cache.clear();
				cache.addAll(l);
			}
		}catch (Exception e) {
			System.err.println("Erreur au moment du reload() :\n "+e.getMessage());
			return false;
		}
		lastCacheUpdate = new Date();
		return true;
	}

	public int size() {
		return cache.size();
	}

	public List<ISP> getListe(){
		return cache;
	}

	public int getMemberCountInFede() {
		int i = 0;
		for(ISP isp : getListe()) {
			if(isp.isFFDNMember()) {
				i += isp.getMembersCount();
			}
		}
		return i;
	}

	public int getSubscribersCountInFede() {
		int i = 0;
		for(ISP isp : getListe()) {
			if(isp.isFFDNMember()) {
				i += isp.getSubscribersCount();
			}
		}
		return i;
	}

	public int getMemberCountOutFede() {
		int i = 0;
		for(ISP isp : getListe()) {
			if(!isp.isFFDNMember()) {
				i += isp.getMembersCount();
			}
		}
		return i;
	}

	public int getSubscribersCountOutFede() {
		int i = 0;
		for(ISP isp : getListe()) {
			if(!isp.isFFDNMember()) {
				i += isp.getSubscribersCount();
			}
		}
		return i;
	}

	public String getSubscribersPercents(int val) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);
		Double val2 = val+0.0;
		Double nbSubscribers = 0.0+getSubscribersCountInFede();
		return nf.format((val2/nbSubscribers)*100)+" %";
	}

	public String getMembersPercents(int val) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);
		Double val2 = val+0.0;
		Double nbMembers = 0.0+getMemberCountInFede();
		return nf.format((val2/nbMembers)*100)+" %";
	}



	/**
	 * Donne le nombre de FAI de la fédé en parcourant le cache.
	 * @param ffdn_member Si = null alors on s'en fout, si =true, seuls les FAI de la fédé, si = false, seuls les FAI non membres
	 * @return nombre de FAI
	 */
	public int getISPCount(Boolean ffdn_member) {
		if(ffdn_member == null) {
			return cache.size();
		}else{
			int count = 0;
			for(ISP fai : cache) {
				if(ffdn_member == fai.isFFDNMember()) {
					count++;
				}
			}
			return count;

		}

	}

	public ISP getISPWithName(String s) {
		for(ISP i : cache) {
			if(i.getShortestName().toLowerCase().contains(s.toLowerCase()) || i.getName().toLowerCase().contains(s.toLowerCase())) {
				return i;
			}
		}
		return null;
	}

	public String toString() {
		return "Cache de "+cache.size()+" FAI";
	}

	public List<String> toStringIRC() {
		List<String> liste = new LinkedList<String>();
		liste.add("Il y a "+cache.size()+" FAI dont "+getISPCount(Boolean.TRUE)+" dans la fédé");
		liste.add("Cela représente en tout (dans la fédé) "+getSubscribersCountInFede()+" Abonné.e.s et "+getMemberCountInFede()+" Membres");
		liste.add("Et hors fédé : "+getSubscribersCountOutFede()+" Abonné.e.s et "+getMemberCountOutFede()+" Membres");
		return liste;
	}


	public ISP getISPWithGeoZone(String s) {

		for(ISP i: cache) {
			if(Main.isDebug()) {
				System.out.println("Recherche sur "+i.getBetterName());
			}
			if(i.getCoveredAreas()!= null) for(CoveredAreas ca : i.getCoveredAreas()) {
				if(Main.isDebug()) {
					ca.getName();
				}
				if(s.equalsIgnoreCase(ca.getName())) {
					return i;
				}
			}

		}

		return null;

	}


}
