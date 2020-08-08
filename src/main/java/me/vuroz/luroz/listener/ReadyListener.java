package me.vuroz.luroz.listener;

import me.vuroz.luroz.manager.GuildManager;
import me.vuroz.luroz.manager.LogManager;
import me.vuroz.luroz.manager.TerminalHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {
	
	@Override
	public void onReady(ReadyEvent event) {
		JDA jda = event.getJDA();
		TerminalHandler.jda = jda;
		
		LogManager.log(ReadyListener.class, 1, "Checking if all guilds have a file...");
		
		for (Guild guild : jda.getGuilds()) {
			GuildManager guildManager = new GuildManager(guild);
			guildManager.checkFile();
		}
		
		LogManager.log(ReadyListener.class, 1, "Logged in as {}", jda.getSelfUser().getAsTag());
	}

}
