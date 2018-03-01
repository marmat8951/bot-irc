package comportement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import main.Bot;

public class Cafe extends Comportement {
	public static volatile Cafe instance = null;
	public Map<String, Boolean> rep = new HashMap<>();
	public Map<String, Long> lastcafe = new HashMap<>();
	public Map<String, Long> lastthe= new HashMap<>();
	public static final long MAX_THE = 30;
	public static final long MAX_CAFE = 360;
	
	public Cafe(Bot b) {
		super(b);
	}
	
	@Override
	public boolean hastoreact(String mesg) {
		String m=mesg.toLowerCase();
		return (m.contains(getBotNick()+" fait moi un café")||m.contains(getBotNick()+" fait moi un thé"));
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
					b.sendMessage(channel, "Ok, pas de problème. La dernière fois c'était le "+new Date(lastone).toString());
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
					b.sendMessage(channel, "Ok, pas de problème, la dernière fois c'était le "+new Date(lastone).toString());
				}
			}
			lastcafe.put(sender, d.getTime());
		}
			
	}
	//true = thé, false = café
	private boolean istheorcafe(String msg) {
		return msg.contains(getBotNick()+" fait moi un thé");	
	}
	
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
