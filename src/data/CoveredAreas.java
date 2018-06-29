package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe stoquant les zones qu'un FAI couvre sous la forme d'une liste de technologies et d'un nom
 * @author marmat
 *
 */
public class CoveredAreas {

	private final List<TechnoCoverage> technos;
	private final String name;
	private ISP isp;
	private Polygon poligon;
	


	public CoveredAreas(String name, ISP isp,List<TechnoCoverage> techno, Polygon poligon) {
		super();
		this.technos = techno;
		this.name = name;
		this.isp = isp;
		this.poligon = poligon;
	}

	public CoveredAreas(String name, ISP isp, List<TechnoCoverage> techno) {
		this(name,isp,techno,null);
	}
	
	public CoveredAreas(String name, List<TechnoCoverage> techno) {
		this(name,null,techno);
	}
	
	public CoveredAreas(String name, ISP isp, TechnoCoverage...techno) {
		ArrayList<TechnoCoverage> l = new ArrayList<>();
		for(int i=0;i<techno.length;++i) {
			l.add(techno[i]);
		}
		this.technos=l;
		this.name=name;
		this.isp=isp;
		
		
	}
	


	public List<TechnoCoverage> getTechnos() {
		return technos;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		String res = name+" via ";
		for (TechnoCoverage tc: technos) {
			res+=tc+" ";
		}
		res+="par "+isp.getBetterName();
		
		return res;
	}
	public ISP getIsp() {
		return isp;
	}
	public void setIsp(ISP isp) {
		this.isp = isp;
	}
	
	public Polygon getPoligon() {
		return poligon;
	}

	public void setPoligon(Polygon poligon) {
		this.poligon = poligon;
	}
	

}
