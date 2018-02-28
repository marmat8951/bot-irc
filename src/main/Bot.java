package main;

import java.util.List;

import org.jibble.pircbot.PircBot;

import actions.Action;

public class Bot extends PircBot {

	private volatile static long TIME_BETWEEN_MESSAGES = 200;
	private List<Action> actions = Action.getAllActions(this);
	private String[] admins;
	private boolean responseOnPrivateChannel = true;
	private boolean responseOnPrivateMessages = true;
	

	public Bot() {
		this.setAutoNickChange(true);
		this.setName("UneFede");
		this.setVersion("Gentille Droide de la fédération, <3 Marmat");
		this.setMessageDelay(TIME_BETWEEN_MESSAGES);
		if(Main.isDebug()) {
			this.setVerbose(true);
		}else {
			this.setVerbose(false);
		}
	}
	
	public void onPrivateMessage(String sender, String login, String hostname, String message) {
		if(responseOnPrivateMessages) {
			onMessage(sender, sender, login, hostname, message);
		}else {
			sendMessage(sender,"Eh non, Je suis configurée pour ne pas répondre aux MP. Désolé!");
		}
	}
	
	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		
		for(Action a:actions){
			if(a.hasToReact(message)) {
				a.react(channel, sender, login, hostname, message);
			}
		}
		//easter Egg
		String ea="Ehlo "+this.getNick();
		if (message.toLowerCase().contains(ea.toLowerCase())) {
			sendMessage(channel, "Ehlo "+sender+"!!");
		}

	}

	public void onMode(String channel,
            String sourceNick,
            String sourceLogin,
            String sourceHostname,
            String mode) {
		mode = mode.toLowerCase();

		if(mode.contains("pircbot") && mode.substring(0, 2).equals("+b") && sourceLogin.toLowerCase().contains("abitbolg")) {
			sendMessageToAdmins("Help, il semblerai que je me soit fait ban sur "+channel+" Tu peux vérifier?");
		}
	}
	
	
	/**
	 * Envoie les messages les uns à la suite de l'autre dans l'ordre de la liste.
	 * @param channel Channel IRC dans lequel envoyer les messages
	 * @param lines liste de chaaines de caractères à envoyer.
	 */
	private void sendMessage(String channel, List<String> lines) {
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

	/**
	 * @return the responseOnPrivateChannel
	 */
	public boolean isResponseOnPrivateChannel() {
		return responseOnPrivateChannel;
	}

	/**
	 * @param responseOnPrivateChannel the responseOnPrivateChannel to set
	 */
	public void setResponseOnPrivateChannel(boolean responseOnPrivateChannel) {
		this.responseOnPrivateChannel = responseOnPrivateChannel;
	}

	/**
	 * @return the responseOnPrivateMessages
	 */
	public boolean isResponseOnPrivateMessages() {
		return responseOnPrivateMessages;
	}

	/**
	 * @param responseOnPrivateMessages the responseOnPrivateMessages to set
	 */
	public void setResponseOnPrivateMessages(boolean responseOnPrivateMessages) {
		this.responseOnPrivateMessages = responseOnPrivateMessages;
	}

	public void sendMessage(String sender,String channel,String message) {
		if(this.responseOnPrivateChannel) {
			this.sendMessage(sender, message);
		}else {
			this.sendMessage(channel, message);
		}
	}
	
	public void sendMessages(String sender, String channel,List<String> messages) {
		if(this.responseOnPrivateChannel) {
			this.sendMessage(sender, messages);
		}else {
			this.sendMessage(channel, messages);
		}
	}
	
	

}
