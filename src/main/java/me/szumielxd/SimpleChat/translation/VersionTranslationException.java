package me.szumielxd.SimpleChat.translation;

import me.szumielxd.SimpleChat.utils.MainUtils.MCVersion;

public class VersionTranslationException extends RuntimeException {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MCVersion from;
	private MCVersion to;
	
	public VersionTranslationException(MCVersion from, MCVersion to, String text) {
		super(text==null? String.format("An exception occured while translating from %s to %s", from, to) : text);
		this.from = from;
		this.to = to;
	}
	
	
	public MCVersion getFrom() {
		return this.from;
	}
	public MCVersion getTo() {
		return this.to;
	}
	

}
