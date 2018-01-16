package main;

import java.util.List;

import org.jibble.pircbot.PircBot;

import data.ISPDAO;

public class Bot extends PircBot {

	private ISPDAO idao;
	
	public Bot() {
		this.setName("UneFede2");
		idao = ISPDAO.getInstance();
	}

	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		if (message.equalsIgnoreCase("time?")) {
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now " + time);
		}
		
		if (message.contains("+info")) {
			
			int  id = Integer.parseInt(message.substring(message.indexOf(' ')+1));
			List<String> s = idao.getISP(id).toStringIRC();
			for(String response : s) {
				sendMessage(channel,response);
			}
			
		}
		
		//easter Egg
		if (message.contains("Ehlo UneFede")) {
			sendMessage(channel, "Ehlo "+sender+"!!");
		}
		
	}
}
