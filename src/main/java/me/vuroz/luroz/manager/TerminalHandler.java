package me.vuroz.luroz.manager;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.user.UserTypingEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TerminalHandler extends ListenerAdapter {

	public static JDA jda;

	private static Scanner scanner = new Scanner(System.in);

	public static void listenAndExecute() {
		try {
			String[] line = scanner.nextLine().split(" ");
			String command = line[0].toLowerCase();
			String[] arguments = Arrays.copyOfRange(line, 1, line.length);

			switch (command) {
			case "guild":
			case "server":
				guild(arguments);
				break;
			case "send":
				send(arguments);
				break;
			case "user":
				user(arguments);
				break;
			case "watch":
				watch(arguments);
				break;
			default:
				LogManager.log(TerminalHandler.class, 3, "'{}' is not a valid command", command);
				break;
			}
			listenAndExecute();
		} catch (Exception e) {
			LogManager.log(TerminalHandler.class, 3, e.getMessage());
			LogManager.log(TerminalHandler.class, 2, "Console reader broken, restart to fix.");
		}
	}
	
	private static void guild(String[] arguments) {
		TextChannel channel = jda.getTextChannelById(arguments[0]);
		channel.sendMessage(String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length))).queue(
		success -> {
			LogManager.log(TerminalHandler.class, 1, "Message sent to {}", arguments[0]);
		},
		error -> {
			LogManager.log(TerminalHandler.class, 3, error.getMessage());
		});
	}

	private static void user(String[] arguments) {
		jda.openPrivateChannelById(arguments[0]).queue(channel -> {
			channel.sendMessage(String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length))).queue(
			success -> {
				LogManager.log(TerminalHandler.class, 1, "Message sent to {}", arguments[0]);
			}, error -> {
				LogManager.log(TerminalHandler.class, 3, error.getMessage());
			});
		});
	}
	
	private static void send(String[] arguments) {
		String[] channelArray = { watchChannel };
		String[] args = Stream.concat(Arrays.stream(channelArray), Arrays.stream(arguments)).toArray(String[]::new);
		if (watchType.equals("user")) {
			user(args);
		} else if (watchType.equals("guild")) {
			guild(args);
		}
	}

	private static String watchType = "user";
	private static String watchChannel = "363398068164362241"; // Watch bot creator when first starting

	private static void watch(String[] arguments) {
		if (arguments[0].equals("user")) {
			watchType = "user";
			watchChannel = arguments[1];
			LogManager.log(TerminalHandler.class, 1, "Now watching {}", arguments[1]);
		} else if (arguments[0].equals("guild")) {
			watchType = "guild";
			watchChannel = arguments[1];
			LogManager.log(TerminalHandler.class, 1, "Now watching {}", arguments[1]);
		} else {
			LogManager.log(TerminalHandler.class, 3, "'{}' is not a valid channel type", arguments[0]);
		}
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (watchType.equals("user")) {
			if (event.getAuthor().getId().equals(watchChannel)) {
				LogManager.log(TerminalHandler.class, 1, "{}: {}", event.getAuthor().getName(), event.getMessage().getContentRaw());
			}
		} else if (watchType.equals("guild")) {
			if (event.getChannel().getId().equals(watchChannel) && !event.getAuthor().getId().equals(jda.getSelfUser().getId())) {
				LogManager.log(TerminalHandler.class, 1, "{}: {}", event.getAuthor().getName(), event.getMessage().getContentRaw());
			}
		}
	}

	@Override
	public void onUserTyping(UserTypingEvent event) {
		if (watchType.equals("user")) {
			if (event.getUser().getId().equals(watchChannel)) {
				LogManager.log(TerminalHandler.class, 1, "{} is typing...", event.getUser().getName());
			}
		} else if (watchType.equals("guild")) {
			if (event.getChannel().getId().equals(watchChannel)) {
				LogManager.log(TerminalHandler.class, 1, "{} is typing in channel '{}' in guild '{}'", event.getUser().getName(), event.getChannel().getName(), event.getGuild().getName());
			}
		}
	}

}
