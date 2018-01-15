package data;

import java.util.Date;

public class ISP {
	private int id;
	private boolean isFFDNMember;
	private Date date_added;
	private Date last_update;
	private ISPdata data;
	
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
	public Date getDate_added() {
		return date_added;
	}
	public void setDate_added(Date date_added) {
		this.date_added = date_added;
	}
	public Date getLast_update() {
		return last_update;
	}
	public void setLast_update(Date last_update) {
		this.last_update = last_update;
	}
	
	public int getMembersCount() {
		return data.getMembersCount();
	}

	public int getSubscribersCount() {
		return data.getSubscribersCount();
	}
	

}
