package main;

import org.jibble.pircbot.PircBot;

/**
 * Classe de Thread, servant à ce que lorsque le bot est déconnecté, à se reconnecter, et a retourner sur les thread.
 * @author marmat
 *
 */
public class RejoinThread implements Runnable{

	private volatile PircBot pb;
	private String chan;
	private static long DEFAULT_WAIT_BEFORE_RECONNECT = 10000;
	private static long DELAY_BEFORE_MESSAGE_AFTER_RECONNECT = 360;
	private Thread thread;
	private String threadName;
	private int failures;


	public RejoinThread(PircBot pb, String chan) {
		super();
		this.pb = pb;
		this.chan = chan;
		this.threadName = "Rejoin "+chan;
	}


	@Override
	public void run() {
		String [] chansWeAreIn;
		boolean connected = false;
		pb.sendMessage("Marmat", "A l'aide, je viens de me faire expulser de "+chan+" j'attend "+DEFAULT_WAIT_BEFORE_RECONNECT/1000+" secondes et je retente de me connecter");
		do {
			try {
				
				Thread.sleep(DEFAULT_WAIT_BEFORE_RECONNECT);
				// On patiente une minute avant chaque tentative
				pb.joinChannel(chan);
				
				chansWeAreIn = pb.getChannels();
				if(Main.isDebug()) {
					System.out.println("Nous sommes de manière sûre dans les channels :");
				}
				for(int i=0; i<chansWeAreIn.length; ++i) {
					if(Main.isDebug()) {
					System.out.println(chansWeAreIn[i]);
					}
					if(chansWeAreIn[i].equals(chan)) {
						connected = true;
					}
				}
			}catch (Exception e) {
				System.err.println("Je n'ai pas pu attendre avant de re-rejoindre le channel "+chan+" duquel je me suis fait kicker.");
			}

		}while(!connected);
		try {
			Thread.sleep(DELAY_BEFORE_MESSAGE_AFTER_RECONNECT*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pb.sendMessage(chan, "Il semblerai que je me soit fait kicker récement. En cas de problème avec moi, merci de contacter adminsys<at>listes<dot>ffdn<dot>org , root[arobaSe]marmat[point]ovh, ou @Marmat sur l'IRC. ");
		pb.sendMessage(chan, "Vous pouvez aussi ouvrir un ticket ici: https://code.ffdn.org/marmat8951/bot-irc2/issues");

	}

	/**
	 * Methode lancée lorsque le bot est kick qui va initialiser et démarer le thread de reconnexion
	 */
	public void start() {
		System.err.println("Je me suis fait kicker de "+chan+" attente de "+DEFAULT_WAIT_BEFORE_RECONNECT/1000+" secondes entre chaque tentative de reconnection");
		if(thread == null) {
			thread = new Thread(this, this.threadName);
			thread.start();
		}
		
	}


	public int getFailures() {
		return failures;
	}


	/**
	 * @return the dEFAULT_WAIT_BEFORE_RECONNECT
	 */
	public static long getDEFAULT_WAIT_BEFORE_RECONNECT() {
		return DEFAULT_WAIT_BEFORE_RECONNECT;
	}


	/**
	 * @param dEFAULT_WAIT_BEFORE_RECONNECT the dEFAULT_WAIT_BEFORE_RECONNECT to set
	 */
	public static void setDEFAULT_WAIT_BEFORE_RECONNECT(long dEFAULT_WAIT_BEFORE_RECONNECT) {
		DEFAULT_WAIT_BEFORE_RECONNECT = dEFAULT_WAIT_BEFORE_RECONNECT;
	}

	
}
