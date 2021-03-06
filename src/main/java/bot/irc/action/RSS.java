package bot.irc.action;

import java.util.ArrayList;
import java.util.List;

import bot.irc.data.Message;
import bot.irc.main.Bot;
import bot.irc.main.Main;
import bot.irc.rss.RssData;
import bot.irc.rss.RssDataRemainder;

/**
 * gère les actions liées au RSS
 * @author marmat
 *
 */

public class RSS extends Action {

	public RSS(Bot b) {
		super(b);
		List<String> kw = new ArrayList<>();
		kw.add("rss");
		kw.add("planet");
		kw.add("flux");
		this.setKeyWords(kw);
	}

	@Override
	public String help() {
		return " Permet l'affichage des article RSS. Si vous ajoutez un nombre, affiche l'article du numero correspondant.";
	}

	@Override
	@Deprecated
	public void react(String channel, String sender, String login, String hostname, Message message) {
		RssDataRemainder remainder = Main.getRSS_DATA_REMAINDER();
		if(message.hasNoParameters()) {
			bot.sendMessages(sender, channel, remainder.toStringIRC());
		}
		int nbParams = message.parameterSize();
		try {
			for(int i = 0; i<nbParams;++i) {
				int id = message.getElementAsInt(i);
				RssData data = remainder.getDataWithId(id);
				if(data != null) {
					bot.sendMessages(sender, channel, data.toStringIRC());
				}else {
					bot.sendMessage(sender, channel, "erreur: le nombre "+id+" n'est pas correct, ce dernier doit être entre 0 et "+(remainder.getCompletion()-1));
				}
				if(i!=nbParams-1) {
					bot.sendMessage(sender, channel, "-------");
				}
			}
		}catch (NumberFormatException e) {
			bot.sendMessage(sender, channel, "erreur: Vous devez utiliser des entiers en paramètres, et ces derniers doivent être entre 0 et "+(remainder.getCompletion()-1));
		}
	}

	@Override
	public List<String> reactL(String channel, String sender, String login, String hostname, Message message) {
		List<String> s = new ArrayList<>();
		RssDataRemainder remainder = Main.getRSS_DATA_REMAINDER();
		if(message.hasNoParameters()) {
			bot.sendMessages(sender, channel, remainder.toStringIRC());
		}
		int nbParams = message.parameterSize();
		try {
			for(int i = 0; i<nbParams;++i) {
				int id = message.getElementAsInt(i);
				RssData data = remainder.getDataWithId(id);
				if(data != null) {
					s.add(data.toString());
				}else {
					s.add("erreur: le nombre "+id+" n'est pas correct, ce dernier doit être entre 0 et "+(remainder.getCompletion()-1));
				}
				if(i!=nbParams-1) {
					s.add("-------");
				}
			}
		}catch (NumberFormatException e) {
			s.add("erreur: Vous devez utiliser des entiers en paramètres, et ces derniers doivent être entre 0 et "+(remainder.getCompletion()-1));
			return s;
		}finally {
			
		}
		return s;
	}

}
