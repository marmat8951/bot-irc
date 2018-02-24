package actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.Bot;
import main.Cache;

public class Reload extends Action {

	public Reload(Bot b) {
		super(b);
		List<String> ar = new ArrayList<>();
		ar.add("reload");
		this.keyWords = ar;
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, String message) {
		Date now = new Date();
		Date lastCU = Cache.getInstance().getLastCacheUpdate();
		if(lastCU.getTime() < now.getTime()-Cache.getTIME_BETWEEN_RELOADS() ) {		// Si la dernière MAJ date de + de 5 minutes
			bot.sendMessage(sender, "Je lance le reload!");
			if(reload()) {
				bot.sendMessage(sender, sender+": Le reload s'est bien passé.");
			}else {
				bot.sendMessage(sender, sender+": Erreur au moment du reload.");
			}
		}else {
			Date nextAllowed = new Date(lastCU.getTime()+Cache.getTIME_BETWEEN_RELOADS());
			bot.sendMessage(sender, "Trop de reload, attendez un peu. Le dernier à eu lieu le "+lastCU.toString()+" Prochain autorisé le "+nextAllowed);
		}
		
	}
	
	private boolean reload() {
		Cache c = Cache.getInstance();
		return c.reload();
	}

	@Override
	public String help() {
		return " Met à jour le cache";
	}

}
