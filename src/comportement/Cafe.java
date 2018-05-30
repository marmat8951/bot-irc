package comportement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import main.Bot;
import main.Main;

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

	@Override
	public void react(String channel, String sender, String login, String hostname, String message) {
		Long lastone;
		Date d = new Date();
		Bot b = this.getBot();
		if(istheorcafe(message.toLowerCase())) {
			lastone = lastthe.get(sender);
			if(lastone == null) {
			b.sendMessage(channel, "Ok pour le thé");
			}else {
				if(d.getTime()-lastone < MAX_THE*1000) {
					b.sendMessage(channel, "Ok, mais la dernière fois c'était il y a moins de "+MAX_THE+" secondes, tu devrais y aller plus doucement...");
				}else {
					b.sendMessage(channel, "Ok, pas de problème. La dernière fois c'était le "+Main.DATE_FORMAT_OUT.format(new Date(lastone)));
				}
			}
			lastthe.put(sender, d.getTime());
		}else {
			lastone = lastcafe.get(sender);
			if(sender.equals("quota_atypique")) {
				b.sendMessage(channel, "Ok Quota!");
			}else if(lastone == null) {
				b.sendMessage(channel, "Ok pour le café!");
			}else {
				if(d.getTime()-lastone < MAX_CAFE*1000) {
					b.sendMessage(channel, "Eu... ok, mais, tu devrai plutôt prendre un thé, ça fait vraiment trop peu de temps la... ");
				}else {
					b.sendMessage(channel, "Ok, pas de problème, la dernière fois c'était le "+Main.DATE_FORMAT_OUT.format(new Date(lastone)));
				}
			}
			lastcafe.put(sender, d.getTime());
		}
			
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
