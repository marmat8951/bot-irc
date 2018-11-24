package comportement;

import java.util.ArrayList;
import java.util.List;

import main.IRCBot;

public abstract class Comportement {

	private IRCBot iRCBot;
	public Comportement(IRCBot b) {
		this.iRCBot = b;
	}

	/**
	 * Crée et renvoie une liste de tous les comprtements présents.
	 * @param b bot que les actions utiliseront pour répondre.
	 * @return Liste de tous les comportements.
	 */
	public static List<Comportement> getAllComportements(IRCBot b){
		List<Comportement> liste = new ArrayList<>();
		liste.add(Cafe.getInstance(b));
		liste.add(Philo.getInstance(b));
		return liste;
	}
	
	public abstract boolean hastoreact(String message);
	
	public abstract void react(String channel, String sender,
			String login, String hostname, String message);


	/**
	 * @return the bot
	 */
	public IRCBot getBot() {
		return iRCBot;
	}


	/**
	 * @param iRCBot the bot to set
	 */
	public void setBot(IRCBot iRCBot) {
		this.iRCBot = iRCBot;
	}

	/**
	 * 
	 * @return le nom actuel du bot placé en minuscules
	 */
	public String getBotNick() {
		return iRCBot.getNick().toLowerCase();
	}
}
