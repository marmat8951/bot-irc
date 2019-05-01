package bot.irc.action;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bot.irc.data.Message;
import bot.irc.main.Bot;
import bot.irc.main.Config;
import bot.irc.main.Main;

/**
 * Méthode pour gérer la sauvegarde de liens dans un fichier pour en faire une revue de presse.
 * @author marmat
 *
 */

public class RP extends Action {

	public File rpFile;

	/**
	 * Crée le fichier si il n'existe pas et prépare la possibilité d'utiliser l'action rp.
	 * @param b bot b
	 */
	protected RP(Bot b) {
		super(b);
		List<String> ar = new ArrayList<>();
		ar.add("rp");
		ar.add("citation");
		this.keyWords = ar;
		rpFile = new File(Config.here+File.separator+"ressources"+File.separator+"rp"+File.separator+"index.txt");
		boolean fileExists = rpFile.exists();
		if(!fileExists) {
			if(Main.isDebug()) {
				System.out.println("Le fichier de RP "+rpFile.getAbsolutePath()+" n'existe pas, je le crée");
			}
			try {
				if(!rpFile.createNewFile()) {
					throw new Exception("impossible de creer le fichier");
				}
			} catch (Exception e) {
				System.err.println("Erreur a la création du fichier de RP :");
				e.printStackTrace();
			}
		}else if(Main.isDebug()) {
			System.out.println("Le fichier de RP "+rpFile.getAbsolutePath()+"existe");
		}
	}

	@Override
	public String help() {
		return "Concatène l'URL indiquée dans un fichier pour les revue de presse";
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, Message message) {
		Date now = new Date();
		FileWriter writer = null;
		try {		
			writer = new FileWriter(rpFile, true);

		} catch (IOException e) {
			bot.sendMessageToAdmins("Erreur: Le fichier rp n'existe pas, impossible d'écrire");
			bot.sendMessageToAdmins(e.getLocalizedMessage());
			e.printStackTrace();
		}
		try {
			writer.write(Main.DATE_FORMAT_OUT.format(now)+": "+message.getAllParametersAsOneString()+"\r\n");
			writer.close();
			bot.sendMessage(sender, channel, "Ajout a la RP réussi!");
		} catch (IOException e) {
			bot.sendMessage(sender, channel, "Erreur lors de l'ajout de la RP");
			e.printStackTrace();
		}

	}

	@Override
	public List<String> reactL(String channel, String sender, String login, String hostname, Message message) {
		List<String> res = new ArrayList<>();
		Date now = new Date();
		FileWriter writer = null;
		try {
			writer = new FileWriter(rpFile, true);
		} catch (IOException e) {
			String s = "Erreur: Le fichier rp n'existe pas, impossible d'écrire";
			System.err.println(s);
			res.add(s);
			e.printStackTrace();
		}
		try {
			writer.write(Main.DATE_FORMAT_OUT.format(now)+": "+message.getAllParametersAsOneString()+"\r\n");
			writer.flush();
			res.add("Ajout à la RP réussi!");
			writer.close();
		}catch(IOException ioe) {
			res.add(ioe.getLocalizedMessage());
		}
		return res;
	}

}
