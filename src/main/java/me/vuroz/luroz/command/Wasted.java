package me.vuroz.luroz.command;

import java.io.IOException;

import org.jsoup.Jsoup;

import me.vuroz.luroz.manager.LogManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Wasted extends Command {
	
	private static String[] usage = { "", "<user>" };

	public Wasted() {
		super(false, "Wasted", "Wasted.", "Sends image with WASTED overlay of you/mentioned user", usage);
	}

	@Override
	public String execute(MessageReceivedEvent event, String[] arguments) {
		event.getChannel().sendTyping().queue();
		
		String link = "https://some-random-api.ml/canvas/wasted?avatar=";
		if (event.getMessage().getMentionedUsers().size() >= 1) {
			link += event.getMessage().getMentionedUsers().get(0).getEffectiveAvatarUrl();
		} else {
			link += event.getAuthor().getEffectiveAvatarUrl();
		}
		
		byte[] image;
		try {
			image = Jsoup.connect(link).ignoreContentType(true).timeout(5000).execute().bodyAsBytes();
		} catch (IOException e) {
			LogManager.log(Cat.class, 3, e.getMessage());
			return e.getMessage();
		}
		
		if (event.isFromGuild()) {
			if (event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_ATTACH_FILES)) {
				event.getChannel().sendFile(image, "wasted.png").queue();
			} else {
				event.getChannel().sendMessage("\u26A0 I don't have permission to send images in this channel.").queue();
			}
		} else {
			event.getChannel().sendFile(image, "wasted.png").queue();
		}
		return null;
	}

}
