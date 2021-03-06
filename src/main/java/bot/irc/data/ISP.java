package bot.irc.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bot.irc.IRC.Server;
import bot.irc.main.AffichableSurIRC;
import bot.irc.main.Cache;

public class ISP implements AffichableSurIRC {

	private String name;
	private int id;
	private boolean isFFDNMember;
	private String dateAdded;
	private String lastUpdate;
	private ISPdata data;
	private CoveredAreas [] coveredAreas;

	/**
	 *  Constructeur principal de la classe.
	 * @param name Nom du FAI
	 * @param id Identifiant unique sur DB
	 * @param isFFDNMember est il membre de FFDN
	 * @param dateAdded Date d'ajout
	 * @param lastUpdate Date de mise a jour sur DB
	 * @param data Data correspondant au champ étendu sur db
	 * @param ca  Zones couvertes
	 */

	public ISP(String name, int id, boolean isFFDNMember, String dateAdded, String lastUpdate, ISPdata data, CoveredAreas [] ca) {
		super();
		this.name = name;
		this.id = id;
		this.isFFDNMember = isFFDNMember;
		this.dateAdded = dateAdded;
		this.lastUpdate = lastUpdate;
		this.data = data;
		this.coveredAreas = ca;
		for(int i=0; i<coveredAreas.length;++i) {
			coveredAreas[i].setIsp(this);
		}
	}

	/**
	 * Méthode pour récuperer le plus court des noms du FAI, c'est à dire, si il existe shortname, sinon name
	 * @return Le nom le plus court du FAI
	 */
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


	public String getDateAdded() {
		return dateAdded;
	}


	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}


	public String getLastUpdate() {
		return lastUpdate;
	}


	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
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

	/**
	 * Renvoie le textuel en Français pour un booleen
	 * @param b booleen
	 * @return "oui" si le boolean est True "non" sinon
	 */
	private String booleanToOuiNon(boolean b) {
		if(b) {
			return "oui";
		}
		return "non";

	}

	
	public List<String> toStringIRC () {
		Cache c = Cache.getInstance();
		List<String> res = new LinkedList<>();
		String preface = "["+getBetterName()+"] ";
		res.add(preface+"Est membre de la fédération : "+booleanToOuiNon(isFFDNMember()));
		if(isFFDNMember()) {
			res.add(preface+"Nombre de Membres : "+getMembersCount()+" soit "+c.getMembersPercents(getMembersCount())+" de ceux de la fédération");
			res.add(preface+"Nombre d'abonnements : "+getSubscribersCount()+" soit "+c.getSubscribersPercents(getSubscribersCount())+" de ceux de la fédération");
		}else {
			res.add(preface+"Nombre de Membres : "+getMembersCount());
			res.add(preface+"Nombre d'abonnements : "+getSubscribersCount());
		}


		return res;
	}

	private String getShortName() {
		return data.getShortname();
	}

	public CoveredAreas[] getCoveredAreas() {
		return coveredAreas;
	}

	/**
	 * Récupere la zone couverte correspondante au paramètre
	 * @param name Nom de la zone
	 * @return	la première Zone correspondante au nom.
	 */
	public CoveredAreas getCoveredArea(String name) {
		for(CoveredAreas ca : coveredAreas) {
			if(ca.getName().equalsIgnoreCase(name)) {
				return ca;
			}
		}
		return null;
	}

	/**
	 * Récupere la zone couverte correspondante au paramètre
	 * @param name Nom de la zone
	 * @return	toutes les Zones correspondante au nom.
	 */
	public List<CoveredAreas> getCoveredAreas(String name){
		List<CoveredAreas> lca = new ArrayList<>(4);
		for(CoveredAreas ca : coveredAreas) {
			if(ca.getName().equalsIgnoreCase(name)) {
				lca.add(ca);
			}
		}
		return lca;

	}
	
	/**
	 * Renvoie une liste de Chaines de caractères
	 * @return Liste de phrases utilisables pour afficher le contact de ce FAI
	 */
	public List<String> contact() {
		List<String> s = new ArrayList<>();
		
		s.add("["+this.getBetterName()+"] est joignable par: \n");
		String site = getData().getWebsite();
		if(site != null && !site.equals("")) s.add("Site web: "+site+"\n");
		String email = getData().emailSyntaxer();
		if(email != null && !email.equals("")) s.add(" Email: "+email);
		Server[] chans = getData().getIrcChan();
		if(chans != null) {
			s.add("Chat : ");
			for(int i=0; i<chans.length; i++) {
				s.add(chans[i].toString());
			}
		}
		if(s.size() == 1) { //Il n'y a aucun élément disponible pour le contact
			s.add("Aucun contact disponible");
		}
		
		return s;
	}

}
