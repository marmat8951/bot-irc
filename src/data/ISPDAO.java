package data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

	/**
	 * @param urlToRead URL the method will transform into a String
	 * @return	String that the server Respond with the URL specified or null in case of an error
	 * 
	 */
	public static String getHTML(String urlToRead) {
		try {
			StringBuilder result = new StringBuilder();
			URL url = new URL(urlToRead);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			
			return result.toString();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	


	/**
	 * 
	 * @return List of ISPs constructed.
	 */
	public List<ISP> getISPs() throws Exception{
		String jsonInfo = getHTML(dbAdress+"?per_page=0");
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
		String json = getHTML(dbAdress+number);
		JSONObject jsonObj = new JSONObject(json);
		ISPData =
		
		String name =
		

		return null;

	}



}
