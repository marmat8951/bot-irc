package actions;

import java.util.ArrayList;
import java.util.List;

import data.ISP;
import main.Bot;
import main.Cache;
import verif_saisie.EntierPositifNonVide;

public class Contact extends Action {

	public Contact(Bot b) {
		super(b);
		List<String> ar = new ArrayList<>();
		ar.add("contact");
		this.keyWords = ar;
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, String message) {
		
		String s = message.substring(message.indexOf(' ')+1);
		if(!EntierPositifNonVide.verifie(s)) {					// +contact suivi d'un mot
			Cache c = Cache.getInstance();
			ISP fai = c.getISPWithName(s);
			if(fai == null) {
				bot.sendMessage(channel, "Aucun FAI "+s);
			}else {
				Bot b = (Bot) bot;
				b.sendMessage(channel, fai.contact());
			}
		}

	}

}


