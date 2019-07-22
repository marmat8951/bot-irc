package bot.irc.data;

import java.util.Scanner;

public class TestCoordinates {

	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		double lat1 = sc.nextDouble();
		double lon1 = sc.nextDouble();
		double lat2 = sc.nextDouble();
		double lon2 = sc.nextDouble();
		Coordinates c1 = new Coordinates(lat1, lon1);
		double dist = c1.distanceAvec(lat2, lon2);
		System.out.println("Distance estimée : "+dist);
		sc.close();
	}

}
