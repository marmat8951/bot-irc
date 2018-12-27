package bot.irc.data;

/**
 * Cette classe représente un segment composé de 2 coordonées.(points)
 * @author marmat
 *
 */
public class Segment {
	private Coordinates c1,c2;
	
	public Segment(Coordinates c1 , Coordinates c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	
	public boolean isLongitudeBeetweenTheTwoPoints(double longitude) {
		boolean b1 = (longitude >= c1.getLongitude()) && (longitude <= c2.getLongitude());
		boolean b2 = (longitude <= c1.getLongitude()) && (longitude >= c2.getLongitude());
		return b1 || b2;
	}
	
	public boolean isLatitudeBeetweenTheTwoPoints(double latitude) {
		boolean b1 = (latitude >= c1.getLatitude()) && (latitude <= c2.getLatitude());
		boolean b2 = (latitude <= c1.getLatitude()) && (latitude >= c2.getLatitude());
		return b1 || b2;
	}
	/**
	 * Longueur du segment. Utilise Coordinate.distanceAvec avec les deu paramètres.
	 * @return distance
	 */
	public double length() {
		return c1.distanceAvec(c2);
	}
	
	/**
	 * Regarde quelle valeur de longitude on a sion place un point à une latitude sur le segment
	 * @param latitude latitude qui est censée se situer entre celle des deux points.
	 * @return longitude correspondante a la latitude sur le segment.
	 */
	public double getLongitudeCorrespondingToTheLatitude(double latitude) {
		Coordinates pointsTriangle;
		if(c1BiggestLat()) {
			pointsTriangle = new Coordinates(c1.getLatitude(), c2.getLongitude());
		}else {
			pointsTriangle = new Coordinates(c2.getLatitude(), c1.getLongitude());
		}
		double distPtC1=pointsTriangle.distanceAvec(c1);
		double distPtC2=pointsTriangle.distanceAvec(c2);
		double rapport = new Coordinates(latitude,c2.getLongitude()).distanceAvec(c2)/distPtC2;
		return c2.getLatitude()+(rapport*(distPtC1));
	}
	
	public boolean c1BiggestLat() {
		return c1.getLatitude()>=c2.getLatitude();
	}

}
