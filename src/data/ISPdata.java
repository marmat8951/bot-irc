package data;

public class ISPdata {

	public final ISP ISP;
	private String website;
	private String description;
	private String [] chatrooms;
	private int progressStatus;
	private int membersCount;
	private int subscribersCount;
	
	
	
	

	public ISPdata(data.ISP iSP, String website, String description, String[] chatrooms, int progressStatus,
			int membersCount, int subscribersCount) {
		super();
		ISP = iSP;
		this.website = website;
		this.description = description;
		this.chatrooms = chatrooms;
		this.progressStatus = progressStatus;
		this.membersCount = membersCount;
		this.subscribersCount = subscribersCount;
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
