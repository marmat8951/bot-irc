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
		if(keyWords.contains(messageSansEspace(s).substring(1))) {	//+contact seul
			bot.sendMessage(sender, messageSansEspace(s)+help());
		}else if(!EntierPositifNonVide.verifie(s)) {					// +contact suivi d'un mot
			Cache c = Cache.getInstance();
			ISP fai = c.getISPWithName(s);
			if(fai == null) {
				bot.sendMessage(sender,channel, "Aucun FAI "+s);
			}else {
				bot.sendMessages(sender, channel, fai.contact());
			}
		}

	}

	@Override
	public String help() {
		return " suivi du nom d'un fai. Renvoie les moyens pour contacter le FAI en question";
	}

	

}


