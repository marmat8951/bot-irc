package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import actions.Action;
import actions.Info;
import actions.Liste;
import verif_saisie.EntierPositifNonVide;


public class PropertiesSetter {

	private String f;
	public final static String DEFAULT_PROPERTIES_FILE = "default.properties";
	public final static String here = (new File(".")).getAbsolutePath();
	
	public PropertiesSetter(String configFile) {
		f = configFile;
	}
	
	public void setFile(String configFile) {
		f=configFile;
	}

	public boolean setPropertiesOn(CacheReloader cr, Bot b) throws IOException,NumberFormatException {
		System.out.println(here);
		System.out.println(here+File.separator+"ressources"+File.separator+"config"+File.separator+DEFAULT_PROPERTIES_FILE);
		FileReader defaultProperties = new FileReader(new File(here+File.separator+"ressources"+File.separator+"config"+File.separator+DEFAULT_PROPERTIES_FILE));
		Properties defaultProp = new Properties();
		Properties prop;
		defaultProp.load(defaultProperties);
		if(defaultProp !=null) {
			prop = new Properties(defaultProp);
			for(Object o: prop.keySet()) {
				String s = (String) o;
				System.out.println(s+":"+prop.getProperty(s));
			}
		}else {
			System.err.println("Pas de default properties : fichier default.properties manquant?");
			prop = new Properties();
		}
		
		FileReader is = new FileReader(new File(here+File.separator+"ressources"+File.separator+"config"+File.separator+f));
		if(is == null) {
			throw new FileNotFoundException("property file: "+f);
		}
		prop.load(is);
		Main.setSERVER(prop.getProperty("SERVER"));
		String port =prop.getProperty("PORT", "6667");
		if(EntierPositifNonVide.entre(port, 0, 65536)) {
			Main.setPORT(Integer.parseInt(port));
		}
		Main.setCHANNELS(getMultipleValues(prop, "CHANNELS"));
		Main.setTIMEOUT_BEFORE_RECONNECTING(Long.parseLong(prop.getProperty("Timeout_before_reconnecting")));
		Main.setDebug(Boolean.parseBoolean(prop.getProperty("Debug")));
		
		Bot.setTIME_BETWEEN_MESSAGES(Long.parseLong(prop.getProperty("Time_between_messages")));
		b.setAdmins(getMultipleValues(prop, "Admins"));
		
		RejoinThread.setDEFAULT_WAIT_BEFORE_RECONNECT(Long.parseLong(prop.getProperty("Wait_before_reconnecting_when_kicked")));
		
		Cache.setTIME_BETWEEN_RELOADS(1000*Long.parseLong(prop.getProperty("Minimum_time_beetween_+reload")));
		
		cr.setTimeout(Long.parseLong(prop.getProperty("CacheReloader_timeout")));
		
		if(prop.getProperty("Caractere_commande") != null) {
			Action.CARACTERE_COMMANDE=prop.getProperty("Caractere_commande").charAt(0);
		}
		if(prop.getProperty("listeall") != null) {
			Liste.allAllowed = Boolean.parseBoolean(prop.getProperty("listeall"));
		}
		if(prop.getProperty("infoall") != null) {
			Info.INFO_ALL = Boolean.parseBoolean(prop.getProperty("infoall"));
		}
		
		
		
		return true;
	}
	
	private String[] getMultipleValues(Properties prop, String key) {
		return prop.getProperty(key).split(",");
	}
	
	
}
