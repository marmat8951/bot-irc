package data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.Main;

public class ISPDAO {

	/**
	 * This class implements the Sigleton Design Patern and is the DataAcess Object. It's role is to get informations from <a href="db.ffdn.org"> db.ffdn.org </a> and transform the into data Objects.
	 */

	public static volatile ISPDAO instance = null;
	private final String dbAdress = "https://db.ffdn.org/api/v1/isp/";


	private ISPDAO() {

	}

	public final static ISPDAO getInstance() {
		if (ISPDAO.instance == null) {
			synchronized (ISPDAO.class) {
				if(ISPDAO.instance == null) {
					ISPDAO.instance = new ISPDAO();
				}
			}
		}
		return ISPDAO.instance;
	}

	private String executeGet(String https_url) {
		return executeGet(https_url, "", 443);
	}
	
	@SuppressWarnings("resource")
	private String executeGet(final String https_url, final String proxyName, final int port) {
	    String ret = "";

	    URL url;
	    Scanner s=null;
	    try {

	        HttpsURLConnection con;
	        url = new URL(https_url);

	        if (proxyName.isEmpty()) {  
	            con = (HttpsURLConnection) url.openConnection();
	        } else {                
	            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyName, port));
	            con = (HttpsURLConnection) url.openConnection(proxy);
	            Authenticator authenticator = new Authenticator() {
	                public PasswordAuthentication getPasswordAuthentication() {
	                        return (new PasswordAuthentication(null, null));
	                    }
	                };
	            Authenticator.setDefault(authenticator);
	        }

	        InputStream is = (InputStream) con.getContent();
	        s = new java.util.Scanner(is).useDelimiter("\\A");
	        ret+= s.hasNext() ? s.next() : "";
	      

	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (FileNotFoundException fnfe) { // IF 404
	    	return null;
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 
	    if(s!=null) {
	    	s.close();
	    }
	    return ret;
	}




	/**
	 * 
	 * @return List of ISPs constructed.
	 */
	public List<ISP> getISPs() throws Exception{
		
		String jsonInfo = executeGet(dbAdress+"?per_page=0");
		JSONObject allISPCount = new JSONObject(jsonInfo);
		int nbItems = allISPCount.getInt("total_items");
		ArrayList<ISP> ar = new ArrayList<>(nbItems);
		int i=0;
		int increm=0;
		final int MAX_FAILS=5;
		int fails=0;
		while(i<nbItems && fails < MAX_FAILS) {
			ISP isp = getISP(increm);
			if(isp != null) {
				ar.add(isp);
				i++;
				fails=0;
			}else {
				fails++;
			}
			increm++;
		}
		return ar;

	}


	public ISP getISP(int number) {
		String json = executeGet(dbAdress+number+'/');
		if(json != null) {
			if(Main.isDebug()) {
				System.out.println("JSON du FAI "+number);
		System.out.println(json);
			}
		JSONObject jsonObj = new JSONObject(json);
		String name = getName(jsonObj);
		int id = getId(jsonObj);
		boolean member = getIsMember(jsonObj);
		ISPdata ispData = new ISPdata(jsonObj.getJSONObject("ispformat"));
		String date_added = getDateAdded(jsonObj);
		String last_update = getDateUpdated(jsonObj);
		CoveredAreas[] ca = getCoveredAreas(jsonObj);
		ISP isp = new ISP(name, id, member, date_added, last_update, ispData,ca);
		return isp;
		}else {
			if(Main.isDebug()) {
				System.out.println("Pas de JSON pour le FAI "+number);
			}
			
			return null;
		}

	}
	/**
	 * Recupere le nom de l'ISP dans le JSON
	 * @param json Objet JSON généré a partir de la requete principale
	 * @return Le nom si displonible ou null
	 */
	private String getName(JSONObject json) {
		try {
		return json.getJSONObject("ispformat").getString("name");
		}catch(JSONException jo) {
			System.err.println("Erreur au moment de récuperer le champ nom dans le JSON : ");
			jo.printStackTrace();
			return null;
		}
	}
	
	private int getId(JSONObject json) {
		try {
			return json.getInt("id");
		}catch(JSONException jo) {
			System.err.println("Erreur au moment de récuperer le champ id dans le JSON : ");
			jo.printStackTrace();
			return -1;
		}
	}
	
	private Boolean getIsMember(JSONObject json) {
		try {
			return json.getBoolean("is_ffdn_member");
		}catch(JSONException jo) {
			System.err.println("Erreur au moment de récuperer le champ is_ffdn_member dans le JSON : ");
			jo.printStackTrace();
			return null;
		}
	}
	
	private String getDateAdded(JSONObject json) {
		String res ="";
		try {
			
			res= json.getString("date_added");
		
		}catch(JSONException jo) {
			System.err.println("Erreur au moment de récuperer le champ date_added dans le JSON : ");
			jo.printStackTrace();
			
		}
		return res;
	}
	/**
	 * 
	 * @param json Objet json a l'origine
	 * @return chaine representant la date sous forme string:ISO8601 ou nu
	 */
	private String getDateUpdated(JSONObject json) {
		String res = "";
		try {
			
			res = json.getString("last_update");
			
		}catch(JSONException jo) {
			//System.err.println("Erreur au moment de récuperer le champ date_added dans le JSON : ");
			//jo.printStackTrace();
			
		}
		return res;
	}
	
	private CoveredAreas [] getCoveredAreas(JSONObject json) {
		CoveredAreas [] res=null;
		try {
			JSONArray ja = json.getJSONArray("coveredAreas");
			res = new CoveredAreas [ja.length()];
			for(int i=0;i<ja.length();i++) {
				JSONObject jo = ja.getJSONObject(i);
				JSONArray tech= jo.getJSONArray("technologies");
				String name = jo.getString("name");
				List<TechnoCoverage> technos = new ArrayList<>();
				for(int j=0;j<tech.length();j++) {
					technos.add(TechnoCoverage.getFromString(tech.getString(j)));
				}
				res[i]=new CoveredAreas(name, technos);				
				
				
			}
			return res;
		}catch(JSONException jo) {
			return null;
		}
		
	}
	
	
	/**
	 *TODO : implements this in Isp
	 * @param date String correspondant a une date au format aaaa-mm-jj ou aaaa-mm
	 * @return	Date correspondant
	 */
	private Date getDate(String date) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5,7))-1, Integer.parseInt(date.substring(8))); // le -1 est présent ici car Janvier correspond à 0 
		Date d = c.getTime();
		return d;
	}
	
	
	

}
