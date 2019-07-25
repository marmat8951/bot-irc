package bot.irc.rss;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import bot.irc.main.AffichableSurIRC;

/**
 * Le but principal de cette classe est de stocker les articles du flux RSS.
 * Celleçi agit comme un espace de taille fixe, dans lequel l'élément le plus ancien est supprimé au profit du plus récent.
 * @author marmat
 */
public class RssDataRemainder extends Observable implements AffichableSurIRC{

	private RssData[] data;
	public static final int DEFAULT_SIZE=10;
	private boolean ready = false;

	public RssDataRemainder(int defaultSize) {
		data = new RssData[defaultSize];
	}
	
	public RssDataRemainder() {
		this(DEFAULT_SIZE);
	}

	public int getSize() {
		return data.length;
	}
	

	/**
	 * Modifie la taille du remainder. Si la nouvelle taille est plus importante, alors crée un nouveau tableau de taille voulue et remet les valeurs précédentes, si elle est plus faible, alors elle détruit les éléments les plus vieux.
	 * Dans le cas de la taille plus importante, il laisse a <code>null</code>  les emplacements les plus anciens.
	 * @param size nouvelle taille
	 */
	public void setSIZE(int size) {
		if(size != data.length) {
			RssData[] newData = new RssData[size];
			for(int i=0;i<data.length && i<newData.length;++i) {
				newData[i]=data[i];
			}
		}
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Détruit le remainder précédent et en crée un nouveau de même taille.
	 */
	public void clear() {
		data = new RssData[data.length];
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Renvoie le nombre d'éléments non null stockés par le remainder
	 * @return Le nombre d'élements de l'objets qui ne sont pas null
	 */
	public int getCompletion() {
		int i = 0;
		for(; i<data.length && data[i] !=null ;++i);
		return i;
	}

	/**
	 * Représente un sift dans les Donnée du remainder. Déplace tout les éléments.
	 * @return L'élément supprimé du remainder a cause de l'insertion du nouveau
	 */
	private RssData movetoright() {
		RssData res = data[data.length-1];
		for(int i=data.length-1;i>0;--i) {
			data[i]=data[i-1];
		}
		return res;
	}

	/**
	 * Décale les valeurs et place la nouvelle mise en paramètre
	 * @param newdata
	 */
	public void push(RssData newdata) {
		movetoright();
		this.data[0]=newdata;
		if(ready) {
			this.setChanged();
			this.notifyObservers(newdata);
		}
	}
	
	/**
	 * Récupere les données correspondant à l'ID indiqué en paramètre
	 * @param id identifiant sous forme d'un entier
	 * @return Données ou null si l'identifiant est incorrect ou n'ayant pas encore de valeur.
	 */
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
			String ligne = "["+(i)+"] "+data[i].getTitre();
			if(data[i].getAuteur()!= null && !data[i].getAuteur().equals("")) {
				ligne +=" par "+data[i].getAuteur();
			}
			if(data[i].getSource()!= null && !data[i].getSource().equals("")) {
				ligne +=" via "+data[i].getSource();
			}
			res.add(ligne);
		}
		return res;
	}

	/**
	 * @return the ready
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * @param ready the ready to set
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public RssData[] getData() {
		return data;
	}

	public void setData(RssData[] data) {
		this.data = data;
	}
	
	

}
