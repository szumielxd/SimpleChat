package me.szumielxd.SimpleChat.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import me.szumielxd.SimpleChat.format.TextLineAccess;

public class ConfigLoader {
	
	
	// ITEM
	public List<String> ITEM_PATTERN = new ArrayList<>();
	public String ITEM_FORMAT = "§f[§b%name%%amount%§f]";
	public String ITEM_AIR_FORMAT = "§f[§b%name%'s hand§f]";
	public ArrayList<String> ITEM_AIR_HOVER = new ArrayList<>();
	
	// MENTION
	public ArrayList<TextLineAccess> MENTION_HOVER = new ArrayList<>();
	public String MENTION_FORMAT = "§b@%nick%";
	public TextLineAccess MENTION_COMMAND;
	public TextLineAccess MENTION_URL;
	public TextLineAccess MENTION_SUGGEST;
	public TextLineAccess MENTION_INSERTION;
	
	private Configuration config;
	
	
	public ConfigLoader(Configuration config) {
		this.config = config;
		{
			// ITEM
			ConfigurationSection section = this.config.getConfigurationSection("item");
			ITEM_PATTERN = section.getStringList("pattern");
			ITEM_FORMAT = ChatColor.translateAlternateColorCodes('&', section.getString("format", ITEM_FORMAT));
			ITEM_AIR_FORMAT = ChatColor.translateAlternateColorCodes('&', section.getString("air-format", ITEM_FORMAT));
			ITEM_AIR_HOVER = section.getStringList("air-hover").stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toCollection(ArrayList::new));
			
		}
		{
			// MENTION
			ConfigurationSection section = this.config.getConfigurationSection("mention");
			MENTION_HOVER = section.getStringList("hover").stream().map(key -> new TextLineAccess(key)).collect(Collectors.toCollection(ArrayList::new));
			MENTION_FORMAT = ChatColor.translateAlternateColorCodes('&', section.getString("format", MENTION_FORMAT));
			MENTION_COMMAND = !section.isSet("command")? null : new TextLineAccess(ChatColor.translateAlternateColorCodes('&', section.getString("command")));
			MENTION_URL = !section.isSet("url")? null : new TextLineAccess(ChatColor.translateAlternateColorCodes('&', section.getString("url")));
			MENTION_SUGGEST = !section.isSet("suggest")? null : new TextLineAccess(ChatColor.translateAlternateColorCodes('&', section.getString("suggest")));
			MENTION_INSERTION = !section.isSet("insertion")? null : new TextLineAccess(ChatColor.translateAlternateColorCodes('&', section.getString("insertion")));
		}
		
	}
	

}
