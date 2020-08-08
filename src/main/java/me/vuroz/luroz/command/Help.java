package me.vuroz.luroz.command;

import java.util.Map;

import me.vuroz.luroz.Luroz;
import me.vuroz.luroz.Misc;
import me.vuroz.luroz.manager.GuildManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Help extends Command {

	private static Map<String, Command> commands = Luroz.commands;

	private static String[] usage = { "", "<command>" };

	public Help() {
		super(false, "Help", "View help", "Shows information about commands, type command as argument to view for more details", usage);
	}

	@Override
	public String execute(MessageReceivedEvent event, String[] arguments) {
		if (arguments.length >= 1) {
			String cmd = arguments[0].toLowerCase();
			Command command = commands.get(cmd);
			if (command != null) {
				EmbedBuilder embed = new EmbedBuilder();
				embed.setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl());
				embed.setFooter("Luroz", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
				embed.setTitle(Misc.capitalize(cmd));
				embed.setDescription(command.getDescription());
				String usages = "";
				for (String usage : command.getUsage()) {
					usages += cmd + " " + usage + "\n";
				}
				embed.addField("Usage", usages, false);
				event.getChannel().sendMessage(embed.build()).queue();
			} else {
				event.getChannel().sendMessageFormat("\u26A0 `%s` is not a command.", arguments[0]).queue();
			}
		} else {
			if (event.isFromGuild()) {
				boolean privateHelp = new GuildManager(event.getGuild()).isPrivateHelp();
				if (privateHelp) {
					event.getAuthor().openPrivateChannel().queue(channel -> {
						sendHelp(event, channel, true);
					});
				} else {
					sendHelp(event, event.getChannel(), false);
				}
			} else {
				sendHelp(event, event.getChannel(), false);
			}
		}
		return null;
	}
	
	private static void sendHelp(MessageReceivedEvent event, MessageChannel channel, boolean sendAlert) {
		int longest = 9;
		
		String content = "**Commands:**\n";
		
		for (Map.Entry<String, Command> entry : commands.entrySet()) {
			String name = entry.getKey();
			Command command = entry.getValue();
			content += "`" + Misc.fixedLength(name, longest) + " | " + command.getHelp() + "`\n";
		}
		
		channel.sendMessage(content).queue();
		
		if (sendAlert) {
			event.getChannel().sendMessage("\uD83D\uDCE8 Help sent, check your DMs").queue();
		}
	}

}
