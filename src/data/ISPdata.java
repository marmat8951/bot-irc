package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import IRC.Server;

public class ISPdata {

	private String website;
	private String description;
	private Server [] ircChan;
	private int progressStatus;
	private int membersCount;
	private int subscribersCount;
	private String email;
	private String creationDate;
	private String joinDate;
	
	
	

	public ISPdata(String website, String description, Server[] chatrooms, int progressStatus,
			int membersCount, int subscribersCount, String email, String creationDate, String joinDate) {
		super();
		this.website = website;
		this.description = description;
		this.ircChan = chatrooms;
		this.progressStatus = progressStatus;
		this.membersCount = membersCount;
		this.subscribersCount = subscribersCount;
		this.email=email;
		this.creationDate = creationDate;
		this.joinDate = joinDate;
	}
	
	public ISPdata(JSONObject jo){
		this.website = jo.getString("website");
		JSONArray chatroomsJSON = jo.getJSONArray("chatrooms");
		Server [] chatrooms = new Server[chatroomsJSON.length()];
		for(int i = 0; i<chatroomsJSON.length(); ++i) {
			String servaddr = chatroomsJSON.getString(i);
			chatrooms[i] = new Server(servaddr);
					
		}
		this.progressStatus = jo.getInt("progressStatus");
		this.membersCount = jo.getInt("memberCount");
		this.subscribersCount = jo.getInt("subscriberCount");
		try {
		this.joinDate = jo.getString("ffdnMemberSince");
		}catch (JSONException joe) {
			this.joinDate = "?";
		}
		this.creationDate = jo.getString("creationDate");
		
	}

	public String getWebsite() {
		return website;
	}


	public String getDescription() {
		return description;
	}

	


	public Server[] getIrcChan() {
		return ircChan;
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
	
	
}
