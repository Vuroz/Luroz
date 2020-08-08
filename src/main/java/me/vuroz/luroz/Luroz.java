package me.vuroz.luroz;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.TreeMap;

import javax.security.auth.login.LoginException;

import org.json.JSONObject;

import me.vuroz.luroz.command.Birb;
import me.vuroz.luroz.command.Cat;
import me.vuroz.luroz.command.Command;
import me.vuroz.luroz.command.Dog;
import me.vuroz.luroz.command.Fortune;
import me.vuroz.luroz.command.Help;
import me.vuroz.luroz.command.Invite;
import me.vuroz.luroz.command.Lyrics;
import me.vuroz.luroz.command.Ping;
import me.vuroz.luroz.command.Settings;
import me.vuroz.luroz.command.Style;
import me.vuroz.luroz.command.Wasted;
import me.vuroz.luroz.command.Wikipedia;
import me.vuroz.luroz.listener.GuildJoinListener;
import me.vuroz.luroz.listener.MessageListener;
import me.vuroz.luroz.listener.ReadyListener;
import me.vuroz.luroz.manager.LogManager;
import me.vuroz.luroz.manager.TerminalHandler;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class Luroz {

	private static JSONObject config;

	public static Map<String, Command> commands = new TreeMap<String, Command>();

	private Luroz() throws LoginException {
		JDABuilder.createDefault(getToken())
				.addEventListeners(new ReadyListener(), new GuildJoinListener(), new MessageListener(), new TerminalHandler())
				.setActivity(Activity.watching("you"))
				.build();
	}

	public static void main(String[] args) throws LoginException, IOException {
		File guilds = new File("guild");
		if (!guilds.exists()) {
			LogManager.log(Luroz.class, 1, "Creating guild directory");
			guilds.mkdir();
		}

		File configFile = new File("config.json");
		String configContent = new String(Files.readAllBytes(configFile.toPath()));
		config = new JSONObject(configContent);
		
		Cat.apiKey = config.getString("catKey");
		
		LogManager.log(Luroz.class, 1, "Initializing letter styles");
		StyleList.initialize();

		commands.put("birb", new Birb());
		commands.put("cat", new Cat());
		commands.put("dog", new Dog());
		commands.put("fortune", new Fortune());
		commands.put("help", new Help());
		commands.put("invite", new Invite());
		commands.put("lyrics", new Lyrics());
		commands.put("ping", new Ping());
		commands.put("settings", new Settings());
		commands.put("style", new Style());
		commands.put("wasted", new Wasted());
		commands.put("wikipedia", new Wikipedia());
		
		new Luroz();
		
		TerminalHandler.listenAndExecute();
	}

	private static String getToken() {
		return config.getString("token");
	}

	public static String getDefaultPrefix() {
		return config.getString("defaultPrefix");
	}

}
