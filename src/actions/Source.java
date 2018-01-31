package actions;

import java.util.ArrayList;
import java.util.List;

import main.Bot;

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
	public void react(String channel, String sender, String login, String hostname, String message) {
		bot.sendMessage(channel, sender+": mes sources sont disponibles ici: https://code.ffdn.org/marmat8951/bot-irc2");
	}

}


