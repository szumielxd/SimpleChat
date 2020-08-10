package me.szumielxd.SimpleChat.format.message;

import org.bukkit.entity.Player;

import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.TextComponent;

public abstract class MessageModule {
	
	public abstract TextComponent parse(Player p);

}
