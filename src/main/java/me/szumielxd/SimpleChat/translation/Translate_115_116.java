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

public class Translate_115_116 implements Translate {
	
	
	private static BiMap<String, String> attrTranslations = HashBiMap.create();
	private static Translate_115_116 translate;

	
	public static Translate_115_116 get(boolean forceReload) {
		if(translate == null || forceReload) {
			try {
				translate = new Translate_115_116();
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
		return translate;
	}
	
	
	@SuppressWarnings("unchecked")
	private Translate_115_116() throws IOException, ParseException {
		
		{
			attrTranslations.clear();
			InputStream is = SimpleChat.getInst().getResource("a15_16.json");
			InputStreamReader reader = new InputStreamReader(is);
			JSONObject json = (JSONObject) new JSONParser().parse(reader);
			json.forEach((k, v) -> attrTranslations.put(k.toString(), v.toString()));
		}

	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public TranslatableItem downgrade(TranslatableItem item) {
		if(!item.getVersion().equals(MCVersion.v1_16)) throw new VersionTranslationException(item.getVersion(), MCVersion.v1_15, String.format("With this function you can only translate %s to %s", MCVersion.v1_16, MCVersion.v1_15));
		JSONObject json = item.getTag();
		if(json != null) {
			if(json.get("AttributeModifiers") instanceof JSONArray) {
				JSONArray attributes = (JSONArray) json.get("AttributeModifiers");
				attributes.forEach(elem -> {
					if(elem instanceof JSONObject) {
						JSONObject attr = (JSONObject) elem;
						if(attr.containsKey("AttributeName")) attr.put("AttributeName", attrTranslations.inverse().get(attr.get("AttributeName").toString()));
						if(attr.containsKey("Name")) attr.put("Name", attrTranslations.inverse().get(attr.get("Name").toString()));
						if(attr.get("UUID") instanceof JSONArray) {
							JSONArray uuid = (JSONArray) attr.get("UUID");
							if(uuid.size() == 4) {
								long most = (long)uuid.get(0) << 32 | (long)uuid.get(1) & 0xFFFFFFFFL;
								long least = (long)uuid.get(2) << 32 | (long)uuid.get(3) & 0xFFFFFFFFL;
								attr.put("UUIDMost", most);
								attr.put("UUIDLeast", least);
								attr.remove("UUID");
							}
							
						}
					}
				});
			}
		}
		return new TranslatableItem(item.getNamespace(), item.getCount(), item.getDamage(), json, MCVersion.v1_15);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TranslatableItem upgrade(TranslatableItem item) {
		if(!item.getVersion().equals(MCVersion.v1_15)) throw new VersionTranslationException(item.getVersion(), MCVersion.v1_16, String.format("With this function you can only translate %s to %s", MCVersion.v1_15, MCVersion.v1_16));
		JSONObject json = item.getTag();
		if(json != null) {
			if(json.get("AttributeModifiers") instanceof JSONArray) {
				JSONArray attributes = (JSONArray) json.get("AttributeModifiers");
				attributes.forEach(elem -> {
					if(elem instanceof JSONObject) {
						JSONObject attr = (JSONObject) elem;
						if(attr.containsKey("AttributeName")) attr.put("AttributeName", attrTranslations.get(attr.get("AttributeName").toString()));
						if(attr.containsKey("Name")) attr.put("Name", attrTranslations.get(attr.get("Name").toString()));
						if(attr.get("UUIDMost") instanceof Long && attr.get("UUIDLeast") instanceof Long) {
							long most = (long) attr.get("UUIDMost");
							long least = (long) attr.get("UUIDLeast");
							JSONArray uuid = new JSONArray();
							uuid.add((int)(most >> 32));
							uuid.add((int)most);
							uuid.add((int)(least >> 32));
							uuid.add((int)least);
							attr.remove("UUIDMost");
							attr.remove("UUIDLeast");
							attr.put("UUID", uuid);
						}
					}
				});
			}
		}
		return new TranslatableItem(item.getNamespace(), item.getCount(), item.getDamage(), json, MCVersion.v1_16);
	}
	

}
