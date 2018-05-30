package data;

/**
 * Cette enum permet de de transformer toutes les techno annoncée dans les zones couvertes en une valeur.
 */
public enum TechnoCoverage {
	
	
	VPN,FTTH,ADSL,VDSL,xDSL,WIFI,PIGEON,CABLE,AUTRE;
	
	public String toString() {
		return this.name().toLowerCase();	
	}
	
	public static TechnoCoverage getFromString(String s){
		if(s.equalsIgnoreCase("vpn")) {
			return VPN;
		}else if(s.equalsIgnoreCase("adsl")) {
			return TechnoCoverage.ADSL;
		}else if(s.equalsIgnoreCase("vdsl")) {
			return TechnoCoverage.VDSL;
		}else if(s.equalsIgnoreCase("dsl") || s.equalsIgnoreCase("xdsl")) {
			return xDSL;
		}else if(s.equalsIgnoreCase("wifi")) {
			return WIFI;
		}else if(s.equalsIgnoreCase("pigeon")) {
			return PIGEON;
		}else if(s.equalsIgnoreCase("cable")) {
			return CABLE;
		}else {
			return AUTRE;
		}
		
	}
	
}
