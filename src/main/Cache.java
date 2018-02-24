package main;

import java.text.NumberFormat;
import java.util.ArrayList;
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
	private static long TIME_BETWEEN_RELOADS = 360000;
	
	/**
	 * Constructeur de cache. Celui ci est privé car la classe utilise le Design Patern singleton.
	 */
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
	
	/**
	 * @return the tIME_BETWEEN_RELOADS
	 */
	public static long getTIME_BETWEEN_RELOADS() {
		return TIME_BETWEEN_RELOADS;
	}



	/**
	 * @param tIME_BETWEEN_RELOADS the tIME_BETWEEN_RELOADS to set
	 */
	public static void setTIME_BETWEEN_RELOADS(long tIME_BETWEEN_RELOADS) {
		TIME_BETWEEN_RELOADS = tIME_BETWEEN_RELOADS;
	}



	/**
	 * Dernière fois que le cache à été mis a jour.
	 * @return Date correspondante à la dernière fois que le cache à été mis a jour
	 */
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
	
	public List<ISP> getListeInFede(){
		List<ISP> listeall = getListe();
		List<ISP> res = new ArrayList<>(30);
		for(ISP isp : listeall) {
			if(isp.isFFDNMember()) {
				res.add(isp);
			}
		}
		return res;
	}

	/**
	 * Compte le nombre de membres <b>dans</b> la fédération. <br>Extrait à partir de la somme du champ membercount de DB sur les FAI de la fédé
	 * @return un entier positif correspondant au nombre total de personnes dans les FAI de la fédé
	 */
	public int getMemberCountInFede() {
		int i = 0;
		for(ISP isp : getListe()) {
			if(isp.isFFDNMember()) {
				i += isp.getMembersCount();
			}
		}
		return i;
	}

	/**
	 * Compte le nombre d'abonnés <b>dans</b> la fédération. Ce nombre peut representer des abonnés xDSL, VPN, ... <br> Extrait à partir du champ subsriberscount de DB sur les FAI de la fédé
	 * @return un entier positif correspondant au nombre total d'abonné.e.s dans la fédé
	 */
	public int getSubscribersCountInFede() {
		int i = 0;
		for(ISP isp : getListe()) {
			if(isp.isFFDNMember()) {
				i += isp.getSubscribersCount();
			}
		}
		return i;
	}
	
	/**
	 * Compte le nombre de membres <b>HORS</b> la fédération. <br>Extrait à partir de la somme du champ membercount de DB sur les FAI de la fédé
	 * @return un entier positif correspondant au nombre total de personnes hors des FAI de la fédé
	 */
	
	public int getMemberCountOutFede() {
		int i = 0;
		for(ISP isp : getListe()) {
			if(!isp.isFFDNMember()) {
				i += isp.getMembersCount();
			}
		}
		return i;
	}

	/**
	 * Compte le nombre d'abonnés <b>HORS</b> la fédération. Ce nombre peut representer des abonnés xDSL, VPN, ... <br> Extrait à partir du champ subsriberscount de DB sur les FAI en dehors de la fédé
	 * @return un entier positif correspondant au nombre total d'abonné.e.s hors de la fédé
	 */
	public int getSubscribersCountOutFede() {
		int i = 0;
		for(ISP isp : getListe()) {
			if(!isp.isFFDNMember()) {
				i += isp.getSubscribersCount();
			}
		}
		return i;
	}

	
	/**
	 * Cette méthode sert a sortir le pourcentage d'abonnées auquel val correspond dans la fédération. Cette methode sert lors de l'affichage d'un FAI notamment à l'aide de +info
	 * @param val Entier correspondant a un nombre d'abonnés
	 * @return Chaine de caractère sous la forme X.XX % avec entre 0 et 2 nombres après la virgule.
	 */
	public String getSubscribersPercents(int val) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);
		Double val2 = val+0.0;
		Double nbSubscribers = 0.0+getSubscribersCountInFede();
		return nf.format((val2/nbSubscribers)*100)+" %";
	}


	/**
	 * Cette méthode sert a sortir le pourcentage de membres auquel val correspond dans la fédération. Cette methode sert lors de l'affichage d'un FAI notamment à l'aide de +info
	 * @param val Entier correspondant a un nombre de membres.
	 * @return Chaine de caractère sous la forme "X.XX %" avec entre 0 et 2 nombres après la virgule.
	 */
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
	
	/**
	 * Récupere le FAI correspondant au nom indiqué. Dès qu'il en trouve un, il le renvoie sinon il renvoie NULL
	 * @param s Chaine de caractère à rechercher
	 * @return FAI correspondant a la chaine ou null si il n'y en a pas.
	 */

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
	
	/**
	 * Affichage des statistiques de la fédération. Méthode utilisée pour l'affichage de +info ffdn
	 */
	public List<String> toStringIRC() {
		List<String> liste = new LinkedList<String>();
		liste.add("Il y a "+cache.size()+" FAI dont "+getISPCount(Boolean.TRUE)+" dans la fédé");
		liste.add("Cela représente en tout (dans la fédé) "+getSubscribersCountInFede()+" Abonné.e.s et "+getMemberCountInFede()+" Membres");
		liste.add("Et hors fédé : "+getSubscribersCountOutFede()+" Abonné.e.s et "+getMemberCountOutFede()+" Membres");
		return liste;
	}

	/**
	 * Récupere la zone geo avec le nom passé en paramètres.
	 * @param s Nom de la zone.
	 * @return FAI ayant indiqué être présent sur la zone.
	 */
	//TODO Changer la méthode pour la zone geographique en soignant plus le nom, et verifier si il n'y a pas plusieurs FAIs
	public ISP getISPWithGeoZone(String s) {

		for(ISP i: cache) {
			if(Main.isDebug()) {
				System.out.println("Recherche sur "+i.getBetterName());
			}
			if(i.getCoveredAreas()!= null) for(CoveredAreas ca : i.getCoveredAreas()) {
				if(Main.isDebug()) {
					ca.getName();
				}
				String name = ca.getName().toLowerCase();
				s = s.toLowerCase();
				if(name.contains(s)) {
					return i;
				}
			}

		}

		return null;

	}
	
	/**
	 * 
	 * @param id numerro correspondanat au FAI dans <a href=db.ffdn.org>db.ffdn.org</a>
	 * @return ISP correpondant a l'identifiant ou null si ce dernier n'est pas dans le cache.
	 */
	public ISP getISPWithID(int id) {
		for(ISP i:cache) {
			if(i.getId() == id) {
				return i;
			}
		}
		return null;
	}


}
