package me.vuroz.luroz.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping extends Command {
	
	private static String[] usage = {""};

	public Ping() {
		super(false, "Ping", "Pong!", "Check latency between the bot and Discord.", usage);
	}

	@Override
	public String execute(MessageReceivedEvent event, String[] arguments) {
		event.getJDA().getRestPing().queue(time -> {
			event.getChannel().sendMessageFormat("Pong! \u23F1 %d ms", time).queue();
		});
		return null;
	}

}
