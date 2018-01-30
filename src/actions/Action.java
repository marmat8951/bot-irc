package actions;

import java.util.ArrayList;
import java.util.List;

import org.jibble.pircbot.PircBot;

import main.Bot;

public abstract class Action {

	public List<String> keyWords;
	public PircBot bot;
	
	protected Action(Bot b, List<String> keywords) {
		this.keyWords = keywords;
		this.bot = b;
	}
	
	protected Action(Bot b) {
		this.bot = b;
	}
	
	public abstract void react(String channel, String sender,
			String login, String hostname, String message);
	
	
	public boolean hasToReact(String s) {
		if(s.charAt(0)!=Bot.CARACTERE_COMMANDE) {
			return false;								// On ne réagit pas si ce n'est pas une commande. Cela évite la suite du traitement.
		}
		s=s.substring(1); // on enleve le caractère de commande
		
		int premierEspace = s.indexOf(' ');

		if(premierEspace>0) { // Si il y a un espace il ne faut prendre que le premier mot.
			s=s.substring(0, premierEspace);
			
		}
		String chaineLowerCase =  s.toLowerCase();
		for(String kw : keyWords) {
			if(chaineLowerCase.contains(kw.toLowerCase())){
				return true;
			}
		}
		return false;
		
	}
	
	public static List<Action> getAllActions(Bot b){
		List<Action> ar= new ArrayList<>();
		ar.add(new Contact(b));
		ar.add(new Info(b));
		ar.add(new Liste(b));
		ar.add(new Source(b));
		return ar;
	}
	

}