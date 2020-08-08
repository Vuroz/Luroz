package me.vuroz.luroz.command;

import me.vuroz.luroz.manager.GuildManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Settings extends Command {

	private static String[] usage = { "", "prefix", "prefix <prefix>", "privateHelp", "privateHelp <true/false>", "privateLyrics", "privateLyrics <true/false>", "privateWiki", "privateWiki <true/false>", "removeCommand", "removeCommand <true/false>" };

	public Settings() {
		super(true, "Settings", "View/edit settings for server",
				"Allows you to view/edit the settings for this server, Administrator permission is required to run this command.",
				usage);
	}

	@Override
	public String execute(MessageReceivedEvent event, String[] arguments) {
		if (arguments.length >= 1) {
			switch (arguments[0].toLowerCase()) {
			case "prefix":
				if (arguments.length == 1) {
					String prefix = new GuildManager(event.getGuild()).getPrefix();
					event.getChannel().sendMessage("\u2139 The prefix in this server is: `" + prefix + "`").queue();
				} else {
					if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR) || event.getAuthor().getId().equals("363398068164362241")) {
						if (!(arguments.length >= 3)) {
							new GuildManager(event.getGuild()).setPrefix(arguments[1]);
							event.getChannel().sendMessage("\uD83D\uDD27 The prefix in this server changed to `" + arguments[1] + "`").queue();
						} else {
							event.getChannel().sendMessage("\u26A0 Prefix cannot contain spaces.").queue();
						}
					} else {
						event.getChannel().sendMessage("\u26D4 You need administrator permission to change settings.").queue();
					}
				}
				break;
			case "phelp":
			case "privhelp":
			case "privatehelp":
				if (arguments.length == 1) {
					boolean privateHelp = new GuildManager(event.getGuild()).isPrivateHelp();
					event.getChannel().sendMessage("\u2139 Private help: `" + privateHelp + "`").queue();
				} else {
					setSettingBoolean(event, "privateHelp", arguments);
				}
				break;
			case "plyr":
			case "plyrs":
			case "plyric":
			case "plyrics":
			case "privlyr":
			case "privlyrs":
			case "privlyric":
			case "privlyrics":
			case "privatelyr":
			case "privatelyrs":
			case "privatelyric":
			case "privatelyrics":
				if (arguments.length == 1) {
					boolean privateLyrics = new GuildManager(event.getGuild()).isPrivateLyrics();
					event.getChannel().sendMessage("\u2139 Private lyrics: `" + privateLyrics + "`").queue();
				} else {
					setSettingBoolean(event, "privateLyrics", arguments);
				}
				break;
			case "pwiki":
			case "privwiki":
			case "pwikipedia":
			case "privatewiki":
			case "privwikipedia":
			case "privatewikipedia":
				if (arguments.length == 1) {
					boolean privateWiki = new GuildManager(event.getGuild()).isPrivateWiki();
					event.getChannel().sendMessage("\u2139 Private wikipedia: `" + privateWiki + "`").queue();
				} else {
					setSettingBoolean(event, "privateWiki", arguments);
				}
				break;
			case "rmcmd":
			case "rmcmds":
			case "removecmd":
			case "removecmds":
			case "removecommand":
			case "removecommands":
				if (arguments.length == 1) {
					boolean removeCommand = new GuildManager(event.getGuild()).isRemoveCommand();
					event.getChannel().sendMessage("\u2139 Remove commands: `" + removeCommand + "`").queue();
				} else {
					setSettingBoolean(event, "removeCommand", arguments);
				}
				break;
			default:
				event.getChannel().sendMessage("\u26A0 `" + arguments[0] + "` is not a valid setting.").queue();
				break;
			}
		} else {
			GuildManager guildManager = new GuildManager(event.getGuild());
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Settings for " + event.getGuild().getName());
			embed.setAuthor("Luroz", "https://bit.ly/invite-luroz", event.getJDA().getSelfUser().getAvatarUrl());
			embed.setThumbnail(event.getGuild().getIconUrl());
			embed.addField("Prefix: `" + guildManager.getPrefix() + "`", "What is typed before a command", false);
			embed.addField("Private Help: `" + guildManager.isPrivateHelp() + "`", "If help list should be sent in private or in server", false);
			embed.addField("Private Lyrics: `" + guildManager.isPrivateLyrics() + "`", "If lyrics should be sent in private or in server", false);
			embed.addField("Private Wikipedia: `" + guildManager.isPrivateWiki() + "`", "If wikipedia result should be sent in private or in server", false);
			embed.addField("Remove Command: `" + guildManager.isRemoveCommand() + "`", "If this is enabled Luroz will remove the message containing the command", false);
			event.getChannel().sendMessage(embed.build()).queue();
		}
		return null;
	}
	
	private static void setSettingBoolean(MessageReceivedEvent event, String setting, String[] arguments) {
		if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR) || event.getAuthor().getId().equals("363398068164362241")) {
			switch (arguments[1].toLowerCase()) {
			case "y":
			case "yes":
			case "true":
				new GuildManager(event.getGuild()).setSetting(setting, true);;
				event.getChannel().sendMessage("\uD83D\uDD27 `" + setting + "` set to `true`").queue();
				break;
			case "n":
			case "no":
			case "false":
				new GuildManager(event.getGuild()).setSetting(setting, false);;
				event.getChannel().sendMessage("\uD83D\uDD27 `" + setting + "` set to `false`").queue();
				break;
			default:
				event.getChannel().sendMessage("\u26A0 The `" + setting + "` setting can only be set to `true` or `false`").queue();
				break;
			}
		} else {
			event.getChannel().sendMessage("\u26D4 You need administrator permission to change settings.").queue();
		}
	}

}
