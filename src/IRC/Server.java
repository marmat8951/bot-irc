package IRC;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import main.AffichableSurIRC;

/**
 * Cette classe à pour but de stocker les serveurs/channels pour les FAI
 * @author Marmat
 *
 */

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
	
	/**
	 * Extrait le port d'une URI. par exemple irc://geeknode.net:6789#marmat renveira 6789
	 * @param s URI sur laquelle extraire le numéro de port
	 * @return Le numéro de port
	 */
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

	/**
	 * Extrait l'addresse d'une URI. par exemple irc://geeknode.net:789#marmat renveira geeknode.net
	 * @param s URI 
	 * @return L'addresse 
	 */
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
	
	/**
	 * Extrait le chan d'une URI. par exemple irc://geeknode.net:789#marmat renveira #marmat
	 * @param s URI 
	 * @return L'addresse 
	 */
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

	public List<String> toStringIRC(){
		List<String> res = new ArrayList<>();
		res.add(toString());
		return res;
	}

	
	public String toString() {
		
		if(isIRC()) {
			return "IRC://"+address+" chan:"+chan+" port:"+port;
		}else {
			return address;
		}
		
	}
	
	
	

}
