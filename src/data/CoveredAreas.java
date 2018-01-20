package data;

import java.util.ArrayList;
import java.util.List;

public class CoveredAreas {

	private final List<TechnoCoverage> technos;
	private final String name;
	
	public CoveredAreas(String name, List<TechnoCoverage> techno) {
		technos=techno;
		this.name=name;
		
	}
	public CoveredAreas(String name, TechnoCoverage...techno) {
		ArrayList<TechnoCoverage> l = new ArrayList<>();
		for(int i=0;i<techno.length;++i) {
			l.add(techno[i]);
		}
		this.technos=l;
		this.name=name;
		
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
		
		return res;
	}
	
	

}
