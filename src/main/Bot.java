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

			String s = message.substring(message.indexOf(' ')+1);
			if(!EntierPositifNonVide.verifie(s)) {			// Un mot après +info
				if(s.equalsIgnoreCase("all")) {	            // +info all
					Cache c = Cache.getInstance();
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

				}

			}else {											// Un nombre après +info

				int  id = Integer.parseInt(message.substring(message.indexOf(' ')+1));
				List<String> strings = idao.getISP(id).toStringIRC();
				for(String response : strings) {
					sendMessage(channel,response);
				}
			}

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
}
