package me.szumielxd.SimpleChat.format.message;

import org.bukkit.entity.Player;

import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.TextComponent;

public class TextMessageModule extends MessageModule {
	
	private String text;
	
	public TextMessageModule(String text) {
		this.text = text;
	}

	@Override
	public TextComponent parse(Player p) {
		return new TextComponent(TextComponent.fromLegacyText(this.text));
	}
	
	@Override
	public String toString() {
		return this.text;
	}
	
	
	
	

}
