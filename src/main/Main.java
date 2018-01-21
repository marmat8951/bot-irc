package main;

import java.net.ConnectException;

public class Main {

	public static final String SERVER = "irc.geeknode.net";
	public static final int PORT = 6667;
	public static final String[] CHANNELS = { "#marmat" };
	public static final long TIMEOUT_BEFORE_RECONNECTING = 360;
	public static final Cache cache = Cache.getInstance();
	public static int failures = 0;
	private static boolean DEBUG;

	public static void main(String[] args) throws Exception {

		try {
			setDebug(args[0].equals("-debug"));



			// Now start our bot up.
			Bot bot = new Bot();



			// Connect to the IRC server.
			bot.connect(SERVER,PORT);

			// Get All the infomations and store in a cache
			Cache c = Cache.getInstance();

			// Join the #pircbot channel.
			for(int i = 0; i< CHANNELS.length; i++) {
				bot.joinChannel(CHANNELS[i]);
			}
			CacheReloader cacheReloader = new CacheReloader(3600); // Met à jour la base toute les heures.
			cacheReloader.start();
			
			
			
			if(isDebug()) {
				// Enable debugging output.
				bot.setVerbose(true);
			}else {
				bot.setVerbose(false);
			}
			
			System.out.println("Debug? "+DEBUG);

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

	private static void setDebug(boolean b) {
		System.out.println("Mise de debug à "+b);
		DEBUG=b;
	}
}
