package me.szumielxd.SimpleChat.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.szumielxd.SimpleChat.utils.MainUtils;

public class PlaceholderParser {
	
	
	private boolean papi = false;
	
	
	public PlaceholderParser() {
		try {
			if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
				Class.forName("me.clip.placeholderapi.PlaceholderAPI");
				papi = true;
			}
		} catch (ClassNotFoundException ex) {}
	}
	
	
	public String parse(Player p, String text) {
		text = MainUtils.replaceIgnoreCase(text, "%name%", p.getName());
		text = MainUtils.replaceIgnoreCase(text, "%displayname%", p.getDisplayName());
		text = MainUtils.replaceIgnoreCase(text, "%ip%", p.getAddress().getAddress().getHostAddress());
		text = MainUtils.replaceIgnoreCase(text, "%uuid%", p.getUniqueId().toString());
		if(papi) text = PlaceholderAPI.setPlaceholders(p, text);
		return text;
	}
	

}
