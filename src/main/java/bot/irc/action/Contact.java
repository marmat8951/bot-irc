package bot.irc.action;

import java.util.ArrayList;
import java.util.List;

import bot.irc.data.ISP;
import bot.irc.data.Message;
import bot.irc.main.Bot;
import bot.irc.main.Cache;

public class Contact extends Action {

	public Contact(Bot b) {
		super(b);
		List<String> ar = new ArrayList<>();
		ar.add("contact");
		this.setKeyWords(ar);
	}


	@Override
	public String help() {
		return " suivi du nom d'un fai. Renvoie les moyens pour contacter le FAI en question";
	}

	@Override
	@Deprecated
	public void react(String channel, String sender, String login, String hostname, Message message) {

		if(message.hasNoParameters()) {
			bot.sendMessage(sender, channel, message.commandCharacterAndKeyword()+help());
		}else{
			Cache c = Cache.getInstance();
			String s = message.getAllParametersAsOneString();
			ISP fai = c.getISPWithName(s);
			if(fai == null) {
				bot.sendMessage(sender,channel, "Aucun FAI "+s);
			}else {
				bot.sendMessages(sender, channel, fai.contact());
			}
		}
		
	}


	@Override
	public List<String> reactL(String channel, String sender, String login, String hostname, Message message) {
		List<String> res = new ArrayList<>();
		if(message.hasNoParameters()) {
			res.add(message.commandCharacterAndKeyword()+help());
		}else{
			Cache c = Cache.getInstance();
			String s = message.getAllParametersAsOneString();
			ISP fai = c.getISPWithName(s);
			if(fai == null) {
				res.add("Aucun FAI "+s);
			}else {
				res.addAll(fai.contact());
			}
		}
		return res;
	}


}


