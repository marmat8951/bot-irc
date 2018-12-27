package bot.irc.main;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bot.irc.rss.RSSChecker;
import bot.irc.rss.RssDataRemainder;
import bot.irc.socials.TwitterBot;


public class Main {

	public volatile static String SERVER = "irc.geeknode.net";
	public volatile static int PORT = 6667;
	private volatile static String[] CHANNELS = { "#marmat" };
	private static long TIMEOUT_BEFORE_RECONNECTING = 360;
	private static int failures = 0;
	private static volatile boolean DEBUG=true;
	private static CacheReloader CR;
	private static IRCBot IRCBOT;
	private static TwitterBot TWITTER;
	private static List<Bot> BOTS = new ArrayList<>();
	private static RssDataRemainder RSS_DATA_REMAINDER;
	
	public static final SimpleDateFormat DATE_FORMAT_OUT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.FRENCH);;

	public static void main(String[] args) throws Exception {

		try {
			
			CR = new CacheReloader(3600); // Met à jour la base toute les heures.
			// Now start our bot up.
			IRCBOT = new IRCBot();
			BOTS.add(IRCBOT);
			
			TWITTER = new TwitterBot();
			BOTS.add(TWITTER);
			
			RSSChecker rcheck = new RSSChecker("https://planet.ffdn.org/atom.xml", BOTS);
			RSS_DATA_REMAINDER = rcheck.getRemainder();
					
			//Properties Setter
			PropertiesSetter ps = new PropertiesSetter("../../ressources/config/config.properties");
			
			ps.setPropertiesOn(CR, IRCBOT,rcheck);

			// Connect to the IRC server.
			IRCBOT.connect(SERVER,PORT);

			// Get All the infomations and store in a cache
			Cache c = Cache.getInstance();

			// Join the #pircbot channel.
			for(int i = 0; i< CHANNELS.length; i++) {
				IRCBOT.joinChannel(CHANNELS[i]);
			}
			
			CR.start();
			
			System.out.println("Debug? "+DEBUG);
			
			rcheck.start();
			
			
		}catch(ConnectException ce) {
			failures++;
			System.err.println("Erreur numéro "+failures);
			System.err.println("La connection a l'adresse "+SERVER+":"+PORT+" a échoué. Le Bot retentera de se connecter dans "+TIMEOUT_BEFORE_RECONNECTING+" secondes");
			Thread.sleep(TIMEOUT_BEFORE_RECONNECTING*1000);
			main(args);
		}
	}

	public static boolean isDebug () {
		return Main.DEBUG;
	}

	public static void setDebug(boolean b) {
		System.out.println("Mise de debug à "+b);
		DEBUG=b;
	}

	public static final String getSERVER() {
		return SERVER;
	}

	public static final void setSERVER(String sERVER) {
		SERVER = sERVER;
	}

	public static final int getPORT() {
		return PORT;
	}

	public static final void setPORT(int pORT) {
		PORT = pORT;
	}

	public static final String[] getCHANNELS() {
		return CHANNELS;
	}

	public static final void setCHANNELS(String[] cHANNELS) {
		CHANNELS = cHANNELS;
	}

	public static final long getTIMEOUT_BEFORE_RECONNECTING() {
		return TIMEOUT_BEFORE_RECONNECTING;
	}

	public static final void setTIMEOUT_BEFORE_RECONNECTING(long tIMEOUT_BEFORE_RECONNECTING) {
		TIMEOUT_BEFORE_RECONNECTING = tIMEOUT_BEFORE_RECONNECTING;
	}

	/**
	 * @return the failures
	 */
	public static int getFailures() {
		return failures;
	}

	/**
	 * @return the dEBUG
	 */
	public static boolean isDEBUG() {
		return DEBUG;
	}

	/**
	 * @return the cR
	 */
	public static CacheReloader getCR() {
		return CR;
	}

	/**
	 * @return the iRCBOT
	 */
	public static IRCBot getIRCBOT() {
		return IRCBOT;
	}

	/**
	 * @return the bOTS
	 */
	public static List<Bot> getBOTS() {
		return BOTS;
	}

	/**
	 * @return the rSS_DATA_REMAINDER
	 */
	public static RssDataRemainder getRSS_DATA_REMAINDER() {
		return RSS_DATA_REMAINDER;
	}

	/**
	 * @return the dateFormatOut
	 */
	public static SimpleDateFormat getDateFormatOut() {
		return DATE_FORMAT_OUT;
	}

}
