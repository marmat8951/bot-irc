package main;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import rss.RSSChecker;

public class Main {

	public volatile static String SERVER = "irc.geeknode.net";
	public volatile static int PORT = 6667;
	private volatile static String[] CHANNELS = { "#marmat" };
	private static long TIMEOUT_BEFORE_RECONNECTING = 360;
	private static int failures = 0;
	private static boolean DEBUG=true;
	
	public static final SimpleDateFormat DATE_FORMAT_OUT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.FRENCH);;

	public static void main(String[] args) throws Exception {

		try {
			
			CacheReloader cr = new CacheReloader(3600); // Met à jour la base toute les heures.
			// Now start our bot up.
			Bot bot = new Bot();
			
			RSSChecker rcheck = new RSSChecker("https://planet.ffdn.org/atom.xml", bot);
			
			//Properties Setter
			PropertiesSetter ps = new PropertiesSetter("../../ressources/config/config.properties");
			
			ps.setPropertiesOn(cr, bot,rcheck);

			// Connect to the IRC server.
			bot.connect(SERVER,PORT);

			// Get All the infomations and store in a cache
			Cache c = Cache.getInstance();

			// Join the #pircbot channel.
			for(int i = 0; i< CHANNELS.length; i++) {
				bot.joinChannel(CHANNELS[i]);
			}
			
			cr.start();
			if(args.length>0) {
				setDebug(args[0].equals("-debug"));
				}else {
					setDebug(false);
			}
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

}
