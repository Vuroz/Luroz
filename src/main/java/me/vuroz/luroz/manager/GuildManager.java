package me.vuroz.luroz.manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import org.json.JSONObject;

import me.vuroz.luroz.Luroz;
import net.dv8tion.jda.api.entities.Guild;

public class GuildManager {
	
	private Guild guild;
	
	public GuildManager(Guild guild) {
		this.guild = guild;
	}
	
	public void checkFile() {
		File file = new File("guild/" + guild.getId() + ".json");
		if (!file.exists()) {
			LogManager.log(GuildManager.class, 1, "Creating file for {}", guild.getId());
			
			JSONObject settings = new JSONObject();
			settings.put("prefix", Luroz.getDefaultPrefix());
			settings.put("privateHelp", true);
			settings.put("privateLyrics", true);
			settings.put("privateWiki", true);
			settings.put("removeCommand", false);
			
			JSONObject json = new JSONObject();
			json.put("settings", settings);
			
			setFile(json);
		}
	}
	
	private JSONObject getFile() {
		try {
			File file = new File("guild/" + guild.getId() + ".json");
			String fileContent = new String(Files.readAllBytes(file.toPath()));
			return new JSONObject(fileContent);
		} catch (IOException e) {
			LogManager.log(GuildManager.class, 3, e.getMessage());
		}
		return null;
	}
	
	private void setFile(JSONObject content) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(new File("guild/" + guild.getId() + ".json"));
			fileWriter.write(content.toString(4));
		} catch (IOException e) {
			LogManager.log(GuildManager.class, 3, e.getMessage());
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				LogManager.log(GuildManager.class, 3, e.getMessage());
			}
		}
	}
	
	private JSONObject getSettings() {
		JSONObject json = getFile();
		return json.getJSONObject("settings");
	}
	
	public void setSetting(String key, Object value) {
		LogManager.log(GuildManager.class, 1, "Changing {} in {} to {}", key, guild.getId(), value);
		
		JSONObject settings = getSettings();
		settings.put(key, value);
		
		JSONObject json = getFile();
		json.put("settings", settings);
		
		setFile(json);
	}
	
	public String getPrefix() {
		return getSettings().getString("prefix");
	}
	
	public void setPrefix(String prefix) {
		setSetting("prefix", prefix);
	}
	
	public boolean isPrivateHelp() {
		return getSettings().getBoolean("privateHelp");
	}
	
	public void setPrivateHelp(boolean privateHelp) {
		setSetting("privateHelp", privateHelp);
	}
	
	public boolean isPrivateLyrics() {
		return getSettings().getBoolean("privateLyrics");
	}
	
	public void setPrivateLyrics(boolean privateLyrics) {
		setSetting("privateLyrics", privateLyrics);
	}
	
	public boolean isPrivateWiki() {
		return getSettings().getBoolean("privateWiki");
	}
	
	public void setPrivateWiki(boolean privateWiki) {
		setSetting("privateWiki", privateWiki);
	}
	
	public boolean isRemoveCommand() {
		return getSettings().getBoolean("removeCommand");
	}
	
	public void setRemoveCommand(boolean removeCommand) {
		setSetting("removeCommand", removeCommand);
	}
}
