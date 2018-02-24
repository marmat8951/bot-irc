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
			boolean hasreacted = false;
			String commandeSansEspaces = message.replaceAll("\\s", "").substring(1); // On enleve les espaces et le +
			if(commandeSansEspaces.toLowerCase().equals("help")) {
				bot.sendMessage(channel, help());
				afficheListeCommandes(l, channel);
				hasreacted = true;
			}else {
				String commande = message.substring(message.indexOf(' ')+1);
				if(commande.indexOf(' ') != -1) {
					commande = commande.substring(0,commande.indexOf(' '));
				}
				commande = commande.replace("+", "");
				for(Action a : l) {
					if(a.keyWords.contains(commande) && hasreacted == false) {
						String msg = "";
						for(String s : a.keyWords) {
							msg+=Action.CARACTERE_COMMANDE+s+" ";
						}
						msg += a.help();
						bot.sendMessage(channel, msg);
						hasreacted = true;
					}
				}
			}
			
			// Si il n'as pas encore réagi
			if(!hasreacted) {
				bot.sendMessage(channel, "Commande inconnue.");
				afficheListeCommandes(l, channel);
			}

	}
	
	private void afficheListeCommandes(List<Action> l, String channel) {
		String listeCommandes="Voici la liste des commandes: ";
		for(Action a : l) {
			listeCommandes += CARACTERE_COMMANDE+a.keyWords.get(0)+" ";
		}
		bot.sendMessage(channel, listeCommandes);
	}

	
	@Override
	public String help() {
		return "Utilisez "+CARACTERE_COMMANDE+"help <commande> Pour avoir les informations sur une commande.";
	}
}
