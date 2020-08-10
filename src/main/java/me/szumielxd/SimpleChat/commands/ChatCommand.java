package me.szumielxd.SimpleChat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.szumielxd.SimpleChat.SimpleChat;

public class ChatCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
				SimpleChat plugin = SimpleChat.getInst();
				s.sendMessage(String.format("&2[&a%s&2]&r &7Reloading %s...", SimpleChat.getInst().getName(), SimpleChat.getInst().getDescription().getFullName()));
				plugin.onDisable();
				plugin.onEnable();
				s.sendMessage(String.format("&2[&a%s&2]&r &bSuccessfully reloaded %s!", SimpleChat.getInst().getName(), SimpleChat.getInst().getDescription().getFullName()));
			}
		}
		return false;
	}
	
	
	
	

}
