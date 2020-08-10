package me.szumielxd.SimpleChat.format;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class FormatModule {
	
	protected ArrayList<TextLineAccess> hover = new ArrayList<>();
	protected TextLineAccess command;
	protected TextLineAccess url;
	protected TextLineAccess suggest;
	protected TextLineAccess insertion;
	
	private String text;
	private String name;
	private String seePerm;
	private String showPerm;
	
	public FormatModule(String name, String text) {
		this.name = name;
		this.text = text;
	}
	
	public void setHover(ArrayList<TextLineAccess> hover) {
		this.hover = hover;
	}
	public void setCommand(TextLineAccess command) {
		this.command = command;
	}
	public void setUrl(TextLineAccess url) {
		this.url = url;
	}
	public void setSuggestCommand(TextLineAccess suggest) {
		this.suggest = suggest;
	}
	public void setInsertion(TextLineAccess insertion) {
		this.insertion = insertion;
	}
	public void setSeePermission(String permission) {
		this.seePerm = permission;
	}
	public void setShowPermission(String permission) {
		this.showPerm = permission;
	}
	
	public String getName() {
		return new String(this.name);
	}
	public String getText() {
		return new String(this.text);
	}
	public String getSeePermission() {
		return new String(this.seePerm);
	}
	public String getShowPermission() {
		return new String(this.showPerm);
	}
	public ArrayList<TextLineAccess> getHover(){
		return new ArrayList<>(this.hover);
	}
	public TextLineAccess getCommand(){
		return this.command.clone();
	}
	public TextLineAccess getUrl(){
		return this.url.clone();
	}
	public TextLineAccess getSuggestCommand(){
		return this.suggest.clone();
	}
	public TextLineAccess getInsertion(){
		return this.insertion.clone();
	}
	
	public boolean canSee(Player p) {
		return this.seePerm!=null? p.hasPermission(this.seePerm) : true;
	}
	public boolean canShow(Player p) {
		return this.showPerm!=null? p.hasPermission(this.showPerm) : true;
	}
	

}
