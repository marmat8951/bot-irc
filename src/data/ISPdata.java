package data;

public class ISPdata {

	public final ISP ISP;
	private String website;
	private String description;
	private String [] chatrooms;
	private int progressStatus;
	private int membersCount;
	private int subscribersCount;
	
	public ISPdata(ISP i) {
		this.ISP=i;
	}

	public String getWebsite() {
		return website;
	}


	public String getDescription() {
		return description;
	}

	public String[] getChatrooms() {
		return chatrooms;
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
