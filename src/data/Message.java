package data;

import java.util.ArrayList;
import java.util.List;

public class Message {
	private final char c;
	private final String commande;
	private List<String> parameters = new ArrayList<String>();
	
	public Message(String input) {
		c=input.charAt(0); // caractère de commande
		if(input.indexOf(' ')==-1) {
			this.commande = input.substring(1); // Remove the first character which is the command caracter
		}else {
			String[] tab = input.split("( )+");
			this.commande = tab[0].substring(1);
			String[] params = new String[tab.length-1];
			for(int i=1;i<tab.length;++i) {
				params[i-1]=tab[i];
			}
			addAllParameters(params);
		}
		
	}
	
	private void addAllParameters(String[] param) {
		for(int i=0;i<param.length;++i) {
			parameters.add(param[i]);
		}
	}
	
	private String get(int id) {
		return parameters.get(id);
	}
	
	public int getElementAsInt(int id) throws NumberFormatException  {
		return Integer.parseInt(get(id));
	}
	public double getElementAsDouble(int id) throws NumberFormatException  {
		return Double.parseDouble(get(id));
	}
	public float getElementAsFloat(int id) throws NumberFormatException  {
		return Float.parseFloat(get(id));
	}
	public String getElementAsString(int id) throws NumberFormatException  {
		return get(id);
	}
	public boolean getElementAsBoolean(int id) throws NumberFormatException  {
		return Boolean.parseBoolean(get(id));
	}
	
	public int parameterSize() {
		return parameters.size();
	}
	public List<String> getParameters() {
		return parameters;
	}
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
	public char getC() {
		return c;
	}
	public String getCommande() {
		return commande;
	}
	
	public String getAllParametersAsOneString() {
		int size = parameterSize();
		String res = "";
		for(int i=0; i < size;++i) {
			res+=parameters.get(i);
			if(i != size-1) {
				res+=" ";
			}
		}
		return res;
	}

	@Override
	public String toString() {
		String res = "Message [c=" + c + ", commande=" + commande + ", parameters=";
		for(int i=0;i<parameterSize();++i) {
			res+= parameters.get(i);
			if(i!=parameterSize()-1) {
				res+=", ";
			}
		}
		res+= " ]";
		return res;
	}
}
