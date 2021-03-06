package me.szumielxd.SimpleChat.libs.bungeechat.chat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.SelectorComponent;

import java.lang.reflect.Type;

public class SelectorComponentSerializer
extends BaseComponentSerializer
implements JsonSerializer<SelectorComponent>,
JsonDeserializer<SelectorComponent> {
	@Override
	public SelectorComponent deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = element.getAsJsonObject();
		SelectorComponent component = new SelectorComponent(object.get("selector").getAsString());
		this.deserialize(object, component, context);
		return component;
	}

	@Override
	public JsonElement serialize(SelectorComponent component, Type type, JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		this.serialize(object, component, context);
		object.addProperty("selector", component.getSelector());
		return object;
	}
}