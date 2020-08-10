package me.szumielxd.SimpleChat.translation;

import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import me.szumielxd.SimpleChat.format.TranslatableItem;
import me.szumielxd.SimpleChat.utils.MainUtils.MCVersion;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class Translate_113_114 implements Translate {
	
	
	private static Translate_113_114 translate;

	
	public static Translate_113_114 get(boolean forceReload) {
		if(translate == null || forceReload) {
			try {
				translate = new Translate_113_114();
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
		return translate;
	}
	
	
	private Translate_113_114() throws IOException, ParseException {
		
	}
		
	
	@SuppressWarnings("unchecked")
	@Override
	public TranslatableItem downgrade(TranslatableItem item) {
		if(!item.getVersion().equals(MCVersion.v1_14)) throw new VersionTranslationException(item.getVersion(), MCVersion.v1_13, String.format("With this function you can only translate %s to %s", MCVersion.v1_14, MCVersion.v1_13));
		JSONObject json = item.getTag();
		if(json != null) {
			if(json.get("display") instanceof JSONObject) {
				JSONObject display = (JSONObject) json.get("display");
				if(display.get("Lore") instanceof JSONArray) {
					JSONArray lore = (JSONArray) display.get("Lore");
					lore.replaceAll(line -> TextComponent.toLegacyText(ComponentSerializer.parse(line.toString())));
				}
			}
		}
		return new TranslatableItem(item.getNamespace(), item.getCount(), item.getDamage(), json, MCVersion.v1_13);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TranslatableItem upgrade(TranslatableItem item) {
		if(!item.getVersion().equals(MCVersion.v1_13)) throw new VersionTranslationException(item.getVersion(), MCVersion.v1_14, String.format("With this function you can only translate %s to %s", MCVersion.v1_13, MCVersion.v1_14));
		JSONObject json = item.getTag();
		if(json != null) {
			if(json.get("display") instanceof JSONObject) {
				JSONObject display = (JSONObject) json.get("display");
				if(display.get("Lore") instanceof JSONArray) {
					JSONArray lore = (JSONArray) display.get("Lore");
					lore.replaceAll(line -> ComponentSerializer.toString(TextComponent.fromLegacyText(line.toString())));
				}
			}
		}
		return new TranslatableItem(item.getNamespace(), item.getCount(), item.getDamage(), json, MCVersion.v1_14);
	}
	
	
	
	

}
