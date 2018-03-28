package rss;

import java.util.ArrayList;
import java.util.List;

import main.AffichableSurIRC;

public class RssData implements AffichableSurIRC{

	private String auteur;
	private String date;
	private String titre;
	private String lien;
	
	public RssData(String auteur, String date, String titre, String lien) {
		super();
		this.auteur = auteur;
		this.date = date;
		this.titre = titre;
		this.lien = lien;
	}


	public RssData() {
		
	}
	
	public String getAuteur() {
		return auteur;
	}


	public void setAuteur(String auteur) {
		auteur = auteur;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getTitre() {
		return titre;
	}


	public void setTitre(String titre) {
		this.titre = titre;
	}


	public String getLien() {
		return lien;
	}


	public void setLien(String lien) {
		this.lien = lien;
	}

	public String toString() {
		return "Article par "+auteur+" : "+titre+" le "+date+" lien: "+lien;
	}


	@Override
	public List<String> toStringIRC() {
		List<String> res = new ArrayList<>();
		res.add(titre);
		String auth = "Par "+auteur;
		if(date != null) {
			auth += "le "+date;
		}
		res.add(auth);
		if(lien != null) {
		res.add("Plus d'infos sur "+lien);
		}
		return res;
	}
	
	

}
