package IRC;

import java.util.List;

public class Server {
	private String address;
	private int port;
	private List<String> chan;
	
	
	public Server(String address, int port, List<String> chan) {
		super();
		this.address = address;
		this.port = port;
		this.chan = chan;
	}
	
	public Server(String gen) {
		String serv = gen;
		if(gen.substring(0, 7).equals("irc://")) {
			
			if(serv.contains(":")) {
				String val = serv.
				port = Integer.parseInt();
			}else {
				port = 6667;
			}
		}else {
			throw new IllegalArgumentException("Le serveur "+gen+" n'est pas un serveur IRC valide");
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
	public List<String> getChan() {
		return chan;
	}
	public void setChan(List<String> chan) {
		this.chan = chan;
	}
	
	
	

}
