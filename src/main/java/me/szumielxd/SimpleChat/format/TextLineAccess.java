package me.szumielxd.SimpleChat.format;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TextLineAccess {
	
	
	private String line;
	private String seePerm;
	private String showPerm;
	
	
	public TextLineAccess(String text) {
		if(text.startsWith("{")) {
			String[] args = text.substring(1).split("\\}", 2);
			if(args.length == 2) {
				for(String param : args[0].trim().toLowerCase().split(",")) {
					Bukkit.getLogger().info(String.format(">>>>>>>>>> %s - %s", text, param));
					if(param.startsWith("see:")) this.seePerm = param.substring(4);
					else if(param.startsWith("show:")) this.showPerm = param.substring(5);
				}
				this.line = args[1];
				return;
			}
		}
		this.line = text.replace("\\{", "{").replace("\\}", "}");
	}
	
	
	private TextLineAccess() {}
	
	
	public boolean canSee(Player p) {
		return this.seePerm!=null? p.hasPermission(this.seePerm) : true;
	}
	public boolean canShow(Player p) {
		return this.showPerm!=null? p.hasPermission(this.showPerm) : true;
	}
	
	public TextLineAccess setText(String str) {
		this.line = str;
		return this;
	}
	
	public String getText() {
		return new String(this.line);
	}
	public String getSeePerm() {
		return new String(this.seePerm);
	}
	public String getShowPerm() {
		return new String(this.showPerm);
	}
	
	@Override
	public TextLineAccess clone() {
		TextLineAccess text = new TextLineAccess();
		text.line = this.line;
		text.seePerm = this.seePerm;
		text.showPerm = this.showPerm;
		return text;
	}
	

}
