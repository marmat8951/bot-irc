package rss;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import main.IRCBot;
import main.Main;

/**
 * Cette classe verifie si il y a de nouveaux articles sur le flux RSS.
 * Dans ce cas, il les affiche et les met en mémoire pour l'action +RSS
 * @author marmat
 */
public class RSSChecker implements Runnable {
	

	private Thread thread;
	private final String threadName="RssChecker";
	private long timeout=3600;
	private String rssaddr;  // Must be https://planet.ffdn.org/atom.xml
	boolean end = false;
	DocumentBuilderFactory docbfact=DocumentBuilderFactory.newInstance();
	private Date lastarticle = new Date(); //last info
	public static final SimpleDateFormat DATE_FORMATIN =  new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss'Z'");
	private IRCBot b;
	private volatile RssDataRemainder remainder;
	
	public RSSChecker() {
		this("",null);
	}
	
	/**
	 * Crée le checker de RSS sur l'adresse en paramètre
	 * @param address adresse à laquelle faire la vérification
	 * @param b bot utilisé pour stoquer les données et retravailler 
	 */
	public RSSChecker(String address, IRCBot b) {
		this.rssaddr = address;
		this.b=b;
		this.remainder = b.getRssdata();
	}

	public void start() {
		if(thread == null) {
			thread = new Thread(this, this.threadName);
			thread.start();
		}
	}
	
	/**
	 * Affiche l'aricle sur tous les channels du bot
	 * @param data donnée RSS
	 */
	private void afficheArticle(RssData data) {
		b.sendMessagesOnAllChannels(data.toStringIRC());
	}

	/**
	 * Crée les données de RSS et les envoie a {@link #afficheArticle(RssData)}
	 * @param article sous la forme d'un noeud
	 */
	private void afficheArticle(Node article) {
		afficheArticle(new RssData(article));
	}

	/**
	 * Travaille sur la liste d'entrée. Vérifie si il y en a des plus récents que
	 * le dernier socké. Si c'est le cas, va prévenir les channels de la nouveautée, et l'intégrer au flux RSS du bot.
	 * @param nl
	 */
	private void workOnEntry(NodeList nl) {
		if(Main.isDebug()) {
			System.out.println("Verification des <entry>");
			System.out.println("Dernier article le: "+Main.DATE_FORMAT_OUT.format(lastarticle));
		}
		int len = nl.getLength();
		boolean istherenews = false;
		for(int i=len-1; i>=0;i--) {
			Node article = nl.item(i);
			NodeList fils = article.getChildNodes();
			int flen = fils.getLength();
			for(int j=0; j<flen;j++) {
				if(fils.item(j).getNodeName().equalsIgnoreCase("updated")){
					try {
						Date date = DATE_FORMATIN.parse(fils.item(j).getTextContent());
						if(Main.isDebug()) {
							System.out.println("Date de l'article: "+Main.DATE_FORMAT_OUT.format(date));
						}
						if(date.after(lastarticle)) {
							if(Main.isDebug()) {
								System.out.println("Cet article est nouveau.");
							}
							if(!istherenews) {
								istherenews=true;
								b.sendMessageOnAllChannels("Nouveauté sur planet.ffdn.org:");
								
							}
							RssData rs = new RssData(article);
							remainder.push(rs);
							afficheArticle(rs);
							lastarticle = date;
						}
					} catch (DOMException | ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public RssDataRemainder getRemainder() {
		return remainder;
	}

	public void setRemainder(RssDataRemainder remainder) {
		this.remainder = remainder;
	}

	@Override
	public void run() {
		if(Main.isDebug()) {
			System.out.println(this.threadName+" lancé sur "+rssaddr);
		}
		boolean firstRun = true;
		do {
			if(Main.isDebug()) {
				System.out.println("Parsing du RSS "+rssaddr);
			}
			DocumentBuilder db=null;
			Document doc = null;
			try {
				db = docbfact.newDocumentBuilder();
			} catch (ParserConfigurationException e1) {
				b.sendMessageToAdmins("Erreur lors de la creation du document Builder dans RSSChecker.run()"); 
				e1.printStackTrace();
			}
			try {
				doc = db.parse(rssaddr);
			} catch (SAXException | IOException e1) {
				e1.printStackTrace();
				b.sendMessageToAdmins("Erreur du parseur XML");
			}
			if(doc!=null) {
				NodeList nl = doc.getElementsByTagName("entry");
				if(firstRun) {
					initRemainder(nl);
				}
				workOnEntry(nl);
			}else {
				b.sendMessageToAdmins("Erreur au parsing du RSS, le document était null");
				System.err.println("Erreur au parsing du RSS, le document était null");
			}

			try {
				Thread.sleep(1000*timeout);
			} catch (InterruptedException e) {
				System.err.println(this.threadName+" à été arrété");
			}
			firstRun=false;
		}while(!end);

	}
	
	/**
	 * Initialise le Remainder avec la liste de noeud RSS passée en paramètre.
	 * Cette méthode est appelée au démarage afin de créer le remainder et d'y inserer les données présentes au départ.
	 * @param nl
	 */
	private void initRemainder(NodeList nl) {
		int len = nl.getLength();
		for(int i=len-1; i>=0;--i) {
			RssData data = new RssData(nl.item(i));
			remainder.push(data);
		}
	}

	public String getRssaddr() {
		return rssaddr;
	}

	public void setRssaddr(String rssaddr) {
		this.rssaddr = rssaddr;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
