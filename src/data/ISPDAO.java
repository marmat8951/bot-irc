package data;

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

import org.json.JSONObject;

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

		for (int i=0; i<nbItems; ++i) {
			ISP isp = getISP(i);
			if(isp != null) {
				ar.add(isp);
			}
		}
		return ar;

	}


	public ISP getISP(int number) {
		String json = executeGet(dbAdress+number+'/');
		System.out.println(json);
		JSONObject jsonObj = new JSONObject(json);
		String name = jsonObj.getJSONObject("ispformat").getString("name");
		int id = jsonObj.getInt("id");
		boolean member = jsonObj.getBoolean("is_ffdn_member");
		ISPdata ispData = new ISPdata(jsonObj.getJSONObject("ispformat"));
		String date_added = jsonObj.getString("date_added");
		String last_update = jsonObj.getString("last_update");
		ISP isp = new ISP(name, id, member, date_added, last_update, ispData);
		return isp;

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
