package bot.irc.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bot.irc.IRC.Server;
import bot.irc.main.Main;

public class ISPdata {

	private String shortname;
	private String website;
	private String description;
	private Server [] comChan;
	private int progressStatus;
	private int membersCount;
	private int subscribersCount;
	private String email;
	private String creationDate;
	private String joinDate;
	private Coordinates coordonnees;
	




	public ISPdata(String shortName, String website, String description, Server[] chatrooms, int progressStatus,
			int membersCount, int subscribersCount, String email, String creationDate, String joinDate) {
		super();
		this.shortname = shortName;
		this.website = website;
		this.description = description;
		this.comChan = chatrooms;
		this.progressStatus = progressStatus;
		this.membersCount = membersCount;
		this.subscribersCount = subscribersCount;
		this.email=email;
		this.creationDate = creationDate;
		this.joinDate = joinDate;
		
	}

	public ISPdata(JSONObject jo){
		this.shortname = getString(jo,"shortname");
		this.website = getString(jo,"website","?");
		Server [] chatrooms;
		try {
			JSONArray chatroomsJSON = jo.getJSONArray("chatrooms");
			chatrooms = new Server[chatroomsJSON.length()];
			for(int i = 0; i<chatroomsJSON.length(); ++i) {
				String servaddr = chatroomsJSON.getString(i);
				chatrooms[i] = new Server(servaddr);

			}
		}catch(JSONException jsonE) {	// Si il n'y a pas de chatroom
			if(Main.isDebug()) {
				System.err.println("Warning : Pas de chatroom pour "+this.shortname);
			}
			chatrooms = new Server[0];
		}
		this.comChan = chatrooms;
		this.progressStatus = getInt(jo,"progressStatus");
		this.membersCount = getInt(jo,"memberCount",0);
		this.subscribersCount = getInt(jo,"subscriberCount",0);
		try {
			this.joinDate = getString(jo,"ffdnMemberSince","?");
		}catch (JSONException joe) {
			this.joinDate = "?";
		}
		this.creationDate = getString(jo,"creationDate","?");
		this.email = getString(jo,"email","?");
		try {
			JSONObject coord = jo.getJSONObject("coordinates");
			this.coordonnees = new Coordinates(getDouble(coord, "latitude"), getDouble(coord, "longitude"));
		}catch(JSONException joe) {
			this.coordonnees = new Coordinates(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		}
		

	}
	
	private double getDouble(JSONObject jo, String key, double DEFAULT) {
		double res;
		try {
			res = jo.getDouble(key);
		}catch(JSONException jsone) {
			res = DEFAULT;
		}
		return res;
	}
	
	/**
	 * Recupere un double dans un objet Json
	 * @param jo Objet Json a parser
	 * @param key clé de la valeur
	 * @return double correspondant
	 */
	private double getDouble(JSONObject jo, String key) {
		return getDouble(jo, key, Double.POSITIVE_INFINITY);
	}

	private int getInt(JSONObject jo, String key, int DEFAULT) {
		int res;
		try {
			res = jo.getInt(key);
		}catch(JSONException jsone) {
			res = DEFAULT;
		}
		return res;
		
	}
	
	private int getInt(JSONObject jo, String key) {
		return getInt(jo,key,-1);
		
	}
	
	private String getString (JSONObject jo, String key, String DEFAULT) {
		String res;
		try {
			res = jo.getString(key);
		}catch(JSONException jsoe) {
			res = DEFAULT;
		}
		return res;
	}
	
	private String getString (JSONObject jo, String key) {
		return getString(jo, key, null);
	}
	

	public String getWebsite() {
		return website;
	}


	public String getDescription() {
		return description;
	}

	public Coordinates getCoordonnees() {
		return coordonnees;
	}

	public Server[] getIrcChan() {
		return comChan;
	}

	public String getEmail() {
		return email;
	}

	public int getProgressStatus() {
		return progressStatus;
	}


	public int getMembersCount() {
		return membersCount;
	}

	public int getSubscribersCount() {
		return subscribersCount;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public String getJoinDate() {
		return joinDate;
	}


	public boolean hasShortName() {
		return !(shortname==null || shortname.equals(""));
	}

	public String getShortname() {
		return shortname;
	}
	
	/**
	 * Transforme l'addresse mail. Si l'adresse est a null, alors renvoie la chaine vide a la place.
	 * @return L'adresse Email modifiée pour ne pas être en clair.
	 */
	public String emailSyntaxer() {
		String email = this.getEmail();
		String res="";
		if(email==null) {
			return res;
		}
		for(int i=0; i<email.length(); ++i) {
			if(email.charAt(i) == '@' ) {
				res +=" <at> ";
			}else if( email.charAt(i) == '.' ) {
				res +=" <dot> ";
			}else {
				res +=email.charAt(i);
			}
		}
		return res;
		
	}
}
