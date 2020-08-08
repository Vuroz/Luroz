package me.vuroz.luroz.command;

import java.io.IOException;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import me.vuroz.luroz.manager.LogManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Fortune extends Command {

	private static String[] usage = { "", "<category>" };
	
	public Fortune() {
		super(false, "Fortune", "Sends wisdom ;)", "Retrieves a random fortune cookie\nAvailable categories are:\n"
				+ "`all`\n"
				+ "`bible`\n"
				+ "`computers`\n"
				+ "`cookie`\n"
				+ "`definitions`\n"
				+ "`misc`\n"
				+ "`people`\n"
				+ "`platitudes`\n"
				+ "`politics`\n"
				+ "`science`\n"
				+ "`wisdom`", usage);
	}

	@Override
	public String execute(MessageReceivedEvent event, String[] arguments) {
		String category = "cookie";
		if (arguments.length >= 2) {
			switch(arguments[0].toLowerCase()) {
			case "all":
				category = "all";
				break;
			case "bible":
				category = "bible";
				break;
			case "computers":
				category = "computers";
				break;
			case "cookie":
				category = "cookie";
				break;
			case "definitions":
				category = "definitions";
				break;
			case "misc":
			case "miscellaneous":
				category = "miscellaneous";
				break;
			case "people":
				category = "people";
				break;
			case "platitudes":
				category = "platitudes";
				break;
			case "politics":
				category = "politics";
				break;
			case "science":
				category = "science";
				break;
			case "wisdom":
				category = "wisdom";
				break;
			}
		}
		event.getChannel().sendTyping().queue();
		String json = "";
		try {
			json = Jsoup.connect("http://yerkee.com/api/fortune/" + category).ignoreContentType(true).timeout(5000).get().body().text();
		} catch (IOException e) {
			LogManager.log(Fortune.class, 3, e.getMessage());
			event.getChannel().sendMessage("\uD83D\uDE2D The cookie supplier seems to be out of fortune cookies.").queue();
			return null;
		}
		String fortune = new JSONObject(json).getString("fortune");
		event.getChannel().sendMessage(fortune).queue();
		
		return null;
	}

}
