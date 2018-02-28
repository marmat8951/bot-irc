package actions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import data.AddresseToGPS;
import data.AddresseToGPS.Lieu;
import data.Coordinates;
import data.ISP;
import data.MultiplePossibleAddressException;
import main.Bot;
import main.Cache;

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
			latitude = Double.parseDouble(s.substring(0, s.indexOf(' ')));
			s=s.substring(s.indexOf(' ')+1); // Je me et au second paramètre
			longitude = Double.parseDouble(s);
			affichePlusProches(latitude, longitude, sender, channel);
		}catch(Exception e) {	//Cela doit alors être une adresse!
			try {
				Coordinates ca = getCoordinatesFromMessage(message, sender, channel);
				latitude = ca.getLatitude();
				longitude = ca.getLongitude();
				affichePlusProches(latitude, longitude, sender, channel);
			} catch (MultiplePossibleAddressException e1) {
				bot.sendMessage(sender, channel, "Plusieurs possibilités pour cet endroit, nous choisirons le premier:");
				for(int i = 0; i<e1.lieux.length; ++i) {
					bot.sendMessage(sender, channel, (i+1)+":"+e1.lieux[i].toString());
				}
				latitude = e1.lieux[0].coordonees.getLatitude();
				longitude = e1.lieux[0].coordonees.getLongitude();
				affichePlusProches(latitude, longitude, sender,channel);
			}
		}
				
		

	}
	
	private void affichePlusProches(double latitude, double longitude, String sender, String channel) {
		ISP[] plusProches = getISPPlusProche(latitude, longitude);
		for(int i=0;i<plusProches.length;++i) {
			if(plusProches[i]!=null) {
				double distance =  plusProches[i].getData().getCoordonnees().distanceAvec(latitude, longitude);
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				distance = distance / 1000.0; 	//On met en KM		
				bot.sendMessage(sender,channel, (i+1)+": "+plusProches[i].getBetterName()+" à "+nf.format(distance)+" Km");
			}
		}
	}
	
	private Coordinates getCoordinatesFromMessage(String message, String sender, String channel) throws MultiplePossibleAddressException {
		final double MAX_DIFF = 0.1; //Differences there MUST between 2 coordinates so they are seen as differents
		AddresseToGPS a2gps = new AddresseToGPS(message.substring(message.indexOf(' ')+1));
		Lieu[] lieux = a2gps.getAllLieu();
		if(lieux == null || lieux.length == 0) {
			bot.sendMessage(sender,channel, "Aucun lieu ne correspond. Requete effectuée: "+a2gps.getAddressToQuerry());
			return null;
		}else if(lieux.length == 1) {
			return lieux[0].coordonees;
		}else {
			
			for(int i=0;i<lieux.length; ++i) {
				for(int j=0;j<lieux.length; ++j) {
					if(!lieux[i].coordonees.equals(lieux[j].coordonees, MAX_DIFF)) {
						throw new MultiplePossibleAddressException(lieux, a2gps.getAdresse());
					}
				}
			}
			return lieux[0].coordonees;
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

	@Override
	public String help() {
		
		return " suivi de la latitude, puis la longitude au format décimal. Exemple: +"+keyWords.get(0)+" 50,410658 61.574548      Renvoie les "+NOMBRE_AFFICHABLE+" FAI de la fédération les plus proches à vol d'oiseau";
	}
	
}

