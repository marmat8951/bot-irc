package comportement;

import java.util.ArrayList;
import java.util.List;

import main.Bot;

public abstract class Comportement {

	private Bot bot;
	public Comportement(Bot b) {
		this.bot = b;
	}


	public static List<Comportement> getAllComportements(Bot b){
		List<Comportement> liste = new ArrayList<>();
		liste.add(Cafe.getInstance(b));
		return liste;
	}
	
	public abstract boolean hastoreact(String message);
	
	public abstract void react(String channel, String sender,
			String login, String hostname, String message);


	/**
	 * @return the bot
	 */
	public Bot getBot() {
		return bot;
	}


	/**
	 * @param bot the bot to set
	 */
	public void setBot(Bot bot) {
		this.bot = bot;
	}
	
	public String getBotNick() {
		return bot.getNick().toLowerCase();
	}
}
