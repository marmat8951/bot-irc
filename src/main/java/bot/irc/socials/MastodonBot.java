package bot.irc.socials;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.sys1yagi.mastodon4j.MastodonClient;
import com.sys1yagi.mastodon4j.api.Handler;
import com.sys1yagi.mastodon4j.api.entity.Notification;
import com.sys1yagi.mastodon4j.api.entity.Status;

import okhttp3.OkHttpClient;


public class MastodonBot {
	MastodonClient client;
	String accessToken="";
	public MastodonBot() {
		 client = new MastodonClient.Builder("mstdn.jp", new OkHttpClient.Builder(), new Gson())
		        .accessToken(accessToken)
		        .useStreamingApi()
		        .build();
		 
		Handler handler = new Handler() {
		    @Override
		    public void onStatus(@NotNull Status status) {
		        System.out.println(status.getContent());
		    }

			@Override
			public void onDelete(long arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNotification(Notification arg0) {
				// TODO Auto-generated method stub
				
			}

			
		};

	}

}
