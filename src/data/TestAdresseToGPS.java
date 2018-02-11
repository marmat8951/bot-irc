package data;

import data.AddresseToGPS.Lieu;
import main.Bot;

public class TestAdresseToGPS {


	public static void main(String[] args) {
		Bot b = new Bot();
		String adresse = "Rue de la pulmez, Landas, France";
		adresse=adresse.replaceAll("\\s", "%20");
		AddresseToGPS ad = new AddresseToGPS(adresse,b);
		Lieu l=null;
		try {
			l=ad.getLieu();
		} catch (MultiplePossibleAddressException e) {
			System.err.println("Plusieurs lieux pour cette adresse, soyez plus précis");
		}
		if(l!=null) {
		System.out.println(l.DisplayName);
		}else {
			System.out.println("Lieu inconnu");
		}
	}

}
