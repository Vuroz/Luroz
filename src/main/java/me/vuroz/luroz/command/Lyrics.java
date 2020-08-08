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

public class Lyrics extends Command {
	
	private static String[] usage = { "<song name>" };

	public Lyrics() {
		super(false, "Lyrics", "View lyrics", "Searches genius for song and sends lyrics", usage);
	}

	@Override
	public String execute(MessageReceivedEvent event, String[] arguments) {
		if (arguments.length >= 1) {
			event.getChannel().sendTyping().queue(); // Letting the user know Luroz has recieved the command
			
			String link = "https://some-random-api.ml/lyrics?title=" + String.join(" ", arguments);
			
			String content;
			try {
				content = Jsoup.connect(link).ignoreContentType(true).timeout(5000).get().body().text();
			} catch (IOException e) {
				LogManager.log(Lyrics.class, 3, e.getMessage());
				return e.getMessage();
			}
			
			JSONObject json = new JSONObject(content);
			
			if (json.has("error")) {
				event.getChannel().sendMessage("\u26A0 Could not find a song named `" + String.join(" ", arguments) + "`").queue();
				return null;
			}
			
			if (event.isFromGuild()) {
				boolean privateLyrics = new GuildManager(event.getGuild()).isPrivateLyrics();
				if (privateLyrics) {
					event.getAuthor().openPrivateChannel().queue(channel -> {
						sendLyrics(event, channel, json, true);
					});
				} else {
					sendLyrics(event, event.getChannel(), json, false);
				}
			} else {
				sendLyrics(event, event.getChannel(), json, false);
			}
		} else {
			event.getChannel().sendMessage("\u26A0 The syntax for this command is: `lyrics <song name>`").queue();
		}
		return null;
	}
	
	private static void sendLyrics(MessageReceivedEvent event, MessageChannel channel, JSONObject json, boolean sendAlert) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(json.getString("title"), json.getJSONObject("links").getString("genius"));
		embed.setAuthor(json.getString("author"));
		embed.setThumbnail(json.getJSONObject("thumbnail").getString("genius"));
		
		String[] parts = json.getString("lyrics").split("\\n\\n");
		channel.sendMessage(embed.build()).queue(msg -> {
			for (String part : parts) {
				if (part.equals(""))
					continue;
				String[] split = Misc.splitStringEvery(part, 2000);
				for (String bit : split) {
					EmbedBuilder lyrics = new EmbedBuilder();
					lyrics.setDescription(bit);
					channel.sendMessage(lyrics.build()).queue();
				}
			}
			
			if (sendAlert) {
				event.getChannel().sendMessage("\uD83D\uDCE8 Song lyrics sent, check your DMs").queue();
			}
		});
	}

}
