package actions;

import java.util.ArrayList;
import java.util.List;

import data.Message;
import main.Bot;

public class Help extends Action {

	public Help(Bot b) {
		super(b);
		List<String> kw = new ArrayList<>(3);
		kw.add("help");
		kw.add("?");
		this.keyWords = kw;
	}


	/**
	 * @deprecated remplacé par getListeCommandes
	 * @param l liste des actions possibles
	 * @param sender personne ayant envoyé le message de demande
	 * @param channel channel dans lequel la demande à été envoyé
	 */
	private void afficheListeCommandes(List<Action> l, String sender, String channel) {
		String listeCommandes="Voici la liste des commandes: ";
		for(Action a : l) {
			listeCommandes += CARACTERE_COMMANDE+a.keyWords.get(0)+" ";
		}
		bot.sendMessage(sender,channel, listeCommandes);
	}
	
	private String getListeCommandes(List<Action> l, String sender, String channel) {
		String listeCommandes="Voici la liste des commandes: ";
		for(Action a : l) {
			listeCommandes += CARACTERE_COMMANDE+a.keyWords.get(0)+" ";
		}
		return listeCommandes;
	}


	@Override
	public String help() {
		return "Utilisez "+CARACTERE_COMMANDE+"help <commande> Pour avoir les informations sur une commande.";
	}

	@Override
	@Deprecated
	public void react(String channel, String sender, String login, String hostname, Message message) {
		List<Action> l = Action.getAllActions(bot);
		boolean hasreacted = false;
		if(message.hasNoParameters()) {
			bot.sendMessage(sender,channel, help());
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
						bot.sendMessage(sender,channel, msg);
						hasreacted = true;
					}
				}
			}

			// Si il n'as pas encore réagi
			if(!hasreacted) {
				bot.sendMessage(sender,channel, "Commande inconnue.");
				afficheListeCommandes(l, sender, channel);
			}

		}
	}
	
	@Override
	public List<String> reactL(String channel, String sender, String login, String hostname, Message message) {
		List<String> res = new ArrayList<>();
		List<Action> l = Action.getAllActions(bot);
		boolean hasreacted = false;
		if(message.hasNoParameters()) {
			res.add(help());
			res.add(getListeCommandes(l, sender, channel));
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
						res.add (msg);
						hasreacted = true;
					}
				}
			}

			// Si il n'as pas encore réagi
			if(!hasreacted) {
				res.add("Commande inconnue.");
				res.add(getListeCommandes(l, sender, channel));
			}

		}
		return res;
	}


}
