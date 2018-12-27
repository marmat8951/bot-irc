package bot.irc.rss;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bot.irc.main.AffichableSurIRC;
import bot.irc.main.Main;

/**
 * Classe de stockage des informations d'une entrée RSS.
 * @author marmat
 *
 */
public class RssData implements AffichableSurIRC{

	private String auteur;
	private String date;
	private String titre;
	private String lien;
	private String source;

	public RssData(String auteur, String date, String titre, String lien, String source) {
		super();
		this.auteur = auteur;
		this.date = date;
		this.titre = titre;
		this.lien = lien;
		this.source = source;
	}

	/**
	 * Transforme un Noeud correspondant à un entry en un RSSData
	 * @param item Noeud w3c 
	 */
	public RssData(Node item, Date date) {
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
			}else if(balise.equalsIgnoreCase("source")) {
				NodeList nl = actualchild.getChildNodes();
				for(int j = 0; j<nl.getLength();++j) {
					Node node = nl.item(j);
					if(node.getNodeName().equalsIgnoreCase("title")) {
						setSource(node.getTextContent());
					}
				}
			}else if(balise.equalsIgnoreCase("updated")) {
				try {
					Date d = RSSChecker.DATE_FORMATIN.parse(actualchild.getTextContent());
					setDate(Main.DATE_FORMAT_OUT.format(d));
				} catch (DOMException | ParseException e) {
					e.printStackTrace();
				}
			}
		}
		if(date != null) {
			this.setDate(Main.DATE_FORMAT_OUT.format(date));
		}
	}

	public RssData(Node item) {
		this(item,null);
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
	

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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
		return "Article par "+auteur+" : "+titre+" le "+date+" lien: "+lien+" via "+source;
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
		if(!(source == null || source.equals(""))) {
			auth += " via "+source;
		}
		if(!auth.equals("")) res.add(auth);
		if(lien != null) {
			res.add("Plus d'infos sur "+lien);
		}
		return res;
	}



}
