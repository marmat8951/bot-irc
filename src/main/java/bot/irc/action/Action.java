package bot.irc.action;

import java.util.ArrayList;
import java.util.List;

import bot.irc.data.Message;
import bot.irc.main.Bot;
import bot.irc.main.Config;

public abstract class Action {

	public List<String> keyWords;
	public Bot bot;
	public volatile static char CARACTERE_COMMANDE = Config.getProperty("Caractere_commande").charAt(0);
	
	protected Action(Bot b, List<String> keywords) {
		this.keyWords = keywords;
		this.bot = b;
	}
	
	protected Action(Bot b) {
		this.bot = b;
	}
	
	protected Action(Bot b, String...keywords) {
		this.bot = b;
		List<String> ar = new ArrayList<>(keywords.length); 
		for(int i=0;i<keywords.length;++i) {
			ar.add(keywords[i]);
		}
		this.keyWords=ar;
	}
	
	/**
	 * @deprecated
	 * @param channel
	 * @param sender
	 * @param login
	 * @param hostname
	 * @param message
	 */
	public abstract void react(String channel, String sender,
			String login, String hostname, Message message);
	/**
	 * 
	 * Methode réagissant au message @see ;
	 * @param channel channer sur l'IRC
	 * @param sender personne ayant envoyé le message
	 * @param login	login du bot
	 * @param hostname hostname actuell
	 * @param message message en question
	 */
	public abstract List<String> reactL(String channel, String sender,
			String login, String hostname, Message message);
	
	public void reactUsingMessage(String channel,String sender,
			String login, String hostname, String message) {
		Message m = new Message(message);
		react(channel, sender, login, hostname, m);
	}
	/**
	 * Indique si oui ou non cette action doit être executée
	 * @param s message envoyé
	 * @return true si l'action contenue doit être executée, false sinon.
	 * @deprecated
	 */
	public boolean hasToReact(String s) {
		if(s.charAt(0)!=CARACTERE_COMMANDE) {
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
	
	/**
	 * Indique si oui ou non cette action doit être executée
	 * @param s message envoyé
	 * @return true si l'action contenue doit être executée, false sinon.
	 */
	public boolean hasToReact(Message m) {
		if(m.getC()!=CARACTERE_COMMANDE) {
			return false;								// On ne réagit pas si ce n'est pas une commande. Cela évite la suite du traitement.
		}
		for(String kw : keyWords) {
			if(m.getCommande().equalsIgnoreCase(kw)){
				return true;
			}
		}
		return false;		
	}
	
	/**
	 * Renvoie toutes les actions possibles prêtes à utiliser le Bot pour s'executer.
	 * @param b Bot que nous utiliserons pour nos actions
	 * @return Liste d'actions prete à être utilisée dans un forEach verifiant si elle doivent être executées.
	 */
	public static List<Action> getAllActions(Bot b){
		List<Action> ar= new ArrayList<>();
		ar.add(new Help(b));
		ar.add(new Contact(b));
		ar.add(new Info(b));
		ar.add(new Liste(b));
		ar.add(new Source(b));
		ar.add(new Reload(b));
		ar.add(new ID(b));
		ar.add(new Distance(b));
		ar.add(new RSS(b));
		ar.add(new RP(b));
		return ar;
	}
	
	/**
	 * Renvoie le message help de la fonction. Ce message sert lors de l'utilisation de +help *nom de commande*
	 * @return chaine help de la commande
	 */
	public abstract String help();
	
	/**
	 * modifie la chaine pour supprimer les espaces
	 * @deprecated
	 * @param s chaine de caracteres
	 * @return renvoie la chaine mise en lowercase et sans espaces
	 */
	public static String messageSansEspace(String s) {
		return s.toLowerCase().replaceAll("\\s", "");
	}
	
}
