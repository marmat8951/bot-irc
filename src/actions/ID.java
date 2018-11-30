package actions;

import java.util.ArrayList;
import java.util.List;

import data.ISP;
import data.Message;
import main.IRCBot;
import main.Bot;
import main.Cache;
import verif_saisie.EntierPositifNonVide;

/**
 * Classe d'action servant à la récuperation de l'ID d'un FAI utilisé pour le stocker dans DB.FFDN
 * @author marmat
 */
public class ID extends Action {

	public ID(Bot b) {
		super(b);
		List<String> keywords = new ArrayList<>();
		keywords.add("id");
		this.keyWords=keywords;
	}

	private String erreur(String sender,String channel, String s) {
		return sender+": "+s;
	}

	@Override
	public String help() {
		return " Suivi du nom d'un FAI ou du numero d'un FAI. Renvoie l'Opposé de l'information donnée en paramètre. Exemple: \"+id 2\"   renvoie \"Ilico\"";
	}

	@Override
	@Deprecated
	public void react(String channel, String sender, String login, String hostname, Message message) {
		if(message.hasNoParameters()) {
			erreur(sender,channel,"Erreur de syntaxe: vous devez specifier un ID ou un nom");
		}else {
			Cache c = Cache.getInstance();
			String idString=message.getAllParametersAsOneString();
			if(EntierPositifNonVide.verifie(idString)) {
				int id = Integer.parseInt(idString);
				ISP fai = c.getISPWithID(id);
				if(fai!=null) {
					bot.sendMessage(sender, channel, "Le FAI "+id+" est: "+fai.getBetterName());
				}else {
					erreur(sender,sender,idString+" est un id null");
				}
			}else {
				ISP fai = c.getISPWithName(idString);
				if(fai!=null) {
					bot.sendMessage(sender,channel, "Le FAI "+fai.getBetterName()+" a pour ID: "+fai.getId());
					bot.sendMessage(sender,channel, "L'url dans db est https://db.ffdn.org/api/v1/isp/"+fai.getId()+"/");
				}else {
					erreur(sender,channel,idString+" ne correspond a aucun FAI");
				}

			}

		}
	}
	

	@Override
	public List<String> reactL(String channel, String sender, String login, String hostname, Message message) {
		List<String> res = new ArrayList<>();
		if(message.hasNoParameters()) {
			res.add(erreur(sender,channel,"Erreur de syntaxe: vous devez specifier un ID ou un nom"));
		}else {
			Cache c = Cache.getInstance();
			String idString=message.getAllParametersAsOneString();
			if(EntierPositifNonVide.verifie(idString)) {
				int id = Integer.parseInt(idString);
				ISP fai = c.getISPWithID(id);
				if(fai!=null) {
					res.add("Le FAI "+id+" est: "+fai.getBetterName());
				}else {
					res.add(erreur(sender,sender,idString+" est un id null"));
				}
			}else {
				ISP fai = c.getISPWithName(idString);
				if(fai!=null) {
					res.add("Le FAI "+fai.getBetterName()+" a pour ID: "+fai.getId());
					res.add("L'url dans db est https://db.ffdn.org/api/v1/isp/"+fai.getId()+"/");
				}else {
					res.add(erreur(sender,channel,idString+" ne correspond a aucun FAI"));
				}

			}

		}
		return res;
	}

}
