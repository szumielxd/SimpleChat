package me.szumielxd.SimpleChat.translation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import me.szumielxd.SimpleChat.SimpleChat;
import me.szumielxd.SimpleChat.format.TranslatableItem;
import me.szumielxd.SimpleChat.utils.MainUtils.MCVersion;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class Translate_112_113 implements Translate {
	
	
	private static BiMap<String, String> itemTranslations = HashBiMap.create();
	private static BiMap<String, String> enchTranslations = HashBiMap.create();
	private static Translate_112_113 translate;

	
	public static Translate_112_113 get(boolean forceReload) {
		if(translate == null || forceReload) {
			try {
				translate = new Translate_112_113();
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
		return translate;
	}
	
	
	@SuppressWarnings("unchecked")
	private Translate_112_113() throws IOException, ParseException {
		
		{
			itemTranslations.clear();
			InputStream is = SimpleChat.getInst().getResource("i12_13.json");
			InputStreamReader reader = new InputStreamReader(is);
			JSONObject json = (JSONObject) new JSONParser().parse(reader);
			json.forEach((k, v) -> itemTranslations.put(k.toString(), v.toString()));
		}
		{
			enchTranslations.clear();
			InputStream is = SimpleChat.getInst().getResource("e12_13.json");
			InputStreamReader reader = new InputStreamReader(is);
			JSONObject json = (JSONObject) new JSONParser().parse(reader);
			json.forEach((k, v) -> enchTranslations.put(k.toString(), v.toString()));
		}

	}
		
	
	@SuppressWarnings("unchecked")
	@Override
	public TranslatableItem downgrade(TranslatableItem item) {
		if(!item.getVersion().equals(MCVersion.v1_13)) throw new VersionTranslationException(item.getVersion(), MCVersion.v1_12, String.format("With this function you can only translate %s to %s", MCVersion.v1_13, MCVersion.v1_12));
		String newNamespace = itemTranslations.inverse().getOrDefault(item.getNamespace(), item.getNamespace());
		String[] args = newNamespace.split("/");
		short damage = 0;
		if(args.length > 1) {
			newNamespace = args[0];
			try{
				damage = Short.parseShort(args[1]);
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		JSONObject json = item.getTag();
		if(json != null) {
			if(json.get("display") instanceof JSONObject) {
				JSONObject display = (JSONObject) json.get("display");
				if(display.containsKey("Name")) {
					display.put("Name", TextComponent.toLegacyText(ComponentSerializer.parse(display.get("Name").toString())));
				}
			}
			if(json.get("Enchantments") instanceof JSONArray) {
				JSONArray list = (JSONArray) json.get("Enchantments");
				list.forEach(elem -> {
					if(elem instanceof JSONObject) {
						JSONObject ench = (JSONObject) elem;
						if(ench.containsKey("id")) {
							String id = ench.get("id").toString();
							ench.put("id", enchTranslations.inverse().getOrDefault(id, id));
						}
					}
				});
				json.put("ench", list);
				json.remove("Enchantments");
			}
		}
		return new TranslatableItem(newNamespace, item.getCount(), damage, json, MCVersion.v1_12);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TranslatableItem upgrade(TranslatableItem item) {
		if(!item.getVersion().equals(MCVersion.v1_12)) throw new VersionTranslationException(item.getVersion(), MCVersion.v1_13, String.format("With this function you can only translate %s to %s", MCVersion.v1_12, MCVersion.v1_13));
		String newNamespace;
		if(item.getDamage() != 0) newNamespace = itemTranslations.getOrDefault(item.getNamespace().concat("/"+item.getDamage()), item.getNamespace());
		else newNamespace = itemTranslations.getOrDefault(item.getNamespace(), item.getNamespace());
		short damage = 0;
		String[] args = newNamespace.split("/");
		if(args.length > 1) {
			newNamespace = args[0];
			try{
				damage = Short.parseShort(args[1]);
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		JSONObject json = item.getTag();
		if(json != null) {
			if(json.get("display") instanceof JSONObject) {
				JSONObject display = (JSONObject) json.get("display");
				if(display.containsKey("Name")) {
					display.put("Name", ComponentSerializer.toString(TextComponent.fromLegacyText(display.get("Name").toString())));
				}
			}
			if(json.get("ench") instanceof JSONArray) {
				JSONArray list = (JSONArray) json.get("ench");
				list.forEach(elem -> {
					if(elem instanceof JSONObject) {
						JSONObject ench = (JSONObject) elem;
						if(ench.containsKey("id")) {
							String id = ench.get("id").toString();
							ench.put("id", enchTranslations.getOrDefault(id, id));
						}
					}
				});
				json.put("Enchantments", list);
				json.remove("ench");
			}
		}
		return new TranslatableItem(newNamespace, item.getCount(), damage, json, MCVersion.v1_13);
	}
	
	
	
	

}
