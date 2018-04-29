package rss;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	
	public RssData(Node item) {
		this();
		NodeList childs = item.getChildNodes();
		int length  = childs.getLength();
		for(int i = 0; i<length;++i) {
			Node actualchild = childs.item(i);
		
		String balise = actualchild.getNodeName();
		if(balise.equalsIgnoreCase("title")) {
			setTitre(actualchild.getTextContent());
		}else if(balise.equalsIgnoreCase("author")) {
			NodeList authlist =actualchild.getChildNodes();
			for(int j = 0;j<authlist.getLength();j++) {
				Node bn = authlist.item(j);
				if(bn.getNodeName().equalsIgnoreCase("name")) {
					String a = ""+bn.getTextContent();
					setAuteur(a);
				}
			}
		}else if(balise.equalsIgnoreCase("link")) {
			NamedNodeMap nmp = actualchild.getAttributes();
			String link = nmp.getNamedItem("href").getTextContent();
			if(link!=null) setLien(link);
		}
		}
	}


	public RssData() {
		this.auteur="";
		this.date="";
		this.titre="";
		this.lien="";
	}
	
	public String getAuteur() {
		return auteur;
	}


	public void setAuteur(String auteur) {
		this.auteur = auteur;
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
		String auth= "";
		if(!(auteur == null || auteur.equals(""))) 
			auth = "Par "+auteur+" ";
		if(!(date == null || date.equals(""))) {
			auth += "le "+date;
		}
		if(!auth.equals("")) res.add(auth);
		if(lien != null) {
		res.add("Plus d'infos sur "+lien);
		}
		return res;
	}
	
	

}
