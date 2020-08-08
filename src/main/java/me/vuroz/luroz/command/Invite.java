package me.vuroz.luroz.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Invite extends Command {

	private static String[] usage = {""};
	
	public Invite() {
		super(false, "Invite", "Sends invite link", "Sends link that adds bot to server", usage);
	}

	@Override
	public String execute(MessageReceivedEvent event, String[] arguments) {
		event.getChannel().sendMessage("\u2B07 Use this link to invite Luroz\nhttps://bit.ly/invite-luroz").queue();
		return null;
	}

}
