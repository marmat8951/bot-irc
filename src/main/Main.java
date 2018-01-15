package main;

import java.net.ConnectException;

public class Main {
	
	public static final String SERVER = "irc.geeknode.net";
	public static final int PORT = 6667;
	public static final String[] CHANNELS = { "#marmat" };
	
	public static void main(String[] args) throws Exception {
		try {
        // Now start our bot up.
        Bot bot = new Bot();
        
        // Enable debugging output.
        bot.setVerbose(true);
        
        // Connect to the IRC server.
        
        bot.connect(SERVER,PORT);
        // Join the #pircbot channel.
        for(int i = 0; i< CHANNELS.length; i++) {
        bot.joinChannel(CHANNELS[i]);
        }
        
		}catch(ConnectException ce) {
			System.err.println("La connection a l'adresse "+SERVER+":"+PORT+" a échoué. Le Bot s'arrete.");
			System.exit(1);
		}
	}

}
