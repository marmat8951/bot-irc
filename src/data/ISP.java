package data;

import java.util.LinkedList;
import java.util.List;

import main.AffichableSurIRC;
import main.Cache;

public class ISP implements AffichableSurIRC {
	
	private String name;
	private int id;
	private boolean isFFDNMember;
	private String date_added;
	private String last_update;
	private ISPdata data;
	
	
	
	public ISP(String name, int id, boolean isFFDNMember, String date_added, String last_update, ISPdata data) {
		super();
		this.name = name;
		this.id = id;
		this.isFFDNMember = isFFDNMember;
		this.date_added = date_added;
		this.last_update = last_update;
		this.data = data;
	}
	
	public String getShortestName() {
		if(data.hasShortName()) {
			return data.getShortname();
		}else {
			return name;
		}
	}
	
	/**
	 * Méthode pour récuperer le nom le plus correct pour afficher les informations.
	 * Si il n'y a pas de Shortname alors name sera utilisé.
	 * Si name possède des espaces alors le shortname sera utilisé.
	 * Si name est plus long que 15 caractères alors shortname sera utilisé.
	 * Sinon, on utilise Name
	 * @return Le meilleur nom utilisable pour l'affichage.
	 */
	public String getBetterName() {
		if(data.hasShortName()) {
			String shortname = getShortName();
			if(name.length()>15 || name.contains(" ")) {
				return shortname;
			}else {
				return name;
			}
		}else {
			return name;
		}
		
	}
	/**
	 * Inverse de getBetterName. Le but est dans le cadre de l'affichage de plusieurs noms
	 * @return Le moins bon nom pour l'affichage
	 */
	public String getWorstName() {
		if(data.hasShortName()) {
			String shortname = getShortName();
			if(name.length()>15 || name.contains(" ")) {
				return name;
			}else {
				return shortname;
			}
			
		}else {
			return name;
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}


	public void setData(ISPdata data) {
		this.data = data;
	}


	public String getName() {
		return name;
	}
	public ISPdata getData() {
		return data;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isFFDNMember() {
		return isFFDNMember;
	}
	public void setFFDNMember(boolean isFFDNMember) {
		this.isFFDNMember = isFFDNMember;
	}

	
	public String getDate_added() {
		return date_added;
	}


	public void setDate_added(String date_added) {
		this.date_added = date_added;
	}


	public String getLast_update() {
		return last_update;
	}


	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}


	public int getMembersCount() {
		return data.getMembersCount();
	}

	public int getSubscribersCount() {
		return data.getSubscribersCount();
	}
	
	public String toString() {
		String res="";
		res+=name+" : ";
		res+="Est membre: "+booleanToOuiNon(isFFDNMember())+" ";
		res+="Nombre de membres: "+getMembersCount()+" Nombre d'abonnements:"+getSubscribersCount(); 
		return res;
	}
	
	private String booleanToOuiNon(boolean b) {
		if(b) {
			return "oui";
		}
		return "non";
		
	}
	
	/**
	 * Renvoie une Liste de chaine de caractères pour permettre l'affichage sur IRC ligne par ligne, bien que le \n ne soit pas interprété.
	 * @return Une Liste de chaine correspondant à toutes les lignes que le bot doit écrire
	 */
	
	public List<String> toStringIRC () {
		Cache c = Cache.getInstance();
		List<String> res = new LinkedList<>();
		String preface = "["+getBetterName()+"] ";
		res.add(preface+"Est membre de la fédération : "+booleanToOuiNon(isFFDNMember()));
		res.add(preface+"Nombre de Membres : "+getMembersCount()+" soit "+c.getMembersPercents(getMembersCount()));
		res.add(preface+"Nombre d'abonnements : "+getSubscribersCount()+" soit "+c.getSubscribersPercents(getSubscribersCount()));
		
		
		return res;
	}
	
	private String getShortName() {
		return data.getShortname();
	}

}
