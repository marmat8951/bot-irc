package data;

import org.json.JSONArray;
import org.json.JSONObject;

import main.Bot;

public class AddresseToGPS {

	private String adresse;
	
	public static final String NOMINATUM = "https://nominatim.openstreetmap.org/search/";
	public static final String FORMAT = "json";
	public static final int LIMIT = 3;
	private Bot bot;
	
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
	}
	
	public AddresseToGPS(String adresse, Bot b) {
		this.adresse = adresse;
		this.bot = b;
	}
	
	public String getAddressToQuerry() {
		return NOMINATUM+adresse+"?format="+FORMAT+"&limit="+LIMIT;
	}

	public Coordinates getCoordinates() throws MultiplePossibleAddressException {
		Lieu l = getLieu();
		return l.coordonees;
	}
	
	
	public Coordinates getCoordinatesWithChoiceForced(int choice) {
		Lieu l = getLieuWithChoiceForced(choice);
		return l.coordonees;
	}
	
	
	public Lieu getLieu() throws MultiplePossibleAddressException {
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
			throw new MultiplePossibleAddressException(l, adresse);
		}else {
			Lieu l = new Lieu(ja.getJSONObject(0));
			return l;
		}
	}
	
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
