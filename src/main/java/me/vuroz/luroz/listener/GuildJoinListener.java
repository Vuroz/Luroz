package me.vuroz.luroz.listener;

import me.vuroz.luroz.manager.GuildManager;
import me.vuroz.luroz.manager.LogManager;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildJoinListener extends ListenerAdapter {
	
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		LogManager.log(GuildJoinListener.class, 1, "Joined {}, checking file", event.getGuild().getId());
		GuildManager guildManager = new GuildManager(event.getGuild());
		guildManager.checkFile();
	}

}
