package me.vuroz.luroz.command;

import java.util.Map;
import java.util.Random;

import me.vuroz.luroz.StyleList;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Style extends Command {
	
	private static Map<String, Map<Character, String>> styles = StyleList.styles;
	
	private static String[] usage = {"<style> <your text>"};

	public Style() {
		super(false, "Style", "Style your text", "Retypes your text with your preferred style\nAvailable styles are:\n"
				+ "`circle` -> " + stylize(styles.get("circle"), "circle") + "\n"
				+ "`leetspeek` -> " + stylize(styles.get("leetspeek"), "leetspeek") + "\n"
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
			case "1337":
			case "l33t":
			case "leet":
			case "eleet":
			case "leetspeak":
			case "leetspeek":
				text = stylize(styles.get("leetspeek"), text);
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
		if (style == styles.get("leetspeek")) {
			String newText = "";
			String[] words = text.split(" ");
			for (String word : words) {
				if (word.equalsIgnoreCase("what")) {
					char w = word.charAt(0);
					char t = word.charAt(3);
					
					boolean capital = Character.isUpperCase(t) ? true : false;
					
					char[] middles = { 'a', 'o', 'u' };
					Random random = new Random();
					char middle = middles[random.nextInt(3)];
					
					if (capital)
						middle = Character.toUpperCase(middle);
					
					word = w + middle + "t";
				}
				
				if (word.startsWith("own")) {
					word = "pwn" + word.substring(3);
				}
				
				word = word.endsWith("and") || word.endsWith("ant") ? replaceLast(word, "&", 3) : word.endsWith("anned") ? replaceLast(word, "&", 5) : word;				
				word = word.endsWith("cker") ? replaceLast(word, "xor", 4) : word;
				
				if (word.endsWith("er") || word.endsWith("or")) {
					if (word.equalsIgnoreCase("or"))
						continue;
					word = replaceLast(word, "xor", 2);
				}
				
				newText += word + " ";
			}
			text = newText;
		}
		
		for (Map.Entry<Character, String> entry : style.entrySet()) {
			char letter = entry.getKey();
			String replacement = entry.getValue();
			text = text.replace(Character.toString(letter), replacement);
		}
		return text;
	}
	
	private static String replaceLast(String original, String replacement, int removalLength) {
		return original.substring(0, original.length() - removalLength) + replacement;
	}

}
