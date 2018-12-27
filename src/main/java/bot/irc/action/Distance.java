package bot.irc.action;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import bot.irc.data.AddresseToGPS;
import bot.irc.data.AddresseToGPS.Lieu;
import bot.irc.data.Coordinates;
import bot.irc.data.ErrorAddressException;
import bot.irc.data.ISP;
import bot.irc.data.Message;
import bot.irc.main.Bot;
import bot.irc.main.Cache;

public class Distance extends Action {
	public static final int NOMBRE_AFFICHABLE = 3;

	public Distance(Bot b) {
		super(b);
		List<String> kw = new ArrayList<>();
		kw.add("dist");
		kw.add("distance");
		this.keyWords=kw;
	}

	@Deprecated
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
	
	private List<String> getPlusProchesL(double latitude, double longitude, String sender, String channel) {
		List<String> res = new ArrayList<>();
		ISP[] plusProches = getISPPlusProche(latitude, longitude);
		for(int i=0;i<plusProches.length;++i) {
			if(plusProches[i]!=null) {
				double distance =  plusProches[i].getData().getCoordonnees().distanceAvec(latitude, longitude);
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				distance = distance / 1000.0; 	//On met en KM		
				res.add((i+1)+": "+plusProches[i].getBetterName()+" à "+nf.format(distance)+" Km");
			}
		}
		return res;
	}
	
	
	
	/**
	 * @param message message recu
	 * @param sender personne qui envoie le message
	 * @param channel channel dont le message provient
	 * @return Les coordonnées du lieu correspondant au contenu du message
	 * @throws ErrorAddressException Si il y a plussieurs coordonnées correspondantes à l'adresse demandée.
	 */
	private Coordinates getCoordinatesFromMessage(String message, String sender, String channel) throws ErrorAddressException {
		final double MAX_DIFF = 0.1; //Differences there MUST between 2 coordinates so they are seen as differents
		AddresseToGPS a2gps = new AddresseToGPS(message.substring(message.indexOf(' ')+1));
		Lieu[] lieux = a2gps.getAllLieu();
		if(lieux == null || lieux.length == 0) {
			throw new ErrorAddressException(null, a2gps.getAdresse(),"Aucun lieu ne correspond. Requete effectuée: "+a2gps.getAddressToQuerry());
		}else if(lieux.length == 1) {
			return lieux[0].coordonees;
		}else {

			for(int i=0;i<lieux.length; ++i) {
				for(int j=0;j<lieux.length; ++j) {
					if(!lieux[i].coordonees.equals(lieux[j].coordonees, MAX_DIFF)) {
						throw new ErrorAddressException(lieux, a2gps.getAdresse());
					}
				}
			}
			return lieux[0].coordonees;
		}

	}

	
	
	/**
	 * Récupere le FAI le plus proche de la position indiquée en paramètre. Utilise {@link Distance#getISPPlusProche(Coordinates)}
	 * @param latitude
	 * @param longitude
	 * @return Tableau contenant les FAI les plus proches.
	 */
	private ISP[] getISPPlusProche(double latitude, double longitude) {
		return getISPPlusProche(new Coordinates(latitude, longitude));
	}

	/**
	 * Récupere le FAI le plus proche de la coordonnée indiquée en paramètre
	 * @param coord
	 * @return Tableau contenant les FAI les plus proches.
	 */
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

	/**
	 * Décalle les éléments du tableau afin de pouvoir un inserer un.
	 * @param array tableau dans lequel effectuer l'opération
	 * @param id numero a partir le décalage doit s'oppérer
	 */
	private void decale(ISP [] array , int id) {
		int length = array.length;
		for(int i = length-1; i>id; --i) {
			array[i]=array[i-1];
		}
	}

	@Override
	public String help() {
		return " suivi de la latitude, puis la longitude au format décimal, ou une addresse. Exemple: "+Action.CARACTERE_COMMANDE+keyWords.get(0)+" 50,410658 61.574548      Renvoie les "+NOMBRE_AFFICHABLE+" FAI de la fédération les plus proches à vol d'oiseau";
	}

	@Override
	@Deprecated
	public void react(String channel, String sender, String login, String hostname, Message message) {
		double latitude = Double.POSITIVE_INFINITY, longitude = latitude;
		if(!message.hasNoParameters()) {
			try {
				latitude = message.getElementAsDouble(0);
				longitude = message.getElementAsDouble(1);
				affichePlusProches(latitude, longitude, sender, channel);
			}catch(NumberFormatException e) {	//Cela doit alors être une adresse!
				try {
					Coordinates ca = getCoordinatesFromMessage(message.getAllParametersAsOneString(), sender, channel);
					latitude = ca.getLatitude();
					longitude = ca.getLongitude();
					affichePlusProches(latitude, longitude, sender, channel);
				} catch (ErrorAddressException e1) {
					bot.sendMessage(sender, channel, "Plusieurs possibilités pour cet endroit, nous choisirons le premier:");
					for(int i = 0; i<e1.lieux.length; ++i) {
						bot.sendMessage(sender, channel, (i+1)+":"+e1.lieux[i].toString());
					}
					latitude = e1.lieux[0].coordonees.getLatitude();
					longitude = e1.lieux[0].coordonees.getLongitude();
					affichePlusProches(latitude, longitude, sender,channel);
				}
			}

		}else {
			bot.sendMessage(sender, channel, message.commandCharacterAndKeyword()+help());
		}
	
		
	}


	@Override
	public List<String> reactL(String channel, String sender, String login, String hostname, Message message) {
		List<String> res = new ArrayList<>();
		double latitude = Double.POSITIVE_INFINITY, longitude = latitude;
		if(!message.hasNoParameters()) {
			try {
				latitude = message.getElementAsDouble(0);
				longitude = message.getElementAsDouble(1);
				res.addAll(getPlusProchesL(latitude, longitude, sender, channel));
			}catch(NumberFormatException e) {	//Cela doit alors être une adresse!
				try {
					Coordinates ca = getCoordinatesFromMessage(message.getAllParametersAsOneString(), sender, channel);
					latitude = ca.getLatitude();
					longitude = ca.getLongitude();
					res.addAll(getPlusProchesL(latitude, longitude, sender, channel));
				} catch (ErrorAddressException e1) {
					if(!e1.isVide()) {
						res.add("Plusieurs possibilités pour cet endroit, nous choisirons le premier:");
						for(int i = 0; i<e1.lieux.length; ++i) {
							res.add((i+1)+":"+e1.lieux[i].toString());
						}
						latitude = e1.lieux[0].coordonees.getLatitude();
						longitude = e1.lieux[0].coordonees.getLongitude();
						res.addAll(getPlusProchesL(latitude, longitude, sender, channel));
					}else {
						res.add(e1.msg);
					}
				}
			}

		}else {
			res.add(message.commandCharacterAndKeyword()+help());
		}
	
		return res;
	}

}

