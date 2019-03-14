package bot.irc.comportement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bot.irc.main.Bot;
import bot.irc.main.IRCBot;
import bot.irc.main.Main;

/**
 * Singleton de comportement pour pouvoir demander au bot un thé ou un café
 * @author marmat
 */
public class Cafe extends Comportement {
	public static volatile Cafe instance = null;
	public Map<String, Boolean> rep = new HashMap<>();
	public Map<String, Long> lastcafe = new HashMap<>();
	public Map<String, Long> lastthe= new HashMap<>();
	public static final long MAX_THE = 30;
	public static final long MAX_CAFE = 360;
	
	private Cafe(Bot b) {
		super(b);
	}
	
	@Override
	public boolean hastoreact(String mesg) {
		String m=mesg.toLowerCase();
		return m.contains(getBotNick())&&(m.contains("fais") || m.contains("fait")) && (m.contains("café")||m.contains("thé"));
	}

	public List<String> react(String channel, String sender, String login, String hostname, String message) {
		List<String> res = new ArrayList<>();
		Long lastone;
		Date d = new Date();
		if(istheorcafe(message.toLowerCase())) {
			lastone = lastthe.get(sender);
			if(lastone == null) {
				res.add("Ok pour le thé");
			}else {
				if(d.getTime()-lastone < MAX_THE*1000) {
					res.add("Ok, mais la dernière fois c'était il y a moins de "+MAX_THE+" secondes, tu devrais y aller plus doucement...");
				}else {
					res.add("Ok, pas de problème. La dernière fois c'était le "+Main.DATE_FORMAT_OUT.format(new Date(lastone)));
				}
			}
			lastthe.put(sender, d.getTime());
		}else {
			lastone = lastcafe.get(sender);
			if(sender.toLowerCase().contains("quota_atypique")) {
				res.add("Ok Quota!");
			}else if(lastone == null) {
				res.add("Ok pour le café!");
			}else {
				if(d.getTime()-lastone < MAX_CAFE*1000) {
					res.add("Eu... ok, mais, tu devrai plutôt prendre un thé, ça fait vraiment trop peu de temps la... ");
				}else {
					res.add("Ok, pas de problème, la dernière fois c'était le "+Main.DATE_FORMAT_OUT.format(new Date(lastone)));
				}
			}
			lastcafe.put(sender, d.getTime());
		}
		return res;
	}
	/**
	 * Indique si il s'agit de thé ou de café qui est demandé
	 * @param msg message a verifier
	 * @return true = thé false = café
	 */
	private boolean istheorcafe(String msg) {
		return msg.contains("thé");
	}
	
	/**
	 * Renvoie l'instance de l'objet, ou le crée si besoin
	 * @param b
	 * @return
	 */
	public final static Cafe getInstance(Bot b) {
		if (Cafe.instance == null) {
			synchronized (Cafe.class) {
				if(Cafe.instance == null) {
					Cafe.instance = new Cafe(b);
				}
			}
		}
		return Cafe.instance;
	}
}
