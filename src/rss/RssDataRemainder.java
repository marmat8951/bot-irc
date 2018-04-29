package rss;

import java.util.ArrayList;
import java.util.List;

import main.AffichableSurIRC;

/**
 * Le but principal de cette classe est de stocker les articles du flux RSS.
 * @author marmat
 */
public class RssDataRemainder implements AffichableSurIRC{

	public RssData[] data;

	public RssDataRemainder(int default_size) {
		data = new RssData[default_size];
	}

	public int getSize() {
		return data.length;
	}
	

	public void setSIZE(int size) {
		if(size != data.length) {
			RssData[] newData = new RssData[size];
			for(int i=0;i<data.length && i<newData.length;++i) {
				newData[i]=data[i];
			}
		}
	}

	public void clear() {
		data = new RssData[data.length];
	}

	/**
	 * 
	 * @return Le nombre d'élements de l'objets qui ne sont pas null
	 */
	public int getCompletion() {
		int i = 0;
		for(; i<data.length && data[i] !=null ;++i);
		return i;
	}

	private RssData movetoright() {
		RssData res = data[data.length-1];
		for(int i=data.length-1;i>0;--i) {
			data[i]=data[i-1];
		}
		return res;
	}

	public void push(RssData newdata) {
		movetoright();
		this.data[0]=newdata;
	}
	
	public RssData getDataWithId(int id) {
		if(id<0 || id>=data.length) return null;
		return data[id];
	}
	
	public String toString() {
		return "Il y a "+data.length+" slots d'articles dont "+getCompletion()+" occupés.";
	}
	
	public List<String> toStringIRC(){
		List<String> res = new ArrayList<String>();
		int len = getCompletion();
		for(int i=0;i<len;i++) {
			String ligne = "["+(i+1)+"] "+data[i].getTitre();
			if(data[i].getAuteur()!= null && !data[i].getAuteur().equals("")) {
				ligne +=" par "+data[i].getAuteur();
			}
			res.add(ligne);
		}
		return res;
	}

}
