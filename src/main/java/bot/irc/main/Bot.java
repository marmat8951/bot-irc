package bot.irc.main;

import java.util.List;

public interface Bot {

	public void onMessage(String channel, String sender,
			String login, String hostname, String message);
	
	public void sendMessage(String sender,String channel,String message);
	
	public void sendMessages(String sender, String channel,List<String> messages);
	
	public void sendMessages(String sender, String channel, AffichableSurIRC affichable);
	
	public String getBotName();

	public void sendMessageToAdmins(String string);
	
	public void sendRSSMessage(List<String> messages);

	
}