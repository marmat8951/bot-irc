package data;

import data.AddresseToGPS.Lieu;

public class MultiplePossibleAddressException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final Lieu[] lieux;
	public final String adresse;
	
	
	public MultiplePossibleAddressException(Lieu [] lieux, String requestedAdress) {
		this.lieux = lieux;
		this.adresse = requestedAdress;
	}


}
