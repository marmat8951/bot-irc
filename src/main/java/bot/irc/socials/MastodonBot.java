package bot.irc.socials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.sys1yagi.mastodon4j.MastodonClient;
import com.sys1yagi.mastodon4j.Parameter;
import com.sys1yagi.mastodon4j.api.Handler;
import com.sys1yagi.mastodon4j.api.Shutdownable;
import com.sys1yagi.mastodon4j.api.entity.Account;
import com.sys1yagi.mastodon4j.api.entity.Mention;
import com.sys1yagi.mastodon4j.api.entity.Notification;
import com.sys1yagi.mastodon4j.api.entity.Status;
import com.sys1yagi.mastodon4j.api.exception.Mastodon4jRequestException;
import com.sys1yagi.mastodon4j.api.method.Streaming;

import bot.irc.main.AffichableSurIRC;
import bot.irc.main.Bot;
import bot.irc.main.Config;
import bot.irc.main.Main;
import bot.irc.rss.RssData;
import bot.irc.rss.RssDataRemainder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;


public class MastodonBot implements Bot, Runnable,Observer {
	private MastodonClient client;
	private String accessToken=Config.getProperty("Mastodon_access_token");
	private String BotName = "Mastodon";
	private Streaming streaming;
	private Thread thread;
	private long timeout;
	boolean end = false;
	private String lastDmId = null;
	private final String INSTANCE_ADRESS = Config.getProperty("Mastodon_instance");
	public  Handler handler = new Handler() {
	    
		@Override
	    public void onStatus(@NotNull Status status) {
	       // System.out.println(status.getContent());
	    }

		@Override
		public void onDelete(long arg0) {
			// TODO Auto-generated method stub	
		}

		@Override
		public void onNotification(@NotNull Notification arg0) {
			System.out.println(arg0.getType()+" | "+arg0.getStatus().getContent()+" | "+arg0.getStatus().getVisibility());
			if(arg0.getType().equals(Notification.Type.Mention.getValue()) && arg0.getStatus().getVisibility().equals("direct")) {
				System.out.println(arg0.getAccount().getUserName() + " | "+ arg0.getAccount().getAcct());
				sendMessage(arg0.getAccount().getId()+"", ""+arg0.getStatus().getId(), "testRep");
			}
		}
	};
	
	public MastodonBot() {
		this.init();
	}
	
	
	
	public MastodonBot(String accessTk,boolean streaming) {
		this();
		setAccessToken(accessTk);
	}
	
	private void init() {
		//TODO Refaire la gestion de l'activation ou non du streaming.
		client = new MastodonClient.Builder(INSTANCE_ADRESS, new OkHttpClient.Builder(), new Gson())
		        .accessToken(accessToken)
		        .useStreamingApi()
		        .build();
		streaming = new Streaming(client);
		try {
			streaming.user(handler);
		} catch (Mastodon4jRequestException e) {
			e.printStackTrace();
		}
	}
		
	
	
	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		
		init();
	}
	
	
	public void start() {
		System.out.println("Démarage du Bot Mastodon. Mise à jour toute les "+timeout+" secondes.");
		if(thread == null) {
			thread = new Thread(this, this.BotName);
			thread.start();
		}
	}
	
	
	
	public void run() {
		do {
			System.out.println("==== Mastodonbot: ====");
			Parameter requParma = new Parameter();
			if(lastDmId != null) {
				requParma.append("since_id", lastDmId);
			}
			System.out.println("URL: "+client.getBaseUrl());
			Response response = client.get("/timelines/direct", requParma);
			if(response.isSuccessful()) {
				System.out.println("sucess");
			}else {
				System.err.println("erreur");
			}
			try {
				treatResponse(response);
			}catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("==== FIN Mastodonbot ====");
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while(!end);
		
	}
	
	
	public void run2() {
		try {
			Shutdownable shutdownable = streaming.user(handler);
			Thread.sleep(10000L);
			shutdownable.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void treatResponse(Response r) throws IOException {
		JSONArray json = new JSONArray(r.body().string());
		int len = json.length();
		for(int i=0;i<len;++i) {
			JSONObject jo = json.getJSONObject(i);
			if(Config.isDebug()) {
				System.out.println("traitement de "+jo.toString());
			}
			lastDmId = jo.getString("id");
		}
	}
	
	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sendMessage(String sender, String channel, String message) {
	
		Response r = client.post("statuses", new RequestBody() {
			
			@Override
			public void writeTo(BufferedSink sink) throws IOException {
				JSONObject jo = new JSONObject();
				Response accSenderResponse = client.get("/api/v1/accounts/"+sender, null);
				if(accSenderResponse.isSuccessful()){
					twitter4j.JSONObject accSenderResponseObject = new twitter4j.JSONObject(accSenderResponse.body().string()); 
					List<Mention> mlist = new ArrayList<>(1);
					Mention m = new Mention(accSenderResponseObject.getString("url"), accSenderResponseObject.getString("username"),  accSenderResponseObject.getString("acct"), accSenderResponseObject.getLong("id"));
					mlist.add(m);
					jo.put("status", sender+" "+message);
					jo.put("in_reply_to_id", channel);
					jo.put("visibility", "direct");
					JSONArray mentionsArray = new JSONArray();
					jo.put("mentions", mlist);
					System.out.println("reply: "+jo.toString());
					sink.writeUtf8(jo.toString());
					sink.flush();
				}
				
			}
			
			@Override
			public MediaType contentType() {
				return MediaType.parse("application/json");
			}
		});
		if(Main.isDebug()) {
			System.out.println(r.networkResponse());
		}
	}
	
	@Override
	public void sendMessages(String sender, String channel, List<String> messages) {	
		String m="";
		for(String s : messages) {
			m=m+s+'\n';
		}
		if(!m.equals("")) {
			sendMessage(sender, channel, m);
		}
	}
	
	@Override
	public void sendMessages(String sender, String channel, AffichableSurIRC affichable) {
		sendMessages(sender, channel, affichable.toStringIRC());
	}
	
	@Override
	public String getBotName() {
		return BotName;
	}
	
	@Override
	public void sendMessageToAdmins(String string) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sendRSSMessage(List<String> messages) {
		
		
		Response r = client.post("statuses", new RequestBody() {
			
			@Override
			public void writeTo(BufferedSink sink) throws IOException {
				String aff = "";
				for(String s : messages) {
					aff += s+"\n";
				}
				JSONObject jo = new JSONObject();
					jo.put("status", aff);
					jo.put("visibility", "public");
					sink.writeUtf8(jo.toString());
					sink.flush();
				}
				
			
			
			@Override
			public MediaType contentType() {
				return MediaType.parse("application/json");
			}
		});
		if(Main.isDebug()) {
			System.out.println(r.networkResponse());
		}
		
		

		}



	@Override
	public void update(Observable o, Object arg) {
		if(o.getClass().equals(RssDataRemainder.class)) {
			RssData data = (RssData) arg;
			this.sendRSSMessage(data.toStringIRC());
		}
	}


	

}
