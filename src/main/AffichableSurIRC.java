package main;

import java.util.List;

public interface AffichableSurIRC {
	
	/**
	 * Cette interface a été faite pour la metode de toStringToIRC.
	 * Elle permet de regrouper tout ce qui est affichable sur plusieurs lignes.
	 */
	
	public static final int MAX_CHARACTERS=400;
	
	/**
	 * Renvoie une Liste de chaine de caractères pour permettre l'affichage sur IRC ligne par ligne, bien que le \n ne soit pas interprété.
	 * @return Une Liste de chaine correspondant à toutes les lignes que le bot doit écrire
	 */
	public List<String> toStringIRC ();

}
