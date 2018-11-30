package data;

import data.AddresseToGPS.Lieu;

/**
 * Classe servant pour {@link AddresseToGPS} a indiquer plusieurs possibilitées et a les stoquer, afin qu'elles soient traitées dans un catch ou a indiquer qu'aucune n'est disponible
 * @author marmat
 *
 */
public class ErrorAddressException extends Exception {

	private static final long serialVersionUID = 1L;
	public final Lieu[] lieux;
	public final String adresse;
	public boolean vide=false;
	public final String msg;
	
	
	public ErrorAddressException(Lieu [] lieux, String requestedAdress) {
		this(lieux,requestedAdress,"");
	}
	
	public ErrorAddressException(Lieu [] lieux, String requestedAdress, String msg) {
		this.lieux = lieux;
		this.adresse = requestedAdress;
		if(lieux == null || lieux.length==0) {
			vide=true;
		}
		this.msg = msg;
	}

	/**
	 * @return Si le tableaua d'adresse est vide ou non
	 */
	public boolean isVide() {
		return vide;
	}

	
}
