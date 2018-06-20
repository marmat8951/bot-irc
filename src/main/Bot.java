package main;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

import actions.Action;
import comportement.Comportement;
import data.Message;
import rss.RssDataRemainder;

public class Bot extends PircBot {

	private volatile static long TIME_BETWEEN_MESSAGES = 200;
	private List<Action> actions = Action.getAllActions(this);
	private List<Comportement> comportements = Comportement.getAllComportements(this);
	private String[] admins;
	private boolean responseOnPrivateChannel = true;
	private boolean responseOnPrivateMessages = true;
	private volatile static long WAIT_BEFORE_RECONNECT = 60;
	private volatile RssDataRemainder rssdata = new RssDataRemainder();
	

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
	
	
	public void onDisconnect() {
		Date d = new Date();
		System.err.println(Main.DATE_FORMAT_OUT.format(d)+": Je viens d'être déconnectée!");
		this.sendMessageToAdmins("Je viens d'être déconnectée!");

		while(!this.isConnected()) {
		try {
			Thread.sleep(WAIT_BEFORE_RECONNECT*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			try {
				this.reconnect();
			}catch (UnknownHostException e) {
				
			
			} catch (NickAlreadyInUseException e) {
				this.sendMessageToAdmins("Je viens d'être déconnectée et en tentant de me reconnecter mon nom était déjà utilisé!");
				e.printStackTrace();
			} catch (IOException e) {
				this.sendMessageToAdmins("Je viens d'être déconnectée et en tentant de me reconnecter, j'ai eu une IOException: "+e.toString());
				e.printStackTrace();
			} catch (IrcException e) {
				this.sendMessageToAdmins("Je viens d'être déconnectée et en tentant de me reconnecter, j'ai eu une IrcException: "+e.toString());
				e.printStackTrace();
			}
		}
		this.joinChannels();
		this.sendMessageToAdmins("Je viens de me reconnecter, j'était abscente depuis le "+Main.DATE_FORMAT_OUT.format(d));
		System.err.println("Je viens de me reconnecter, j'était abscente depuis le "+Main.DATE_FORMAT_OUT.format(d));
	}
	
	public final void joinChannels() {
		String[] channels = Main.getCHANNELS();
		for(int i = 0; i< channels.length; i++) {
			joinChannel(channels[i]);
		}
	}
	
	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		Message m = new Message(message);
		System.out.println(m.toString());
		for(Action a:actions){
			if(a.hasToReact(message)) {
				a.react(channel, sender, login, hostname, message);
			}
		}
		//easter Eggs
		String ea="Ehlo "+this.getNick();
		if (message.toLowerCase().contains(ea.toLowerCase())) {
			sendMessage(channel, "Ehlo "+sender+"!!");
		}
		
		if(message.toLowerCase().contains("mousse au chocolat")) {
			sendMessage(channel, "J'adore la mousse au chocolat de Benjamin B.");
		}
		for(Comportement c : comportements) {
			if(c.hastoreact(message)) {
				c.react(channel, sender, login, hostname, message);
			}
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
	
	public void sendMessageOnAllChannels(String message) {
		String[] chans = this.getChannels();
		for(int i=0;i<chans.length;i++) {
			this.sendMessage(chans[i], message);
		}
	}
	
	public void sendMessagesOnAllChannels(List<String> messages) {
		for(String s : messages) {
			sendMessageOnAllChannels(s);
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
	
	
	public void sendMessages(String sender, String channel, AffichableSurIRC affichable) {
		sendMessages(sender,channel,affichable.toStringIRC());
	}


	public RssDataRemainder getRssdata() {
		return rssdata;
	}


	public void setRssdata(RssDataRemainder rssdata) {
		this.rssdata = rssdata;
	}

}
