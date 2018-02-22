package main;

import java.util.List;

import org.jibble.pircbot.PircBot;

import actions.Action;

public class Bot extends PircBot {

	private static long TIME_BETWEEN_MESSAGES = 200;
	private List<Action> actions = Action.getAllActions(this);
	private String[] admins;

	public Bot() {
		this.setName("UneFede2");
		this.setVersion("Gentille Droide de la fédération, <3 Marmat");
		this.setMessageDelay(TIME_BETWEEN_MESSAGES);
		if(Main.isDebug()) {
			this.setVerbose(true);
		}else {
			this.setVerbose(false);
		}
	}
	
	public void onPrivateMessage(String sender, String login, String hostname, String message) {
		this.sendMessage(sender, "Bonjour a toi "+sender+". Pas la peine de me demander mon 06, je ne répond pas aux messages privés. Sauf certains spéciaux.");
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
		if (message.contains(ea)) {
			sendMessage(channel, "Ehlo "+sender+"!!");
		}

	}

	
	/**
	 * Envoie les messages les uns à la suite de l'autre dans l'ordre de la liste.
	 * @param channel Channel IRC dans lequel envoyer les messages
	 * @param lines liste de chaaines de caractères à envoyer.
	 */
	public void sendMessage(String channel, List<String> lines) {
		for(String s : lines) {
			sendMessage(channel,s);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void onKick (String channel, String kickerNick, String login, String hostname, String recipientNick, String reason) {
		if(recipientNick.equalsIgnoreCase(this.getNick())){
			RejoinThread rj = new RejoinThread(this,channel);
			rj.start();
		}
		
	}
	
	public void sendMessageToAdmins(String message) {
		for(int i=0;i<admins.length;i++) {
			sendMessage(admins[i], message);
		}
	}
	
	public void sendMessageToAdmins(List<String> messages) {
		for(int i=0;i<admins.length;i++) {
			sendMessage(admins[i], messages);
		}
	}
	
	
	/**
	 * @return the tIME_BETWEEN_MESSAGES
	 */
	public static long getTIME_BETWEEN_MESSAGES() {
		return TIME_BETWEEN_MESSAGES;
	}

	/**
	 * @param tIME_BETWEEN_MESSAGES the tIME_BETWEEN_MESSAGES to set
	 */
	public static void setTIME_BETWEEN_MESSAGES(long tIME_BETWEEN_MESSAGES) {
		TIME_BETWEEN_MESSAGES = tIME_BETWEEN_MESSAGES;
	}

	/**
	 * @return the admins
	 */
	public String[] getAdmins() {
		return admins;
	}

	/**
	 * @param admins the admins to set
	 */
	public void setAdmins(String[] admins) {
		this.admins = admins;
	}
	
	

}
