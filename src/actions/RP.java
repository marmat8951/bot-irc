package actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.Bot;
import main.Main;
import main.PropertiesSetter;

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
		rpFile = new File(PropertiesSetter.here+File.separator+"ressources"+File.separator+"rp"+File.separator+"index.txt");
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
			
		}else {
			if(Main.isDebug()) {
				System.out.println("Le fichier de RP "+rpFile.getAbsolutePath()+"existe");
			}
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(rpFile, "UTF-8");
		if(!fileExists) {
				writer.println("\n");
		}
		}catch (IOException e) {
			System.err.println("Erreur lors de la création du fileWriter du ficher RP:");
			e.printStackTrace();
		}finally {
			writer.close();
		}
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, String message) {
		Date now = new Date();
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(rpFile, "UTF-8");
		} catch (FileNotFoundException e) {
			bot.sendMessageToAdmins("Erreur: Le fichier rp n'existe pas, impossible d'écrire");
			bot.sendMessageToAdmins(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		writer.println(Main.DATE_FORMAT_OUT.format(now)+" "+message.substring(message.indexOf(" "))+"\n");
		bot.sendMessage(sender, channel, "Ajout a la RP réussi!");
		writer.close();
	}
	

	@Override
	public String help() {
		return "Concatène l'URL indiquée dans un fichier pour les revue de presse";
	}

}
