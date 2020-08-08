package me.vuroz.luroz.command;

import java.util.Map;

import me.vuroz.luroz.StyleList;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Style extends Command {
	
	private static Map<String, Map<Character, String>> styles = StyleList.styles;
	
	private static String[] usage = {"<style> <your text>"};

	public Style() {
		super(false, "Style", "Style your text", "Retypes your text with your preferred style\nAvailable styles are:\n"
				+ "`circle` -> " + stylize(styles.get("circle"), "circle") + "\n"
				+ "`old` -> " + stylize(styles.get("fraktur"), "london") + "\n"
				+ "`retro` -> " + stylize(styles.get("retro"), "retro") + "\n"
				+ "`small` -> "  + stylize(styles.get("small"), "small") + "\n"
				+ "`square` -> "  + stylize(styles.get("square"), "square") + "\n", usage);
	}

	@Override
	public String execute(MessageReceivedEvent event, String[] arguments) {
		if (arguments.length >= 2) {
			String text = String.join(" ", arguments).substring(arguments[0].length());
			switch(arguments[0].toLowerCase()) {
			case "circle":
			case "circled":
				text = stylize(styles.get("circle"), text);
				break;
			case "old":
			case "london":
			case "fraktur":
				text = stylize(styles.get("fraktur"), text);
				break;
			case "80s":
			case "mono":
			case "retro":
			case "synth":
			case "monospace":
				text = stylize(styles.get("retro"), text);
				break;
			case "small":
				text = stylize(styles.get("small"), text);
			case "square":
			case "squared":
				text = stylize(styles.get("square"), text);
				break;
			default:
				text = "\u26A0 `" + arguments[0] + "` is not a valid style";
				break;
			}
			event.getChannel().sendMessage(text).queue();
		} else {
			event.getChannel().sendMessage("\u26A0 The syntax for this command is `style <style> <your text>`").queue();
		}
		return null;
	}
	
	private static String stylize(Map<Character, String> style, String text) {
		for (Map.Entry<Character, String> entry : style.entrySet()) {
			char letter = entry.getKey();
			String replacement = entry.getValue();
			text = text.replace(Character.toString(letter), replacement);
		}
		return text;
	}

}
