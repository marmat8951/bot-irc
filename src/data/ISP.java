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
		res.add(name+" : ");
		res.add("Est membre de la fédération : "+booleanToOuiNon(isFFDNMember()));
		res.add("Nombre de Membres : "+getMembersCount()+" soit "+c.getMembersPercents(getMembersCount()));
		res.add("Nombre d'abonnements : "+getSubscribersCount()+" soit "+c.getSubscribersPercents(getSubscribersCount()));
		
		
		return res;
	}

}
