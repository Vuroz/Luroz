package me.vuroz.luroz.manager;

import me.vuroz.luroz.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandManager {

	public static void execute(MessageReceivedEvent event, Command command, String[] arguments) {
		LogManager.log(CommandManager.class, 1, "{} is executing command '{}'", event.getAuthor().getId(), command.getName());
		
		if (command.isGuildOnly() && !event.isFromGuild()) {
			LogManager.log(CommandManager.class, 1, "The requested command can only be executed from a guild, aborting.", command.getName());
			event.getChannel().sendMessage("\u26A0 That is a server-only command!").queue();
			return;
		}

		String error = command.execute(event, arguments);

		if (error == null) {
			if (event.isFromGuild()) {
				if (new GuildManager(event.getGuild()).isRemoveCommand()) {
					event.getMessage().delete().queue();
				}
			}
		} else {
			LogManager.log(CommandManager.class, 1, "There was an error while executing the command: {}", error);
			event.getChannel().sendMessage("\u26A0 There was an error while executing your command.").queue();
		}
	}

}
