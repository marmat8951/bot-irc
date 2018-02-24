package actions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import data.ISP;
import data.ISPDAO;
import main.AffichableSurIRC;
import main.Bot;
import main.Cache;

public class Liste extends Action {
	
	public static volatile boolean allAllowed=true;

	public Liste(Bot b) {
		super(b);
		List<String> ar = new ArrayList<>();
		ar.add("liste");
		ar.add("list");
		this.keyWords = ar;
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, String message) {
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

		if(allAllowed && message.indexOf(' ')!=-1 && message.substring(message.indexOf(" ")+1).equalsIgnoreCase("all")) {
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
		Bot b2 = (Bot) bot;
		b2.sendMessage(sender, messages);
	}

	@Override
	public String help() {
		return " Liste tous les FAI de la fédération. L'Ajout du parametre All affiche aussi ceux hors fédération.";
	}

}


