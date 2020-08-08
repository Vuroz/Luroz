package me.vuroz.luroz.command;

import java.io.IOException;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import me.vuroz.luroz.manager.LogManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Cat extends Command {

	private static String[] usage = { "" };
	
	public static String apiKey;

	public Cat() {
		super(false, "Cat", "Random cat", "Sends a random image of a cat!", usage);
	}

	@Override
	public String execute(MessageReceivedEvent event, String[] arguments) {
		event.getChannel().sendTyping().queue(); // Letting the user know Luroz has recieved the command
		String json = "";
		try {
			json = Jsoup.connect("https://api.thecatapi.com/v1/images/search")
					.header("x-api-key", apiKey)
					.ignoreContentType(true)
					.timeout(5000)
					.get().body().text();
		} catch (IOException e) {
			LogManager.log(Cat.class, 3, e.getMessage());
			event.getChannel().sendMessage("\uD83D\uDE2D The cat generator seems to be down.").queue();
			return null;
		}
		
		json = json.substring(1, json.length() - 1);
		
		String image = new JSONObject(json).getString("url");
		event.getChannel().sendMessage(image).queue();
		return null;
	}

}
