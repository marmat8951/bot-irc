package rss;

/**
 * Le but principal de cette classe est de stocker les articles du flux RSS. 
 * @author marmat
 */
public class RssDataRemainder {

	public RssData[] data;

	public RssDataRemainder(int default_size) {
		data = new RssData[default_size];
	}

	public int getSIZE() {
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
		for(; i<data.length && data[i] !=null ;++i) {
		}
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


}
