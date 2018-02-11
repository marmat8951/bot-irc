package verif_saisie;

public class LongCheck {

	
	public static boolean check(String s) {
		boolean virg=false;
		int i=0;
		for(; i<s.length() && charIsAllowed(s.charAt(i),i) ;++i) {
			if(estVirgule(s.charAt(i)) && virg == true) return false;
			if(estVirgule(s.charAt(i))) virg = true;
		}
		if(i==s.length()-1) {
			return true;
		}
		return false;
	}
	
	private static boolean charIsAllowed(char c, int pos) {
		if(c<=9 && c>=0) return true;
		if((c == '.' || c == ',')&&pos!=0) return true;
		if(c == '-' && pos==0) return true;
		return false;
	}
	
	private static boolean estVirgule(char c) {
		return (c == '.' || c == ',');
	}

}
