package data;

import data.AddresseToGPS.Lieu;

/**
 * Classe servant pour {@link AddresseToGPS} a indiquer plusieurs possibilitées et a les stoquer, afin qu'elles soient traitées dans un catch
 * @author marmat
 *
 */
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
