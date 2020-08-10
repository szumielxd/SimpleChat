package me.szumielxd.SimpleChat.libs.bungeechat.chat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.BaseComponent;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.TextComponent;

import java.lang.reflect.Type;
import java.util.List;

public class TextComponentSerializer
extends BaseComponentSerializer
implements JsonSerializer<TextComponent>,
JsonDeserializer<TextComponent> {
	@Override
	public TextComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		TextComponent component = new TextComponent();
		JsonObject object = json.getAsJsonObject();
		this.deserialize(object, component, context);
		component.setText(object.get("text").getAsString());
		return component;
	}

	@Override
	public JsonElement serialize(TextComponent src, Type typeOfSrc, JsonSerializationContext context) {
		List<BaseComponent> extra = src.getExtra();
		JsonObject object = new JsonObject();
		if (src.hasFormatting() || extra != null && !extra.isEmpty()) {
			this.serialize(object, src, context);
		}
		object.addProperty("text", src.getText());
		return object;
	}
}