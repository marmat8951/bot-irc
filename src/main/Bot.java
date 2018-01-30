package main;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jibble.pircbot.PircBot;

import actions.Action;
import data.CoveredAreas;
import data.ISP;
import data.ISPDAO;
import verif_saisie.EntierPositifNonVide;

public class Bot extends PircBot {

	public static final long TIME_BETWEEN_MESSAGES = 200;
	public static char CARACTERE_COMMANDE = '+';
	private ISPDAO idao;
	private List<Action> actions = Action.getAllActions(this);

	public Bot() {
		this.setName("UneFede2");
		this.setMessageDelay(TIME_BETWEEN_MESSAGES);
		if(Main.isDebug()) {
			this.setVerbose(true);
		}else {
			this.setVerbose(false);
		}
		idao = ISPDAO.getInstance();
	}

	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		
		for(Action a:actions){
			if(a.hasToReact(message)) {
				a.react(channel, sender, login, hostname, message);
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
