package me.vuroz.luroz.listener;

import java.util.Arrays;

import me.vuroz.luroz.Luroz;
import me.vuroz.luroz.command.Birb;
import me.vuroz.luroz.command.Cat;
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
import me.vuroz.luroz.manager.CommandManager;
import me.vuroz.luroz.manager.GuildManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String prefix;
		if (event.isFromGuild()) {
			prefix = new GuildManager(event.getGuild()).getPrefix();
		} else {
			prefix = Luroz.getDefaultPrefix();
		}
		
		String content = event.getMessage().getContentRaw();
		if (content.startsWith(prefix)) {
			String[] message = content.substring(prefix.length()).split(" ");
			String command = message[0].toLowerCase();
			String[] arguments = Arrays.copyOfRange(message, 1, message.length);
			switch(command) {
			case "birb":
			case "bird":
			case "borb":
			case "bord":
			case "parrot":
				CommandManager.execute(event, new Birb(), arguments);
				break;
			case "cat":
				CommandManager.execute(event, new Cat(), arguments);
				break;
			case "dog":
				CommandManager.execute(event, new Dog(), arguments);
				break;
			case "quote":
			case "wisdom":
			case "fortune":
				CommandManager.execute(event, new Fortune(), arguments);
				break;
			case "help":
				CommandManager.execute(event, new Help(), arguments);
				break;
			case "invite":
				CommandManager.execute(event, new Invite(), arguments);
				break;
			case "lyrics":
				CommandManager.execute(event, new Lyrics(), arguments);
				break;
			case "ping":
				CommandManager.execute(event, new Ping(), arguments);
				break;
			case "prefix":
			case "rmcmd":
			case "rmcmds":
			case "removecmd":
			case "removecmds":
			case "removecommand":
			case "removecommands":
				String[] args = (command + " " + String.join(" ", arguments)).split(" ");
				CommandManager.execute(event, new Settings(), args);
				break;
			case "cfg":
			case "config":
			case "setting":
			case "settings":
			case "preference":
			case "preferences":
				CommandManager.execute(event, new Settings(), arguments);
				break;
			case "style":
				CommandManager.execute(event, new Style(), arguments);
				break;
			case "wasted":
				CommandManager.execute(event, new Wasted(), arguments);
				break;
			case "wiki":
			case "wikipedia":
				CommandManager.execute(event, new Wikipedia(), arguments);
				break;
			}
		}
	}
}
