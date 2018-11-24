package actions;

import java.util.ArrayList;
import java.util.List;

import data.ISP;
import data.Message;
import main.IRCBot;
import main.Cache;
import verif_saisie.EntierPositifNonVide;

public class Contact extends Action {

	public Contact(IRCBot b) {
		super(b);
		List<String> ar = new ArrayList<>();
		ar.add("contact");
		this.keyWords = ar;
	}


	@Override
	public String help() {
		return " suivi du nom d'un fai. Renvoie les moyens pour contacter le FAI en question";
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, Message message) {

		if(message.hasNoParameters()) {
			iRCBot.sendMessage(sender, message.commandCharacterAndKeyword()+help());
		}else{
			Cache c = Cache.getInstance();
			String s = message.getAllParametersAsOneString();
			ISP fai = c.getISPWithName(s);
			if(fai == null) {
				iRCBot.sendMessage(sender,channel, "Aucun FAI "+s);
			}else {
				iRCBot.sendMessages(sender, channel, fai.contact());
			}
		}
		
	}

	

}


