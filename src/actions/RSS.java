package actions;

import java.util.ArrayList;
import java.util.List;

import data.Message;
import main.IRCBot;
import rss.RssData;
import rss.RssDataRemainder;

/**
 * gère les actions liées au RSS
 * @author marmat
 *
 */

public class RSS extends Action {

	public RSS(IRCBot b) {
		super(b);
		List<String> kw = new ArrayList<>();
		kw.add("rss");
		kw.add("planet");
		kw.add("flux");
		this.keyWords=kw;
	}

	@Override
	public String help() {
		return " Permet l'affichage des article RSS. Si vous ajoutez un nombre, affiche l'article du numero correspondant.";
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, Message message) {
		RssDataRemainder remainder = iRCBot.getRssdata();
		if(message.hasNoParameters()) {
			iRCBot.sendMessages(sender, channel, remainder.toStringIRC());
		}
		int nbParams = message.parameterSize();
		try {
			for(int i = 0; i<nbParams;++i) {
				int id = message.getElementAsInt(i);
				RssData data = remainder.getDataWithId(id);
				if(data != null) {
					iRCBot.sendMessages(sender, channel, data);
				}else {
					iRCBot.sendMessage(sender, channel, "erreur: le nombre "+id+" n'est pas correct, ce dernier doit être entre 0 et "+(remainder.getCompletion()-1));
				}
				if(i!=nbParams-1) {
					iRCBot.sendMessage(sender, channel, "-------");
				}
			}
		}catch (NumberFormatException e) {
			iRCBot.sendMessage(sender, channel, "erreur: Vous devez utiliser des entiers en paramètres, et ces derniers doivent être entre 0 et "+(remainder.getCompletion()-1));
		}
	}

}
