package bot.irc.comportement;

import java.util.ArrayList;
import java.util.List;

import bot.irc.main.Bot;
import bot.irc.main.IRCBot;


public abstract class Comportement {

	private Bot bot;
	public Comportement(Bot b) {
		this.bot = b;
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
	
	public abstract List<String> react(String channel, String sender,
			String login, String hostname, String message);
	
	/**
	 * @return the bot
	 */
	public Bot getBot() {
		return bot;
	}


	/**
	 * @param iRCBot the bot to set
	 */
	public void setBot(Bot bot) {
		this.bot = bot;
	}

	/**
	 * 
	 * @return le nom actuel du bot placé en minuscules
	 */
	public String getBotNick() {
		return bot.getBotName().toLowerCase();
	}
}
