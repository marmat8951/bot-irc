package data;

public class Coordinates {
	
	/**
	 * Cette Classe est là pour stoquer les atributs de coord
	 */
	
	private double latitude,longitude;
	
	public Coordinates(double latitude,double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
 	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}

	/**
	 * Cette methode mesure la distance entre deux points GPS. Cependant, cette mesure est aproximative, et ne prend pas en compte l'altitude.
	 * Cette mesure de distance est faite a vol d'oiseau.
	 * @param latitude2 Latitude avec laquelle mesurer
	 * @param longitude2 longitude avec laquelle mesurer
	 * @return Distance calculée en mètres.
	 */
	public double distanceAvec(double latitude2,double longitude2) {
		final double EARTH_RADIUS = 6378137;  // Rayon en mètres de la terre
		double rlat1 = Math.PI * latitude/180;
		double rlat2 = Math.PI * latitude2/180;
		double rlon1 = Math.PI * longitude/180;
		double rlon2 = Math.PI * longitude2/180;
		double dlon = (rlon2 - rlon1) / 2;
		double dlat = (rlat2 - rlat1) / 2;
		double a = (Math.sin(dlat) * Math.sin(dlat)) + Math.cos(rlat1) * Math.cos(rlat2) * (Math.sin(dlon) * Math.sin(dlon));
		double dist = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return dist * EARTH_RADIUS;
		
	}
	
}