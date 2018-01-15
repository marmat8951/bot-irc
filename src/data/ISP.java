package data;

import java.util.Date;

public class ISP {
	
	private String name;
	private int id;
	private boolean isFFDNMember;
	private Date date_added;
	private Date last_update;
	private ISPdata data;
	
	
	public ISP(String name, int id, boolean isFFDNMember, Date date_added, Date last_update, ISPdata data) {
		super();
		this.name = name;
		this.id = id;
		this.isFFDNMember = isFFDNMember;
		this.date_added = date_added;
		this.last_update = last_update;
		this.data = data;
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
	
	public String toString() {
		String res="";
		res+=name+" : \n";
		res+="Est membre: "+isFFDNMember()+" \n";
		res+="Nombre de membres: "+getMembersCount()+" Nombre d'abonnements:"+getSubscribersCount(); 
		return res;
	}

}
