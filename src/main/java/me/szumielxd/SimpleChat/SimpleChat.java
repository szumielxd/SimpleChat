package me.szumielxd.SimpleChat;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import me.szumielxd.SimpleChat.commands.ChatCommand;
import me.szumielxd.SimpleChat.listeners.ChatListener;
import me.szumielxd.SimpleChat.managers.ChatFormatManager;
import me.szumielxd.SimpleChat.managers.ConfigLoader;
import me.szumielxd.SimpleChat.managers.PermissionManager;
import me.szumielxd.SimpleChat.managers.PlaceholderParser;

public class SimpleChat extends JavaPlugin {
	
	
	private static SimpleChat instance;
	private PlaceholderParser parser;
	private ChatFormatManager chatManager;
	private PermissionManager permManager;
	private ConfigLoader mainConfig;
	
	
	@Override
	public void onEnable() {
		
		instance = this;
		this.saveDefaultConfig();
		parser = new PlaceholderParser();
		chatManager = new ChatFormatManager(new File(this.getDataFolder(), "config.yml"));
		permManager = new PermissionManager();
		mainConfig = new ConfigLoader(this.getConfig());
		Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
		this.getCommand("simplechat").setExecutor(new ChatCommand());
		
	}
	
	
	@Override
	public void onDisable() {
		
		chatManager = null;
		permManager = null;
		HandlerList.unregisterAll(this);
		
	}
	
	
	public static SimpleChat getInst() {
		return instance;
	}
	
	public PlaceholderParser getParser() {
		return parser;
	}
	
	public ChatFormatManager getChatManager() {
		return chatManager;
	}
	
	public PermissionManager getPermissionManager() {
		return permManager;
	}
	
	public ConfigLoader getConfigLoader() {
		return mainConfig;
	}
	

}
