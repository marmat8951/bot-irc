package actions;

import java.util.ArrayList;
import java.util.List;

import main.Bot;

public class Help extends Action {

	public Help(Bot b) {
		super(b);
		List<String> kw = new ArrayList<>(3);
		kw.add("help");
		kw.add("?");
	}

	@Override
	public void react(String channel, String sender, String login, String hostname, String message) {
		// TODO Auto-generated method stub
		
	}
}
