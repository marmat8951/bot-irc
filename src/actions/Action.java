package actions;

import java.util.List;

import org.jibble.pircbot.PircBot;

public abstract class Action {

	public List<String> keyWords;
	public PircBot bot;
	
	public abstract void react(String channel, String sender,
			String login, String hostname, String message);
	
	
	public boolean hasToReact(String s) {
		String chaineLowerCase = s.toLowerCase();
		for(String kw : keyWords) {
			if(chaineLowerCase.contains(kw.toLowerCase())){
				return true;
			}
		}
		return false;
		
	}
	

}
