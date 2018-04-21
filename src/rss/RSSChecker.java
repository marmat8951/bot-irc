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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import data.ISPDAO;
import main.Bot;
import main.Main;

public class RSSChecker implements Runnable {

	private Thread thread;
	private final String threadName="RssChecker";
	private long timeout=3600;
	private String rssaddr;  // Must be https://planet.ffdn.org/atom.xml
	boolean end = false;
	DocumentBuilderFactory docbfact=DocumentBuilderFactory.newInstance();
	private Date lastarticle = new Date(); //last info
	public static final SimpleDateFormat DATE_FORMATIN =  new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss'Z'");
	private Bot b;
	
	public RSSChecker() {
		this.rssaddr="";
	}

	public RSSChecker(String address, Bot b) {
		this.rssaddr = address;
		this.b=b;
	}

	public void start() {
		if(thread == null) {
			thread = new Thread(this, this.threadName);
			thread.start();
		}
	}
	
	
	private void afficheArticle(Node article, String date) {
		NodeList nl = article.getChildNodes();
		RssData rssdata = new RssData(article);
		rssdata.setDate(date);
		b.sendMessagesOnAllChannels(rssdata.toStringIRC());
		
	}
	
	private void afficheArticle(Node article) {
		afficheArticle(article, "");
			
	}
	
	
	
	private void workOnEntry(NodeList nl) {
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
						if(date.after(lastarticle)) {
							if(!istherenews) {
								istherenews=true;
								b.sendMessageOnAllChannels("Nouveautée sur planet.ffdn.org:");
							}
							afficheArticle(article,Main.DATE_FORMAT_OUT.format(date));
							lastarticle = date;
						}
					} catch (DOMException | ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void run() {
		if(Main.isDebug()) {
			System.out.println(this.threadName+" lancé sur "+rssaddr);
		}
		do {
			if(Main.isDebug()) {
				System.out.println("Parsing du RSS "+rssaddr);
			}
			DocumentBuilder db=null;
			Document doc = null;
			try {
				db = docbfact.newDocumentBuilder();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ISPDAO idao = ISPDAO.getInstance();
			try {
				doc = db.parse(rssaddr);
			} catch (SAXException | IOException e1) {
				e1.printStackTrace();
			}
			if(doc!=null) {
				NodeList nl = doc.getElementsByTagName("entry");
				workOnEntry(nl);
			}

			try {
				Thread.sleep(1000*timeout);
			} catch (InterruptedException e) {
				System.err.println(this.threadName+" à été arrété");
			}
		}while(!end);



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
