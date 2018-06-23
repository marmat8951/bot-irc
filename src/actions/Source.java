package actions;

import java.util.ArrayList;
import java.util.List;

import data.Message;
import main.Bot;

/**
 * Indique simplement où on peut trouver le code source
 * @author marmat
 *
 */
public class Source extends Action {

	public Source(Bot b) {
		super(b);
		List<String> ar = new ArrayList<>();
		ar.add("source");
		ar.add("sources");
		ar.add("code");
		this.keyWords = ar;
	}

	@Override
	public String help() {
		return " Renvoie une URI où on peut trouver les sources";
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, Message message) {
		react(channel, sender);
	}
	
	private void react(String channel, String sender) {
		bot.sendMessage(sender, channel, sender+": mes sources sont disponibles ici: https://code.ffdn.org/marmat8951/bot-irc2");
	}
}


