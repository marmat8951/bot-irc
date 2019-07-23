package bot.irc.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bot.irc.data.Message;
import bot.irc.main.Bot;
import bot.irc.main.Cache;
import bot.irc.main.Main;

/**
 * Classe servant à l'action de forcer une mise à jour des informations
 * @author marmat
 *
 */
public class Reload extends Action {

	public Reload(Bot b) {
		super(b);
		List<String> ar = new ArrayList<>();
		ar.add("reload");
		this.setKeyWords(ar);
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
	@Deprecated
	public void react(String channel, String sender, String login, String hostname, Message message) {
		Date now = new Date();
		Date lastCU = Cache.getInstance().getLastCacheUpdate();
		if(lastCU.getTime() < now.getTime()-Cache.getTIME_BETWEEN_RELOADS() ) {		// Si la dernière MAJ date de + de 5 minutes
			bot.sendMessage(sender,channel, "Je lance le reload!");
			if(reload()) {
				bot.sendMessage(sender, channel, sender+": Le reload s'est bien passé.");
			}else {
				bot.sendMessage(sender, channel, sender+": Erreur au moment du reload.");
			}
		}else {
			Date nextAllowed = new Date(lastCU.getTime()+Cache.getTIME_BETWEEN_RELOADS());
			bot.sendMessage(sender, channel, "Trop de reload, attendez un peu. Le dernier à eu lieu le "+lastCU.toString()+" Prochain autorisé le "+nextAllowed);
		}
		
	}

	@Override
	public List<String> reactL(String channel, String sender, String login, String hostname, Message message) {
		List<String> res = new ArrayList<>();
		Date now = new Date();
		Date lastCU = Cache.getInstance().getLastCacheUpdate();
		if(lastCU.getTime() < now.getTime()-Cache.getTIME_BETWEEN_RELOADS() ) {		// Si la dernière MAJ date de + de 5 minutes
			if(reload()) {
				res.add(sender+": Le reload s'est bien passé.");
			}else {
				res.add(sender+": Erreur au moment du reload.");
			}
		}else {
			Date nextAllowed = new Date(lastCU.getTime()+Cache.getTIME_BETWEEN_RELOADS());
			res.add("Trop de reload, attendez un peu. Le dernier à eu lieu le "+Main.DATE_FORMAT_OUT.format(lastCU.toString())+" Prochain autorisé le "+Main.DATE_FORMAT_OUT.format(nextAllowed));
		}
		return res;
	}
}
