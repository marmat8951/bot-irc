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

	public Info(Bot b) {
		this.bot = b;
		List<String> ar = new ArrayList<>();
		ar.add("info");
		this.keyWords = ar;
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, String message) {
		String s = message.substring(message.indexOf(' ')+1);
		ISPDAO idao = ISPDAO.getInstance();
		if(!EntierPositifNonVide.verifie(s)) {			// Un mot après +info


			if(s.equalsIgnoreCase("all")) {	          			  // +info all
				Cache c = Cache.getInstance();
				bot.sendMessage(channel, c.toStringIRC());
				for(ISP i : c.getListe()) {
					if(i.isFFDNMember()) {
						bot.sendMessage(channel, i.toStringIRC());
					}
				}

			}else if(s.equalsIgnoreCase("ffdn")) {				//+info ffdn
				Cache c = Cache.getInstance();
				bot.sendMessage(channel, c.toStringIRC());

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
						bot.sendMessage(channel, j.toStringIRC());
						List<CoveredAreas> cas = j.getCoveredAreas(s);
						String technos = "";
						for(CoveredAreas ca: cas) {
							technos+=ca.getTechnos()+" ";
						}
						bot.sendMessage(channel, "Avec pour techno "+technos);
					}
				}else {
					bot.sendMessage(channel, i.toStringIRC());
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


