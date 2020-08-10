package me.szumielxd.SimpleChat.format;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.szumielxd.SimpleChat.SimpleChat;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.ClickEvent;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.ComponentBuilder;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.HoverEvent;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.TextComponent;
import me.szumielxd.SimpleChat.managers.PlaceholderParser;

public class DefinedFormatModule extends FormatModule {
	

	public DefinedFormatModule(FormatModule archetype, Player sender) {
		super(archetype.getName(), SimpleChat.getInst().getParser().parse(sender, archetype.getText()));
		
		PlaceholderParser parser = SimpleChat.getInst().getParser();
		
		if(archetype.command!=null&&archetype.command.canShow(sender)) this.command = archetype.getCommand().setText(parser.parse(sender, archetype.command.getText()));
		if(archetype.url!=null&&archetype.url.canShow(sender)) this.url = archetype.getUrl().setText(parser.parse(sender, archetype.url.getText()));
		if(archetype.suggest!=null&&archetype.suggest.canShow(sender)) this.suggest = archetype.getSuggestCommand().setText(parser.parse(sender, archetype.suggest.getText()));
		if(archetype.insertion!=null&&archetype.insertion.canShow(sender)) this.insertion = archetype.getInsertion().setText(parser.parse(sender, archetype.suggest.getText()));
		ArrayList<TextLineAccess> list = archetype.getHover();
		if(!list.isEmpty()) {
			for(TextLineAccess txt : list) {
				if(txt.canShow(sender)) this.hover.add(txt.clone().setText(parser.parse(sender, txt.getText())));
			}
		}
		
	}
	
	public TextComponent parse(Player receiver) {
		
		TextComponent text = new TextComponent(TextComponent.fromLegacyText(this.getText()));
		
		// HOVER
		if(!this.hover.isEmpty()) {
			ArrayList<String> list = new ArrayList<>();
			for(TextLineAccess line : this.hover) {
				if(line.canSee(receiver)) list.add(line.getText());
			}
			if(!list.isEmpty()) {
				text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(String.join("\n", list)).create()));
			}
		}
		
		// RUN_COMMAND
		if(this.command != null && this.command.canSee(receiver)) {
			text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.command.getText()));
		}
		
		// OPEN_URL
		else if(this.url != null && this.url.canSee(receiver)) {
			text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, this.url.getText()));
		}
		
		// SUGGEST_COMMAND
		else if(this.suggest != null && this.suggest.canSee(receiver)) {
			text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, this.suggest.getText()));
		}
		
		// INSERTION
		if(this.insertion != null && this.insertion.canSee(receiver)) {
			text.setInsertion(this.insertion.getText());
		}
		return text;
	}
	
	
	
	

}
