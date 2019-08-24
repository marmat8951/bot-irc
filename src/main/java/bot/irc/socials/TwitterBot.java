package bot.irc.socials;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import bot.irc.action.Action;
import bot.irc.data.Message;
import bot.irc.main.AffichableSurIRC;
import bot.irc.main.Bot;
import bot.irc.rss.RssData;
import bot.irc.rss.RssDataRemainder;
import twitter4j.DirectMessage;
import twitter4j.DirectMessageList;
import twitter4j.IDs;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

@SuppressWarnings("deprecation")
public class TwitterBot extends TwitterAdapter implements Bot, UserStreamListener, Runnable, Observer {
	
	private Twitter twitter;
	private TwitterStream twitterStream;
	private String login;
	private String botName = "Twitter";
	private List<Action> actions = Action.getAllActions(this);
	private Thread thread;
	private long timeout = 100;
	private String cursor = null;
	private final int COUNT = 50;
	
	boolean end = false;
	
	public TwitterBot() {
		super();
		TwitterFactory factory = new TwitterFactory();
		twitter = factory.getInstance();
		this.twitter = new TwitterFactory().getInstance();
		
		try {
			this.login = twitter.getScreenName();
		} catch (IllegalStateException | TwitterException e) {
			this.login = "UneFede";
			e.printStackTrace();
		}
		
		TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory();
        twitterStream = twitterStreamFactory.getInstance();
        twitterStream.addListener(this);

	}
	
	public void start() {
		System.out.println("Démarage du Bot "+botName+". Mise à jour toute les "+timeout+" secondes.");
		if(thread == null) {
			thread = new Thread(this, this.botName);
			thread.start();
		}
	}
	
	public void run() {
		do {
			try {
				
			DirectMessageList messages = twitter.getDirectMessages(COUNT);
			 for (DirectMessage message : messages) {
				 this.onDirectMessage(message);
				 twitter.destroyDirectMessage(message.getId());
				 String cr = messages.getNextCursor();
				 if(cr != null) {
					 cursor = cr;
				 }
			 }
			
			 
			}catch (TwitterException e) {
				e.printStackTrace();
			}finally {
				try {
					Thread.sleep(timeout*1000);
				 } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 }
			}
		} while(!end);
		
	}

	/* (non-Javadoc)
	 * @see twitter4j.TwitterAdapter#gotDirectMessage(twitter4j.DirectMessage)
	 */
	@Override
	public void gotDirectMessage(DirectMessage message) {
		super.gotDirectMessage(message);
		String s = message.getText();
		System.out.println("reçu sur twitter: "+s);
		Message m = new Message(s);
		String envoyeur="";
		try {
			envoyeur = twitter.showUser(message.getSenderId()).getName();
		} catch (TwitterException e1) {
			
			e1.printStackTrace();
		}finally {
			for (Action a: actions) {
				if(a.hasToReact(m)) {
					sendMessages(envoyeur, null, a.reactL("", envoyeur, s, login, m));
				}
			}
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
				twitter.sendDirectMessage(ids.getNextCursor(), "Bonjour! Pour apprendre toutes mes commandes, envoyez moi "+Action.CARACTERE_COMMANDE+"help");
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
			DirectMessage msg = this.twitter.sendDirectMessage(sender, message);
			System.out.println(botName+": Envoyé: "+ msg.getText());
		} catch (TwitterException e) {
			System.err.println(botName+" : Twitter : Impossible d'envoyer le message");
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
		return botName;
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
			twitter.updateStatus(status);
		} catch (TwitterException e) {
			System.err.println(botName+": Erreur: impossible de twitter");
			e.printStackTrace();
		}
		
	}

	
	@Override
	public void onDirectMessage(DirectMessage directMessage) {
		gotDirectMessage(directMessage);
	}
	
	@Override
	public void onStatus(Status status) {
		// donothing
		
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		// donothing
		
	}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		// donothing
		
	}

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {
		// donothing
		
	}

	@Override
	public void onStallWarning(StallWarning warning) {
		// donothing
		
	}

	@Override
	public void onException(Exception ex) {
		// donothing
		
	}

	@Override
	public void onDeletionNotice(long directMessageId, long userId) {
		// donothing
		
	}

	@Override
	public void onFriendList(long[] friendIds) {
		// donothing
		
	}

	@Override
	public void onFavorite(User source, User target, Status favoritedStatus) {
		// donothing
		
	}

	@Override
	public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
		// donothing
		
	}

	@Override
	public void onFollow(User source, User followedUser) {
		// donothing
		
	}

	@Override
	public void onUnfollow(User source, User unfollowedUser) {
		// donothing
		
	}



	@Override
	public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
		// donothing
		
	}

	@Override
	public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
		// donothing
		
	}

	@Override
	public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
		// donothing
		
	}

	@Override
	public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
		// donothing
		
	}

	@Override
	public void onUserListCreation(User listOwner, UserList list) {
		// donothing
		
	}

	@Override
	public void onUserListUpdate(User listOwner, UserList list) {
		// donothing
		
	}

	@Override
	public void onUserListDeletion(User listOwner, UserList list) {
		// donothing
		
	}

	@Override
	public void onUserProfileUpdate(User updatedUser) {
		// donothing
		
	}

	@Override
	public void onUserSuspension(long suspendedUser) {
		// donothing
		
	}

	@Override
	public void onUserDeletion(long deletedUser) {
		// donothing
		
	}

	@Override
	public void onBlock(User source, User blockedUser) {
		// donothing
		
	}

	@Override
	public void onUnblock(User source, User unblockedUser) {
		// donothing
		
	}

	@Override
	public void onRetweetedRetweet(User source, User target, Status retweetedStatus) {
		// donothing
		
	}

	@Override
	public void onFavoritedRetweet(User source, User target, Status favoritedRetweeet) {
		// do nothing
		
	}

	@Override
	public void onQuotedTweet(User source, User target, Status quotingTweet) {
		// do nothing
		
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o.getClass().equals(RssDataRemainder.class)) {
			RssData data = (RssData) arg;
			this.sendRSSMessage(data.toStringIRC());
		}
	}

	@Override
	public String getBotNickName() {
		try {
			return twitter.getScreenName();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
}
