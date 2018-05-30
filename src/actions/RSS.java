package actions;

import java.util.ArrayList;
import java.util.List;

import main.Bot;
import rss.RssDataRemainder;

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
		this.keyWords=kw;
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, String message) {
		RssDataRemainder remainder = bot.getRssdata();
		if(message.indexOf(' ')==-1 || message.substring(message.indexOf(' ')).replaceAll(" ", "").equals("")) {
			bot.sendMessages(sender, channel, remainder.toStringIRC());
		}else {
			String contenu = message.substring(message.indexOf(' ')+1);
			try {
				int id = Integer.parseInt(contenu);
				if(id<0 || id>=remainder.getCompletion()) {
					throw new IllegalArgumentException();
				}
				bot.sendMessages(sender, channel, remainder.getDataWithId(id));
			}catch ( NullPointerException | IllegalArgumentException e) {
				bot.sendMessage(sender, channel, "erreur: Vous devez utiliser un nombre, et ce dernier doit être entre 0 et "+(remainder.getCompletion()-1));
			}
		}
	}

	@Override
	public String help() {
		return " Permet l'affichage des article RSS. Si vous ajoutez un nombre, affiche l'article du numero correspondant.";
	}

}
