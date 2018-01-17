package IRC;

import java.util.LinkedList;
import java.util.List;

import main.AffichableSurIRC;

public class Server implements AffichableSurIRC{
	private String address;
	private int port;
	private String chan;
	private Type t;
	
	private enum Type{
		IRC,AUTRE;
	}
	
	public Server(String address, int port, String chan) {
		super();
		this.address = address;
		this.port = port;
		this.chan = chan;
	}
	
	public Server(String gen) {
		
		if(gen.substring(0, 7).equals("irc://")) {
			this.t=Type.IRC;
			this.port = getSrvPort(gen);
			this.address = getSrvAddr(gen);
			this.chan = getSrvChan(gen);
		}else {
			this.t=Type.AUTRE;
			this.address=gen;
		}
		
		
	}
	private int getSrvPort(String s) {
		int res;
		if(s.contains(":")) {
			int idx = s.indexOf(':');
			idx++;
			String val = "";
			while(s.charAt(idx)<=9 || s.charAt(idx)>=0) {
				val += s.charAt(idx);
				idx++;
			}
			res = Integer.parseInt(val);
		}else {
			res = 6667; 
		}
		return res;
	}

	private String getSrvAddr(String s) {
		String res="";
		String serv=s.substring(7); // on enleve irc://
		int idxcar = 0;
		while(idxcar<serv.length() && serv.charAt(idxcar)!=':' && serv.charAt(idxcar)!='#') {
			res+= serv.charAt(idxcar);
			idxcar++;
		}
		return res;
	}
	
	private String getSrvChan(String s) {
		int idxcar = s.indexOf('#');
		return s.substring(idxcar+1);
		
		
	}
	

	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public String getChan() {
		return chan;
	}

	public void setChan(String chan) {
		this.chan = chan;
	}

	public boolean isIRC() {
		return this.t.equals(Type.IRC);
	}

	@Override
	public List<String> toStringIRC() {
		List<String> s = new LinkedList<>();
		if(isIRC()) {
			s.add("IRC://"+address+" chan:"+chan+" port:"+port);
		}else {
			s.add(address);
		}
		return s;
	}
	
	
	

}
