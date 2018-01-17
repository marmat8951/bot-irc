package main;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jibble.pircbot.PircBot;

import data.ISP;
import data.ISPDAO;
import verif_saisie.EntierPositifNonVide;

public class Bot extends PircBot {

	public static final long TIME_BETWEEN_RELOADS = 360000;
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

		if (message.contains("+liste")) {
			list(channel, sender, login, hostname, message);
		}

		if(message.equals("+reload")) {
			Date now = new Date();
			Date lastCU = Cache.getInstance().getLastCacheUpdate();
			if(lastCU.getTime() < now.getTime()-TIME_BETWEEN_RELOADS ) {		// Si la dernière MAJ date de + de 5 minutes
				sendMessage(channel, "Je lance le reload!");
				if(reload()) {
					sendMessage(channel, sender+": Le reload s'est bien passé.");
				}else {
					sendMessage(channel, sender+": Erreur au moment du reload.");
				}
			}else {
				Date nextAllowed = new Date(lastCU.getTime()+TIME_BETWEEN_RELOADS);
				sendMessage(channel, "Trop de reload, attendez un peu. Le dernier à eu lieu le "+lastCU.toString()+" Prochain autorisé le "+nextAllowed);
			}
		}


		//easter Egg
		String ea="Ehlo UneFede";
		if (message.contains("Ehlo UneFede")) {
			sendMessage(channel, "Ehlo "+sender+"!!");
		}



	}

	private boolean reload() {
		Cache c = Cache.getInstance();
		return c.reload();
	}

	public void sendMessage(String channel, List<String> lines) {
		for(String s : lines) {
			sendMessage(channel,s);
		}
	}


	public void list(String channel, String sender, String login, String hostname, String message) {
		Cache c = Cache.getInstance();
		List<ISP> listeFAI=null;
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

		List<String> messages = new LinkedList<>();
		messages.add("Les FAI surveillés par mes petits yeux mignons de bot sont:");
		String s="";

		for(ISP isp: listeFAI ) {
			if(isp.isFFDNMember()) {

				s+= isp.getBetterName();

				if(s.length()>=80) {
					messages.add(s);
					s="";
				}else {
					s+=", ";
				}
			}
		}
		messages.add(s);
		sendMessage(channel, messages);


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
