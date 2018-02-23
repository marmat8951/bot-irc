package actions;

import java.util.ArrayList;
import java.util.List;

import data.ISP;
import main.Bot;
import main.Cache;
import verif_saisie.EntierPositifNonVide;

public class ID extends Action {

	

	public ID(Bot b) {
		super(b);
		List<String> keywords = new ArrayList<>();
		keywords.add("id");
		this.keyWords=keywords;
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, String message) {
		Cache c = Cache.getInstance();
		String idString=null;
		try {
		idString=message.substring(message.indexOf(' ')+1);
		}catch(IndexOutOfBoundsException ioe) {
			erreur(channel,sender,"Erreur de syntaxe: vous devez specifier un ID ou un nom");
		}
		if(EntierPositifNonVide.verifie(idString)) {
			int id = Integer.parseInt(idString);
			ISP fai = c.getISPWithID(id);
			if(fai!=null) {
			bot.sendMessage(channel, "Le FAI "+id+" est: "+fai.getBetterName());
			
			}else {
				erreur(channel,sender,idString+" est un id null");
			}
		}else {
			ISP fai = c.getISPWithName(idString);
			if(fai!=null) {
				bot.sendMessage(channel, "Le FAI "+fai.getBetterName()+" a pour ID: "+fai.getId());
				bot.sendMessage(channel, "L'url dans db est https://db.ffdn.org/api/v1/isp/");
			}else {
				erreur(channel,sender,idString+" ne correspond a aucun FAI");
			}
			
		}
		

	}
	
	private void erreur(String channel, String sender, String s) {
		bot.sendMessage(channel, sender+": "+s);
	}

	@Override
	public String help() {
		return " Suivi du nom d'un FAI ou du numero d'un FAI. Renvoie l'Opposé de l'information donnée en paramètre. Exemple: \"+id 2\"   renvoie \"Ilico\"";
	}

}
