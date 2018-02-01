package actions;

import java.util.ArrayList;
import java.util.List;

import main.Bot;

public class Help extends Action {

	public Help(Bot b) {
		super(b);
		List<String> kw = new ArrayList<>(3);
		kw.add("help");
		kw.add("?");
		this.keyWords = kw;
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, String message) {
		List<Action> l = Action.getAllActions((Bot) bot);
		String commande = message.substring(message.indexOf(' ')+1);
		if(commande.indexOf(' ') != -1) {
			commande = commande.substring(0,commande.indexOf(' '));
		}
			boolean hasreacted = false;
			if(commande.equals("help")) {
				bot.sendMessage(channel, help());
				hasreacted = true;
			}else {
				for(Action a : l) {
					if(a.keyWords.contains(commande) && hasreacted == false) {
						String msg = "";
						for(String s : a.keyWords) {
							msg+="+"+s+" ";
						}
						msg += a.help();
						bot.sendMessage(channel, msg);
						hasreacted = true;
					}
				}
			}
			
			// Si il n'as pas encore réagi
			if(!hasreacted) {
				bot.sendMessage(channel, "commande inconnue");
			}

	}

	@Override
	public String help() {
		return "Utilisez +help <commande> Pour avoir les informations sur une commande.";
	}
}
