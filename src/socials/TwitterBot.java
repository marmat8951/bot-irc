package socials;

import java.util.List;

import actions.Action;
import data.Message;
import main.AffichableSurIRC;
import main.Bot;
import twitter4j.DirectMessage;
import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterBot extends TwitterAdapter implements Bot {
	
	private Twitter sender;
	private String login;
	private String BotName = "Twitter";
	
	public TwitterBot() {
		super();
		this.sender = TwitterFactory.getSingleton();
		try {
			this.login = sender.getScreenName();
		} catch (IllegalStateException | TwitterException e) {
			this.login = "UneFede";
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see twitter4j.TwitterAdapter#gotDirectMessage(twitter4j.DirectMessage)
	 */
	@Override
	public void gotDirectMessage(DirectMessage message) {
		super.gotDirectMessage(message);
		String s = message.getText();
		Message m = new Message(s);
		try {
			onMessage("", sender.showUser(message.getId()).getName(), s, "", s);
		} catch (TwitterException e) {
			
			e.printStackTrace();
		}
		
	}

	
	

	/* (non-Javadoc)
	 * @see twitter4j.TwitterAdapter#gotIncomingFriendships(twitter4j.IDs)
	 */
	@Override
	public void gotIncomingFriendships(IDs ids) {
		super.gotIncomingFriendships(ids);
		do{
			try {
				sender.sendDirectMessage(ids.getNextCursor(), "Bonjour! Pour apprendre toutes mes commandes, envoyez moi "+Action.CARACTERE_COMMANDE+"help");
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}while(ids.hasNext());
	}

	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		
		
	}

	@Override
	public void sendMessage(String sender, String channel, String message) {
		try {
			DirectMessage msg = this.sender.sendDirectMessage(sender, message);
			System.out.println(BotName+": Envoyé: "+ msg.getText() + " à @" + msg.getRecipientScreenName());
		} catch (TwitterException e) {
			System.err.println(BotName+"Twitter : Impossible d'envoyer le message");
			e.printStackTrace();
		}
		
	}

	@Override
	public void sendMessages(String sender, String channel, List<String> messages) {
		String m="";
		for(String s : messages) {
			m=m+s+'\n';
		}
		if(!m.equals("")) {
			sendMessage(sender, channel, m);
		}
	}

	@Override
	public void sendMessages(String sender, String channel, AffichableSurIRC affichable) {
		sendMessages(sender,channel,affichable.toStringIRC());
		
	}

	@Override
	public String getBotName() {
		return BotName;
	}

	@Override
	public void sendMessageToAdmins(String string) {
		//TODO Not yet implemented
		
	}

	@Override
	public void sendRSSMessage(List<String> messages) {
		String aff = "";
		for(String s : messages) {
			aff += s+"\n";
		}
		StatusUpdate status = new StatusUpdate(aff);
		try {
			sender.updateStatus(status);
		} catch (TwitterException e) {
			System.err.println(BotName+": Erreur: impossible de twitter");
			e.printStackTrace();
		}
		
	}
	
	

}
