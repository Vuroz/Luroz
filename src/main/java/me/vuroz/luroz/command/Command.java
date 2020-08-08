package me.vuroz.luroz.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
	private boolean guildOnly;
	private String name, help, description;
	private String[] usage;
	
	public Command(boolean guildOnly, String name, String help, String description, String[] usage) {
		this.guildOnly = guildOnly;
		this.name = name;
		this.help = help;
		this.description = description;
		this.usage = usage;
	}
	
	public abstract String execute(MessageReceivedEvent event, String[] arguments);
	
	public boolean isGuildOnly() {
		return guildOnly;
	}
	
	public String getName() {
		return name;
	}
	
	public String getHelp() {
		return help;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String[] getUsage() {
		return usage;
	}

}
