package data;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import IRC.Server;

public class ISPdata {

	private ISP ISP;
	private String website;
	private String description;
	private Server [] ircChan;
	private int progressStatus;
	private int membersCount;
	private int subscribersCount;
	
	
	
	

	public ISPdata(data.ISP ISP, String website, String description, Server[] chatrooms, int progressStatus,
			int membersCount, int subscribersCount) {
		super();
		this.ISP = ISP;
		this.website = website;
		this.description = description;
		this.ircChan = chatrooms;
		this.progressStatus = progressStatus;
		this.membersCount = membersCount;
		this.subscribersCount = subscribersCount;
	}
	
	public ISPdata(ISP isp, JSONObject jo){
		this.ISP = ISP;
		this.website = jo.getString("website");
		isp.setName(jo.getString("name"));
		JSONArray chatrooms = jo.getJSONArray("chatrooms");
	}

	public String getWebsite() {
		return website;
	}


	public String getDescription() {
		return description;
	}

	public String[] getChatrooms() {
		return ircChan;
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
	
	
}
