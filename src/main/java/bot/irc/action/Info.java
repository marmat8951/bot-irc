package bot.irc.action;

import java.util.ArrayList;
import java.util.List;

import bot.irc.data.CoveredAreas;
import bot.irc.data.ISP;
import bot.irc.data.ISPDAO;
import bot.irc.data.Message;
import bot.irc.main.Bot;
import bot.irc.main.Cache;
import bot.irc.main.Config;
import bot.irc.verif_saisie.EntierPositifNonVide;
/**
 * Classe d'Action servant à gérer une demande d'info sur un FAI
 * @author marmat
 *
 */
public class Info extends Action {
	
	public static boolean INFO_ALL = Config.getPropertyAsBoolean("infoall", false);

	public Info(Bot b) {
		super(b);
		List<String> ar = new ArrayList<>();
		ar.add("info");
		this.setKeyWords(ar);
	}

	@Override
	public String help() {
		return " suivi du nom d'un FAI ou de son numero. Dans le cas d'un nom, il va le chercher dans le Cache. Dans le cas d'un numéro, il fait la requète directement dans db.ffdn.org. Exemples: \"+info 2\" ou \"+info fdn\". Pour les infos sur la fédé: \"+info ffdn\"";
	}

	@Override
	@Deprecated
	public void react(String channel, String sender, String login, String hostname, Message message) {
		
		if(message.hasNoParameters()) {
			bot.sendMessage(sender,channel,message.commandCharacterAndKeyword()+" doit être suivi d'une chaine de caractère ou d'un numero");
		}else {
			String s = message.getAllParametersAsOneString();
			ISPDAO idao = ISPDAO.getInstance();
			Bot ib = bot;

			if(!EntierPositifNonVide.verifie(s)) {			// Un mot après commande

				if(s.equalsIgnoreCase("all") && INFO_ALL) {	          			  // info all
					Cache c = Cache.getInstance();
					ib.sendMessages(sender,channel, c.toStringIRC());
					for(ISP i : c.getListe()) {
						if(i.isFFDNMember()) {
							ib.sendMessages(sender,channel, i.toStringIRC());
						}
					}

				}else if(s.equalsIgnoreCase("ffdn")) {				//info ffdn
					Cache c = Cache.getInstance();
					ib.sendMessages(sender, channel, c.toStringIRC());

				}else {
					Cache c = Cache.getInstance();
					ISP i = c.getISPWithName(s);
					if(i == null) {
						bot.sendMessage(sender, channel, "Recherche d'une zone "+s);
						ISP j = c.getISPWithGeoZone(s);
						if(j == null)
							bot.sendMessage(sender, channel, "Le FAI "+s+" est Inconnu, désolé. Et aucun FAI n'opère sur une sone dénomée "+s+" ...");
						else {
							bot.sendMessage(sender, channel, "Un FAI opère sur une zone correspondante : ");
							ib.sendMessages(sender, channel, j.toStringIRC());
							List<CoveredAreas> cas = j.getCoveredAreas(s);
							String technos = "Avec pour techno:";
							for(CoveredAreas ca: cas) {
								if(ca.getName().toLowerCase().contains(s.toLowerCase())) {
								technos+=ca.getTechnos()+" ";
								}
							}
							bot.sendMessage(sender, channel, technos);
						}
					}else {
						ib.sendMessages(sender,channel, i.toStringIRC());
					}
				}

			}else {											// Un nombre après +info
				int nbParameters = message.parameterSize();
				for(int i=0; i<nbParameters;i++) {
					try {
					int  id = message.getElementAsInt(i);
					List<String> strings = idao.getISP(id).toStringIRC();
						for(String response : strings) {
							bot.sendMessage(sender, channel, response);
						}
					}catch(NumberFormatException ne) {
						bot.sendMessage(sender, channel, message.getElementAsString(i)+" n'est pas un nombre");
					}
				}
			}
		}
		
	}

	@Override
	public List<String> reactL(String channel, String sender, String login, String hostname, Message message) {
		List<String> res = new ArrayList<>();
		if(message.hasNoParameters()) {
			res.add(message.commandCharacterAndKeyword()+" doit être suivi d'une chaine de caractère ou d'un numero");
		}else {
			String s = message.getAllParametersAsOneString();
			ISPDAO idao = ISPDAO.getInstance();

			if(!EntierPositifNonVide.verifie(s)) {			// Un mot après commande

				if(s.equalsIgnoreCase("all") && INFO_ALL) {	          			  // info all
					Cache c = Cache.getInstance();
					res.addAll(c.toStringIRC());
					for(ISP i : c.getListe()) {
						if(i.isFFDNMember()) {
							res.addAll(i.toStringIRC());
						}
					}

				}else if(s.equalsIgnoreCase("ffdn")) {				//info ffdn
					Cache c = Cache.getInstance();
					res.addAll(c.toStringIRC());

				}else {												//info FAI / Zone
					Cache c = Cache.getInstance();
					ISP i = c.getISPWithName(s);
					if(i == null) {
						res.add("Recherche d'une zone "+s);
						ISP j = c.getISPWithGeoZone(s);
						if(j == null)
							res.add("Le FAI "+s+" est Inconnu, désolé. Et aucun FAI n'opère sur une sone dénomée "+s+" ...");
						else {
							res.add("Un FAI opère sur une zone correspondante : ");
							res.addAll(j.toStringIRC());
							List<CoveredAreas> cas = j.getCoveredAreas(s);
							String technos = "Avec pour techno:";
							for(CoveredAreas ca: cas) {
								if(ca.getName().toLowerCase().contains(s.toLowerCase())) {
								technos+=ca.getTechnos()+" ";
								}
							}
							res.add(technos);
						}
					}else {
						res.addAll(i.toStringIRC());
					}
				}

			}else {												// Un nombre après info
				int nbParameters = message.parameterSize();
				for(int i=0; i<nbParameters;i++) {
					try {
					int id = message.getElementAsInt(i);
					res.addAll(idao.getISP(id).toStringIRC());
						
					}catch(NumberFormatException ne) {
						res.add(message.getElementAsString(i)+" n'est pas un nombre");
					}
				}
			}
		}
		return res;
		
	}

}


