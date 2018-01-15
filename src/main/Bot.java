package main;

import org.jibble.pircbot.PircBot;

public class Bot extends PircBot {

	public Bot() {
		this.setName("UneFede2");
	}

	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		if (message.equalsIgnoreCase("time?")) {
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now " + time);
		}
	}
}
