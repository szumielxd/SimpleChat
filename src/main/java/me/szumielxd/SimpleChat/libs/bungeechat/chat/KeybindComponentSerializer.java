package me.szumielxd.SimpleChat.libs.bungeechat.chat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.KeybindComponent;

import java.lang.reflect.Type;

public class KeybindComponentSerializer
extends BaseComponentSerializer
implements JsonSerializer<KeybindComponent>,
JsonDeserializer<KeybindComponent> {
    @Override
    public KeybindComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        KeybindComponent component = new KeybindComponent();
        JsonObject object = json.getAsJsonObject();
        this.deserialize(object, component, context);
        component.setKeybind(object.get("keybind").getAsString());
        return component;
    }

    @Override
    public JsonElement serialize(KeybindComponent src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        this.serialize(object, src, context);
        object.addProperty("keybind", src.getKeybind());
        return object;
    }
}