package me.vuroz.luroz.command;

import java.io.IOException;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import me.vuroz.luroz.manager.LogManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Dog extends Command {

	private static String[] usage = { "" };
	
	public Dog() {
		super(false, "Dog", "Random dog", "Sends a random image of a dog!", usage);
	}

	@Override
	public String execute(MessageReceivedEvent event, String[] arguments) {
		event.getChannel().sendTyping().queue(); // Letting the user know Luroz has recieved the command
		String json = "";
		try {
			json = Jsoup.connect("http://random.dog/woof.json").ignoreContentType(true).timeout(5000).get().body().text();
		} catch (IOException e) {
			LogManager.log(Dog.class, 3, e.getMessage());
			event.getChannel().sendMessage("\uD83D\uDE2D The dog generator seems to be down.").queue();
			return null;
		}
		String image = new JSONObject(json).getString("url");
		event.getChannel().sendMessage(image).queue();
		return null;
	}

}
