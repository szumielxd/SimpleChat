package me.szumielxd.SimpleChat.format.message;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import me.szumielxd.SimpleChat.SimpleChat;
import me.szumielxd.SimpleChat.format.TextLineAccess;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.ClickEvent;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.ComponentBuilder;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.HoverEvent;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.TextComponent;
import me.szumielxd.SimpleChat.managers.ConfigLoader;
import me.szumielxd.SimpleChat.managers.PlaceholderParser;

public class MentionMessageModule extends MessageModule {

	
	private Player mentioned;
	private String replacedText;
	private ArrayList<TextLineAccess> hoverList = new ArrayList<>();
	private TextLineAccess command;
	private TextLineAccess url;
	private TextLineAccess suggest;
	private TextLineAccess insertion;
	
	
	public MentionMessageModule(Player mentioned, String replacedText) {
		ConfigLoader cfg = SimpleChat.getInst().getConfigLoader();
		PlaceholderParser parser = SimpleChat.getInst().getParser();
		this.mentioned = mentioned;
		this.replacedText = replacedText;
		this.hoverList = SimpleChat.getInst().getConfigLoader().MENTION_HOVER.stream().filter(line -> line.canShow(mentioned)).map(line -> line.setText(parser.parse(mentioned, line.getText()))).collect(Collectors.toCollection(ArrayList::new));
		if(cfg.MENTION_COMMAND != null && cfg.MENTION_COMMAND.canShow(mentioned)) this.command = cfg.MENTION_COMMAND.clone().setText(parser.parse(mentioned, cfg.MENTION_COMMAND.getText()));
		if(cfg.MENTION_URL != null && cfg.MENTION_URL.canShow(mentioned)) this.url = cfg.MENTION_URL.clone().setText(parser.parse(mentioned, cfg.MENTION_URL.getText()));
		if(cfg.MENTION_SUGGEST != null && cfg.MENTION_SUGGEST.canShow(mentioned)) this.suggest = cfg.MENTION_SUGGEST.clone().setText(parser.parse(mentioned, cfg.MENTION_SUGGEST.getText()));
		if(cfg.MENTION_INSERTION != null && cfg.MENTION_INSERTION.canShow(mentioned)) this.insertion = cfg.MENTION_INSERTION.clone().setText(parser.parse(mentioned, cfg.MENTION_INSERTION.getText()));
	}
	
	
	@Override
	public TextComponent parse(Player target) {
		if(target.canSee(this.mentioned)) {
			SimpleChat plugin = SimpleChat.getInst();
			TextComponent text = new TextComponent(TextComponent.fromLegacyText((replacedText.startsWith(" ")? " ": "").concat(plugin.getParser().parse(this.mentioned, plugin.getConfigLoader().MENTION_FORMAT))));
			if(this.hoverList.size() > 0) {
				String hover = String.join("\n", this.hoverList.stream().filter(line -> line.canSee(mentioned)).map(TextLineAccess::getText).collect(Collectors.toCollection(ArrayList::new)));
				text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
			}
			if(this.command != null && this.command.canSee(target)) text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, plugin.getParser().parse(mentioned, this.command.getText())));
			if(this.url != null && this.url.canSee(target)) text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, plugin.getParser().parse(mentioned, this.url.getText())));
			if(this.suggest != null && this.suggest.canSee(target)) text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getParser().parse(mentioned, this.suggest.getText())));
			if(this.insertion != null && this.insertion.canSee(target)) text.setInsertion(plugin.getParser().parse(mentioned, this.command.getText()));
			return text;
		}
		return new TextComponent(TextComponent.fromLegacyText(this.replacedText));
	}

}
