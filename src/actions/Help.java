package actions;

import java.util.ArrayList;
import java.util.List;

import data.Message;
import main.IRCBot;

public class Help extends Action {

	public Help(IRCBot b) {
		super(b);
		List<String> kw = new ArrayList<>(3);
		kw.add("help");
		kw.add("?");
		this.keyWords = kw;
	}


	/**
	 * 
	 * @param l liste des actions possibles
	 * @param sender personne ayant envoyé le message de demande
	 * @param channel channel dans lequel la demande à été envoyé
	 */
	private void afficheListeCommandes(List<Action> l, String sender, String channel) {
		String listeCommandes="Voici la liste des commandes: ";
		for(Action a : l) {
			listeCommandes += CARACTERE_COMMANDE+a.keyWords.get(0)+" ";
		}
		iRCBot.sendMessage(sender,channel, listeCommandes);
	}


	@Override
	public String help() {
		return "Utilisez "+CARACTERE_COMMANDE+"help <commande> Pour avoir les informations sur une commande.";
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, Message message) {
		List<Action> l = Action.getAllActions((IRCBot) iRCBot);
		boolean hasreacted = false;
		if(message.hasNoParameters()) {
			iRCBot.sendMessage(sender,channel, help());
			afficheListeCommandes(l, sender, channel);
			hasreacted = true;
		}else {
			int nbParameters = message.parameterSize();
			for(int i=0; i<nbParameters; ++i) {
				String commande = message.getElementAsString(i);
				commande = commande.replaceAll(""+Action.CARACTERE_COMMANDE, "");
				for(Action a : l) {
					if(a.keyWords.contains(commande)) {
						String msg = "";
						for(String s : a.keyWords) {
							msg+=Action.CARACTERE_COMMANDE+s+" ";
						}
						msg += a.help();
						iRCBot.sendMessage(sender,channel, msg);
						hasreacted = true;
					}
				}
			}

			// Si il n'as pas encore réagi
			if(!hasreacted) {
				iRCBot.sendMessage(sender,channel, "Commande inconnue.");
				afficheListeCommandes(l, sender, channel);
			}

		}
	}
}
