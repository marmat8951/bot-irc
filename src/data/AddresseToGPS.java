package data;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddresseToGPS {

	private String adresse;
	
	public static final String NOMINATUM = "https://nominatim.openstreetmap.org/search/";
	public static final String FORMAT = "json";
	public static final int LIMIT = 3;
	
	/**
	 * Lieu est un groupement d'un Nom a afficher et d'une coordonnée GPS.
	 */
	public class Lieu{
		
		public final String DisplayName;
		public final Coordinates coordonees;
		
		public Lieu(String displayName, Coordinates coordonees) {
			super();
			DisplayName = displayName;
			this.coordonees = coordonees;
		}
		public Lieu(JSONObject jo) {
			super();
			DisplayName = jo.getString("display_name");
			this.coordonees = new Coordinates(jo.getDouble("lat"), jo.getDouble("lon"));
		}
		
		public String toString() {
			return DisplayName+" : "+coordonees;
		}
	}
	
	public AddresseToGPS(String adresse) {
		this.adresse = adresse;

	}
	
	/**
	 * @return l'adresse
	 */
	public String getAdresse() {
		return adresse;
	}

	/**
	 * Récupere l'URL pour la requète a Nominatum pour {@link AddresseToGPS#adresse} 
	 * @return
	 */
	public String getAddressToQuerry() {
		String s= NOMINATUM+adresse+"?format="+FORMAT+"&limit="+LIMIT;
		s=s.replaceAll("\\s", "%20");
		return s;
	}

	/**
	 * Récupère les coordonnée du lieu correspondant à l'adresse
	 * @return Coordonnée du lieu
	 * @throws ErrorAddressException si plusieurs addresses sont possibles.
	 */
	public Coordinates getCoordinates() throws ErrorAddressException {
		Lieu l = getLieu();
		return l.coordonees;
	}
	
	/**
	 * Récupère les coordonnée du lieu correspondant à l'adresse parmis plusieurs
	 * @param choice identifiant du choix que l'ont fait parmis les multiples possibilitées de lieu
	 * @return
	 */
	public Coordinates getCoordinatesWithChoiceForced(int choice) {
		Lieu l = getLieuWithChoiceForced(choice);
		return l.coordonees;
	}
	
	/**
	 * Effectue la requète pour récuperer le lieu et le renvoie
	 * @return Lieu correspondant ou null si il n'y en a pas
	 * @throws ErrorAddressException si il y a plusieurs lieux possibles
	 */
	public Lieu getLieu() throws ErrorAddressException {
		String get = ISPDAO.getInstance().executeGet(getAddressToQuerry());
		JSONArray ja = new JSONArray(get);
		if(ja.length()<1) {
			return null;
		}else if(ja.length()>1) {
			int len = ja.length();
			Lieu[] l = new Lieu[len];
			for(int i=0;i<len;++i) {
				l[i]=new Lieu(ja.getJSONObject(i));
			}
			throw new ErrorAddressException(l, adresse);
		}else {
			Lieu l = new Lieu(ja.getJSONObject(0));
			return l;
		}
	}
	
	/**
	 * Même chose que {@link AddresseToGPS#getLieu()} mais avec le choix forcé sur l'identifiant 
	 * @param choice identifiant du choix
	 * @return Lieu choisi parmis la liste ou null si aucun n'existe
	 */
	public Lieu getLieuWithChoiceForced(int choice) {
		
		String get = ISPDAO.getInstance().executeGet(getAddressToQuerry());
		JSONArray ja = new JSONArray(get);
		JSONObject jo = ja.getJSONObject(choice);
		if(jo == null) {
			return null;
		}
		Lieu l = new Lieu(jo);
		return l;
		
	}
	
	/**
	 * Récupere un tableau de Lieu correspondant a l'adresse indiquée.
	 * @return
	 */
	public Lieu[] getAllLieu() {
		String get = ISPDAO.getInstance().executeGet(getAddressToQuerry());
		JSONArray ja = new JSONArray(get);
		if(ja.length()<1) {
			return null;
		}else {
			int len = ja.length();
			Lieu[] l = new Lieu[len];
			for(int i=0;i<len;++i) {
				l[i]=new Lieu(ja.getJSONObject(i));
			}
			return l;
		}
		
	}
	
}
