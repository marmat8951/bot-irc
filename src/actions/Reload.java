package actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import data.Message;
import main.IRCBot;
import main.Cache;

/**
 * Classe servant à l'action de forcer une mise à jour des informations
 * @author marmat
 *
 */
public class Reload extends Action {

	public Reload(IRCBot b) {
		super(b);
		List<String> ar = new ArrayList<>();
		ar.add("reload");
		this.keyWords = ar;
	}
	
	private boolean reload() {
		Cache c = Cache.getInstance();
		return c.reload();
	}

	@Override
	public String help() {
		return " Met à jour le cache";
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, Message message) {
		Date now = new Date();
		Date lastCU = Cache.getInstance().getLastCacheUpdate();
		if(lastCU.getTime() < now.getTime()-Cache.getTIME_BETWEEN_RELOADS() ) {		// Si la dernière MAJ date de + de 5 minutes
			iRCBot.sendMessage(sender,channel, "Je lance le reload!");
			if(reload()) {
				iRCBot.sendMessage(sender, channel, sender+": Le reload s'est bien passé.");
			}else {
				iRCBot.sendMessage(sender, channel, sender+": Erreur au moment du reload.");
			}
		}else {
			Date nextAllowed = new Date(lastCU.getTime()+Cache.getTIME_BETWEEN_RELOADS());
			iRCBot.sendMessage(sender, channel, "Trop de reload, attendez un peu. Le dernier à eu lieu le "+lastCU.toString()+" Prochain autorisé le "+nextAllowed);
		}
		
	}

}
