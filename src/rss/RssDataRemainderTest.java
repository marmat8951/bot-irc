package rss;

import java.util.Date;
import java.util.List;

public class RssDataRemainderTest {

	public static void main(String[] args) {
		RssDataRemainder rd = new RssDataRemainder(3);
		RssData d1  =  new RssData("adrien",new Date().toString(), "A bit blog", "www.irc.to");
		RssData d2  =  new RssData("MArmat",new Date().toString(), "UneFede", "www.marmat.ovh");
		RssData d3  =  new RssData("quota",new Date().toString(), "Café", "www.ca.fe");
		RssData d4  =  new RssData("zorun",new Date().toString(), "ftth", "www.fible.ffdn.org");
		rd.push(d1);
		printList(rd.toStringIRC());
		rd.push(d2);
		rd.push(d3);
		printList(rd.toStringIRC());
		rd.push(d4);
		printList(rd.toStringIRC());
		rd.push(d4);
		printList(rd.toStringIRC());
		
	}

	public static void printList(List<String> data) {
		for(String s : data) {
			System.out.println(s);
		}
	}
}
