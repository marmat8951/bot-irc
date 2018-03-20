package rss;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import data.ISPDAO;

public class RSSChecker implements Runnable {

	private Thread thread;
	private final String threadName="RssChecker";
	private long timeout=3600;
	private String rssaddr;
	boolean end = false;
	DocumentBuilderFactory docbfact=DocumentBuilderFactory.newInstance();
	
	public RSSChecker(String address) {
		this.rssaddr = address;
	}

	public void start() {
		if(thread == null) {
			thread = new Thread(this, this.threadName);
			thread.start();
		}
	}
	
	@Override
	public void run() {
		do {
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
				doc = db.parse(idao.executeGet("https://planet.ffdn.org/atom.xml"));
			} catch (SAXException | IOException e1) {
				e1.printStackTrace();
			}
			NodeList nl = doc.getElementsByTagName("entry");
			nl.getLength();
			
			try {
				Thread.sleep(1000*timeout);
			} catch (InterruptedException e) {
				System.err.println("rss Chacker à été arrété");
			}
		}while(!end);

	}

}
