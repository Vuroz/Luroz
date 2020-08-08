package me.vuroz.luroz.command;

import java.io.IOException;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import me.vuroz.luroz.Misc;
import me.vuroz.luroz.manager.GuildManager;
import me.vuroz.luroz.manager.LogManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Wikipedia extends Command {

	private static String[] usage = { "<subject>" };
	
	public Wikipedia() {
		super(false, "Wikipedia", "Retrieves wikipedia information", "Searches wikipedia for the given subject", usage);
	}

	@Override
	public String execute(MessageReceivedEvent event, String[] arguments) {
		if (arguments.length >= 1) {
			event.getChannel().sendTyping().queue(); // Letting the user know Luroz has recieved the command
			
			String link = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=" + String.join(" ", arguments);
			
			String content;
			try {
				content = Jsoup.connect(link).ignoreContentType(true).timeout(5000).get().body().text();
			} catch (IOException e) {
				LogManager.log(Lyrics.class, 3, e.getMessage());
				return e.getMessage();
			}
			
			JSONObject json = new JSONObject(content);
			
			
			JSONObject pages = json.getJSONObject("query").getJSONObject("pages");
			
			if (pages.has("-1")) {
				event.getChannel().sendMessage("\u26A0 Wikipedia does not seem to have a page for `" + String.join(" ", arguments) + "`").queue();
				return null;
			}
			
			JSONObject page = null;
			
			for (String key : pages.keySet()) {
				page = pages.getJSONObject(key);
			}

			if (event.isFromGuild()) {
				boolean privateWiki = new GuildManager(event.getGuild()).isPrivateWiki();
				if (privateWiki) {
					event.getAuthor().openPrivateChannel().queue(channel -> {
						JSONObject pagePriv = null;
						for (String key : pages.keySet()) {
							pagePriv = pages.getJSONObject(key);
						}
						sendResult(event, channel, pagePriv, true);
					});
				} else {
					sendResult(event, event.getChannel(), page, false);
				}
			} else {
				sendResult(event, event.getChannel(), page, false);
			}
		} else {
			event.getChannel().sendMessage("\u26A0 The syntax for this command is: `wikipedia <subject>`").queue();
		}
		return null;
	}
	
	private static void sendResult(MessageReceivedEvent event, MessageChannel channel, JSONObject page, boolean sendAlert) {
		String parts[] = page.getString("extract").split("\\n");
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(page.getString("title"));
		channel.sendMessage(embed.build()).queue(msg -> {
			for (String part : parts) {
				if (part.equals(""))
					continue;
				String[] split = Misc.splitStringEvery(part, 2000);
				for (String bit : split) {
					EmbedBuilder embedBit = new EmbedBuilder();
					embedBit.setDescription(bit);
					channel.sendMessage(embedBit.build()).queue();
				}
			}
			
			if (sendAlert) {
				event.getChannel().sendMessage("\uD83D\uDCE8 Information sent, check your DMs").queue();
			}
		});
	}

}
