package actions;

import java.util.ArrayList;
import java.util.List;

import data.CoveredAreas;
import data.ISP;
import data.ISPDAO;
import main.Bot;
import main.Cache;
import verif_saisie.EntierPositifNonVide;

public class Info extends Action {
	
	public static boolean INFO_ALL = false;

	public Info(Bot b) {
		super(b);
		List<String> ar = new ArrayList<>();
		ar.add("info");
		this.keyWords = ar;
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, String message) {
		String messageSansEspace = message.toLowerCase().replaceAll("\\s", "");
		if(messageSansEspace.equals(CARACTERE_COMMANDE+keyWords.get(0))) {
			bot.sendMessage(channel,messageSansEspace+" doit être suivi d'une chaine de caractère ou d'un numero");

		}else {


			String s = message.substring(message.indexOf(' ')+1);
			ISPDAO idao = ISPDAO.getInstance();
			Bot ib = (Bot)bot;


			if(!EntierPositifNonVide.verifie(s)) {			// Un mot après +info


				if(s.equalsIgnoreCase("all") && INFO_ALL) {	          			  // +info all
					Cache c = Cache.getInstance();
					ib.sendMessage(channel, c.toStringIRC());
					for(ISP i : c.getListe()) {
						if(i.isFFDNMember()) {
							ib.sendMessage(channel, i.toStringIRC());
						}
					}

				}else if(s.equalsIgnoreCase("ffdn")) {				//+info ffdn
					Cache c = Cache.getInstance();
					ib.sendMessage(channel, c.toStringIRC());

				}else {
					Cache c = Cache.getInstance();
					ISP i = c.getISPWithName(s);
					if(i == null) {
						bot.sendMessage(channel, "Recherche d'une zone "+s);
						ISP j = c.getISPWithGeoZone(s);
						if(j == null)
							bot.sendMessage(channel, "Le FAI "+s+" est Inconnu, désolé. Et aucun FAI n'opère sur une sone dénomée "+s+" ...");
						else {
							bot.sendMessage(channel, "Un FAI opère sur la zone "+s+" : ");
							ib.sendMessage(channel, j.toStringIRC());
							List<CoveredAreas> cas = j.getCoveredAreas(s);
							String technos = "";
							for(CoveredAreas ca: cas) {
								technos+=ca.getTechnos()+" ";
							}
							bot.sendMessage(channel, "Avec pour techno "+technos);
						}
					}else {
						ib.sendMessage(channel, i.toStringIRC());
					}
				}

			}else {											// Un nombre après +info

				int  id = Integer.parseInt(message.substring(message.indexOf(' ')+1));
				List<String> strings = idao.getISP(id).toStringIRC();
				for(String response : strings) {
					bot.sendMessage(channel,response);
				}
			}
		}

	}

	@Override
	public String help() {
		return " suivi du nom d'un FAI ou de son numero. Dans le cas d'un nom, il va le chercher dans le Cache. Dans le cas d'un numéro, il fait la requète directement dans db.ffdn.org. Exemples: \"+info 2\" ou \"+info fdn\". Pour les infos sur la fédé: \"+info ffdn\"";
	}

}


