package bot.irc.action;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bot.irc.data.ISP;
import bot.irc.data.ISPDAO;
import bot.irc.data.Message;
import bot.irc.main.AffichableSurIRC;
import bot.irc.main.Bot;
import bot.irc.main.Cache;
import bot.irc.main.Config;

/**
 * Classe servant à la récuperation d'une liste des FAI présents dans la base de donnée.
 * @author marmat
 *
 */
public class Liste extends Action {
	
	public static volatile boolean allAllowed=Config.getPropertyAsBoolean("listeall",true);

	public Liste(Bot b) {
		super(b);
		List<String> ar = new ArrayList<>();
		ar.add("liste");
		ar.add("list");
		this.setKeyWords(ar);
	}

	@Override
	public String help() {
		return " Liste tous les FAI de la fédération. L'Ajout du parametre All affiche aussi ceux hors fédération.";
	}

	
	@Override
	@Deprecated
	public void react(String channel, String sender, String login, String hostname, Message message) {
		bot.sendMessages(sender,channel, reactL(channel, sender, login, hostname, message));
		
	}

	@Override
	public List<String> reactL(String channel, String sender, String login, String hostname, Message message) {
		ISPDAO idao = ISPDAO.getInstance();
		Cache c = Cache.getInstance();
		List<ISP> listeFAI=null;
		try {
			listeFAI = c.getListe();
		}catch (Exception e) {
			try {
				listeFAI = idao.getISPs();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		List<String> messages = new LinkedList<>();
		messages.add("Les FAI surveillés par mes petits yeux mignons de bot sont:");
		String s="";

		if(allAllowed && message.parametersContains("all")) {
			messages.add("=== Hors fédé: ===");
			for(ISP isp: listeFAI) {
				if(!isp.isFFDNMember()) {
					s+= isp.getBetterName()+", ";
				}
				if(s.length()>=AffichableSurIRC.MAX_CHARACTERS) {
					messages.add(s);
					s="";
				}
			}
			messages.add(s);
			s="";
		}
		messages.add("=== Dans la fédé: ===");
		for(ISP isp: listeFAI ) {
			if(isp.isFFDNMember()) {
				s+= isp.getBetterName();
				if(s.length()>=AffichableSurIRC.MAX_CHARACTERS) {
					messages.add(s);
					s="";
				}else {
					s+=", ";
				}
			}
		}
		messages.add(s);
		return messages;
	}

}


