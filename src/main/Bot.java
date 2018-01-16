package main;

import java.util.List;

import org.jibble.pircbot.PircBot;

import data.ISP;
import data.ISPDAO;
import verif_saisie.EntierPositifNonVide;

public class Bot extends PircBot {

	private ISPDAO idao;

	public Bot() {
		this.setName("UneFede2");
		idao = ISPDAO.getInstance();
	}

	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		if (message.equalsIgnoreCase("time?")) {
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now " + time);
		}

		if (message.contains("+info")) {

			info(channel,sender,login,hostname,message);

		}

		//easter Egg
		String ea="Ehlo UneFede";
		if (message.contains("Ehlo UneFede")) {
			sendMessage(channel, "Ehlo "+sender+"!!");
		}



	}

	public void sendMessage(String channel, List<String> lines) {
		for(String s : lines) {
			sendMessage(channel,s);
		}
	}
	
	
	public void info(String channel, String sender,
	String login, String hostname, String message) {
		
		String s = message.substring(message.indexOf(' ')+1);
		if(!EntierPositifNonVide.verifie(s)) {			// Un mot après +info
			
			
			if(s.equalsIgnoreCase("all")) {	          			  // +info all
				Cache c = Cache.getInstance();
				sendMessage(channel, c.toStringIRC());
				
				
				
				List<ISP> listeFAI;
				try {
					listeFAI = c.getListe();
				}catch (Exception e) {
					try {
						listeFAI = idao.getISPs();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				for(ISP i : c.getListe()) {
					if(i.isFFDNMember()) {
						sendMessage(channel, i.toStringIRC());
					}
				}

			}else if(s.equalsIgnoreCase("ffdn")) {				//+info ffdn
				Cache c = Cache.getInstance();
				sendMessage(channel, c.toStringIRC());
				
			}else {
				Cache c = Cache.getInstance();
				ISP i = c.getISPWithName(s);
				if(i == null) {
					sendMessage(channel, "Le FAI "+s+" est Inconnu, désolé");
				}else {
					sendMessage(channel, i.toStringIRC());
				}
			}

		}else {											// Un nombre après +info

			int  id = Integer.parseInt(message.substring(message.indexOf(' ')+1));
			List<String> strings = idao.getISP(id).toStringIRC();
			for(String response : strings) {
				sendMessage(channel,response);
			}
		}
		
	}
	
}
