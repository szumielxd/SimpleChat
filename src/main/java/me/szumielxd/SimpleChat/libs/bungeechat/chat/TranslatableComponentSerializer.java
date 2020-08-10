package me.szumielxd.SimpleChat.libs.bungeechat.chat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.BaseComponent;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.TranslatableComponent;

import java.lang.reflect.Type;
import java.util.Arrays;

public class TranslatableComponentSerializer
extends BaseComponentSerializer
implements JsonSerializer<TranslatableComponent>,
JsonDeserializer<TranslatableComponent> {
	@Override
	public TranslatableComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		TranslatableComponent component = new TranslatableComponent();
		JsonObject object = json.getAsJsonObject();
		this.deserialize(object, component, context);
		component.setTranslate(object.get("translate").getAsString());
		if (object.has("with")) {
			component.setWith(Arrays.asList(context.deserialize(object.get("with"), BaseComponent[].class)));
		}
		return component;
	}

	@Override
	public JsonElement serialize(TranslatableComponent src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		this.serialize(object, src, context);
		object.addProperty("translate", src.getTranslate());
		if (src.getWith() != null) {
			object.add("with", context.serialize(src.getWith()));
		}
		return object;
	}
}