package comportement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import actions.Action;
import main.IRCBot;
import main.Main;

/**
 * Singleton de comportement permettant au bot de faire de la philosophie
 * @author marmat
 *
 */
public class Philo extends Comportement {
	
	public static volatile Philo instance = null;
	public static final ArrayList<JSONObject> JSONS = new ArrayList<>();
	private final static String here = (new File(".")).getAbsolutePath();
	private final File folder=new File(here+File.separator+"ressources"+File.separator+"philo"+File.separator);
	private final String[] files = folder.list();
	private final Random random = new Random();
	
	private Philo(IRCBot b) {
		super(b);
		load();
	}
	
	public final static Philo getInstance(IRCBot b) {
		if (Philo.instance == null) {
			synchronized (Philo.class) {
				if(Philo.instance == null) {
						Philo.instance = new Philo(b);
				}
			}
		}
		return Philo.instance;
	}
	
	/**
	 * Parse les fichiers et les enregistre en mémoire.
	 */
	private void load() {
		if(Main.isDebug()) {
			System.out.println("Creation de la liste des citations de philo");
		}
		for(int i=0; i<files.length;++i) {
			File f = new File(folder.getAbsolutePath()+File.separator+files[i]);
			if(f.isFile() && f.canRead()) {
				try {
					String s = FileUtils.readFileToString(f, "UTF-8");
					JSONS.add(new JSONObject(s));
				}catch (IOException e) {
					e.printStackTrace();
				}
			
			}else {
				System.err.println("Attention, dans les ressources de philo, la ressource "+folder.getAbsolutePath()+File.separator+files[i]+" n'est pas un fichier lisible.");
			}
		}
	}

	@Override
	public boolean hastoreact(String message) {
		if(message.toLowerCase().contains(this.getBotNick())) {
			return getJSONObjectForMessage(message)!=null;
		}else if(message.toLowerCase().replaceAll(" ", "").equals(Action.CARACTERE_COMMANDE+"philo")) { //TODO eviter cette exception pour déclancher l'évenement comme une action
			return true;
		}else {
			return false;
		}
		
	}

	/**
	 * Cherche un objet json de philosophie dont le topic correspond au message
	 * @param message message envoyé
	 * @return Objet contenant les messages si il existe une thématique, null sinon.
	 */
	private JSONObject getJSONObjectForMessage(String message) {
		for(JSONObject json : JSONS) {
			String topic = json.getString("topic");
			if(message.toLowerCase().contains(topic)) return json;
		}
		return null;
	}
	
	/**
	 * Choisi au hasard une citation dans la liste correspondant de l'objet
	 * @param jo Objet contenant la liste de phrases
	 * @return Citation mise en forme
	 */
	private String giveMeAQuoteFrom(JSONObject jo) {
		JSONArray array = jo.getJSONArray("quotes");
		int nbrCit = array.length();
		int choix = random.nextInt(nbrCit);
		JSONObject quote  = array.getJSONObject(choix);
		final String UNKNOWN = "Inconnu";
		String author="";
		try {
			author = quote.getString("author");
		}catch(JSONException e) {
			author = UNKNOWN;
		}
		if(author == null) {
			author = UNKNOWN;
		}
		return "\""+quote.getString("quote")+"\" par : "+author;
	}
	
	@Override
	public void react(String channel, String sender, String login, String hostname, String message) {
		IRCBot b = this.getBot();
		String res = ""+sender;
		JSONObject jo;
		if(message.toLowerCase().replaceAll(" ", "").equals(Action.CARACTERE_COMMANDE+"philo")) {
			int size = JSONS.size();
			int choix = random.nextInt(size);
			jo = JSONS.get(choix);
			res += ": Voici une citation à propos de "+jo.getString("topic");
			
		}else {
			res += ", je connais de la philo a propos de ";
			jo = getJSONObjectForMessage(message);
			res += jo.getString("topic")+":";
			
		}
		b.sendMessage(channel, res);
		b.sendMessage(channel, giveMeAQuoteFrom(jo));
	}

}
