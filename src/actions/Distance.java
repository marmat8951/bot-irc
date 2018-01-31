package actions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import data.Coordinates;
import data.ISP;
import main.Bot;
import main.Cache;
import main.Main;

public class Distance extends Action {
	public static final int NOMBRE_AFFICHABLE = 3;

	public Distance(Bot b) {
		super(b);
		List<String> kw = new ArrayList<>();
		kw.add("dist");
		kw.add("distance");
		this.keyWords=kw;
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, String message) {
		String s = message.substring(1);
		s=s.replace(',', '.');
		s=s.substring(s.indexOf(' ')+1); // Je me met après la commande
		double latitude = Double.POSITIVE_INFINITY, longitude = latitude;
		try {
			if(Main.isDebug()) {
			bot.sendMessage(channel, s.substring(0, s.indexOf(' ')));
			}
			latitude = Double.parseDouble(s.substring(0, s.indexOf(' ')));
			s=s.substring(s.indexOf(' ')+1); // Je me et au second paramètre
			longitude = Double.parseDouble(s);
		}catch(Exception e) {
			bot.sendMessage(channel, "Erreur: l'un des arguments n'est pas une coordonnée sous la forme d'un nombre a virgule");
		}
		ISP[] plusProches = getISPPlusProche(latitude, longitude);
		for(int i=0;i<plusProches.length;++i) {
			if(plusProches[i]!=null) {
				double distance =  plusProches[i].getData().getCoordonnees().distanceAvec(latitude, longitude);
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				distance = distance / 1000.0; 	//On met en KM		
				bot.sendMessage(channel, (i+1)+": "+plusProches[i].getBetterName()+" à "+nf.format(distance)+" Km");
			}
		}
		

	}

	
	private ISP[] getISPPlusProche(double latitude, double longitude) {
		return getISPPlusProche(new Coordinates(latitude, longitude));
	}
	
	private ISP[] getISPPlusProche(Coordinates coord) {
		ISP[] res = new ISP[NOMBRE_AFFICHABLE];
		Cache c = Cache.getInstance();
		List<ISP> allFAI = c.getListeInFede();
		for(ISP fai:allFAI) {
			Coordinates faicoord = fai.getData().getCoordonnees();
			double distance = faicoord.distanceAvec(coord);
			if(distance>=0) {
			for(int i=0;i<NOMBRE_AFFICHABLE;++i) {
				
				if(res[i] == null || distance<res[i].getData().getCoordonnees().distanceAvec(coord)) {				//TODO creer un accesseur plus rapide
					if(res[i]!=null) {
						decale(res,i);
						res[i] = fai;											//J'insere
					}else {
						res[i] = fai;
					}
					break;
				}
				
			}
			}else {
				
			}
			
		}
		
		return res;
	}
	
	private void decale(ISP [] array , int id) {
		int length = array.length;
		for(int i = length-1; i>id; --i) {
			array[i]=array[i-1];
		}
	}
	
}

