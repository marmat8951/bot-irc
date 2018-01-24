package data;

import java.util.ArrayList;
import java.util.List;

public class CoveredAreas {

	private final List<TechnoCoverage> technos;
	private final String name;
	private ISP isp;
	
	public CoveredAreas(String name, ISP isp, List<TechnoCoverage> techno) {
		technos=techno;
		this.name=name;
		this.isp = isp;
		
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
	
	
	

}
