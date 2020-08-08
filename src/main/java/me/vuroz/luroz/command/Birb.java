package me.vuroz.luroz.command;

import java.io.IOException;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import me.vuroz.luroz.manager.LogManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Birb extends Command {

	private static String[] usage = { "" };
	
	public Birb() {
		super(false, "Birb", "Random birb", "Send a random image of a birb", usage);
	}

	@Override
	public String execute(MessageReceivedEvent event, String[] arguments) {
		event.getChannel().sendTyping().queue(); // Letting the user know Luroz has recieved the command
		String json = "";
		try {
			json = Jsoup.connect("http://random.birb.pw/tweet.json").ignoreContentType(true).timeout(5000).get().body().text();
		} catch (IOException e) {
			LogManager.log(Dog.class, 3, e.getMessage());
			event.getChannel().sendMessage("\uD83D\uDE2D The birb generator seems to be down.").queue();
			return null;
		}
		String image = new JSONObject(json).getString("file");
		event.getChannel().sendMessage("http://random.birb.pw/img/" + image).queue();
		return null;
	}

}
