package me.szumielxd.SimpleChat.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;
import us.myles.ViaVersion.api.Via;

public class MainUtils {
	
	
	public static String replaceIgnoreCase(String str, String target, String replacement) {
		return str.replaceAll("(?i)"+Pattern.quote(target), replacement);
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject getNBTToJSON(Object nbt) {
		JSONObject json = new JSONObject();
		Class<?> compound = MCVersion.getDefault().getNetNMSClass("NBTTagCompound");
		Class<?> list = MCVersion.getDefault().getNetNMSClass("NBTTagList");
		Map<String,?> map = (Map<String, ?>) MainUtils.getField(nbt, "map");
		Set<String> keys = map.keySet();
		keys.forEach(key -> {
			Object value = MainUtils.methInvoke(compound, nbt, "get", key);
			if(compound.isInstance(value)) json.put(key, getNBTToJSON(value));
			else if(list.isInstance(value)) {
				json.put(key, getNBTListToList((ArrayList<Object>) MainUtils.getField(value, "list")));
			}
			else json.put(key, MainUtils.getField(value, "data"));
		});
		return json;
	}
	
	
	@SuppressWarnings("unchecked")
	public static JSONArray getNBTListToList(ArrayList<Object> nbtList) {
		Class<?> compound = MCVersion.getDefault().getNetNMSClass("NBTTagCompound");
		Class<?> list = MCVersion.getDefault().getNetNMSClass("NBTTagList");
		JSONArray arr = new JSONArray();
		nbtList.replaceAll(nbt -> {
			if(compound.isInstance(nbt)) return getNBTToJSON(nbt);
			else if(list.isInstance(nbt)) return getNBTListToList((ArrayList<Object>) MainUtils.getField(nbt, "list"));
			return MainUtils.getField(nbt, "data");
		});
		arr.addAll(nbtList);
		return arr;
	}
	
	
	@SuppressWarnings("unchecked")
	public static JSONObject jsonObjectDeepClone(JSONObject json) {
		if(json == null) return null;
		JSONObject newJson = new JSONObject();
		json.forEach((k, v) -> {
			Object key;
			if(k instanceof JSONObject) {
				key = jsonObjectDeepClone((JSONObject) k);
			} else if(k instanceof JSONArray) {
				key = jsonArrayDeepClone((JSONArray) k);
			} else if(k instanceof String) {
				key = new String((String) k);
			} else {
				key = k;
			}
			Object value;
			if(v instanceof JSONObject) {
				value = jsonObjectDeepClone((JSONObject) v);
			} else if(v instanceof JSONArray) {
				value = jsonArrayDeepClone((JSONArray) v);
			} else if(v instanceof String) {
				value = new String((String) v);
			} else {
				value = v;
			}
			newJson.put(key, value);
		});
		return newJson;
	}
	
	
	@SuppressWarnings("unchecked")
	public static JSONArray jsonArrayDeepClone(JSONArray json) {
		if(json == null) return null;
		JSONArray newJson = new JSONArray();
		json.forEach(v -> {
			Object value;
			if(v instanceof JSONObject) {
				value = jsonObjectDeepClone((JSONObject) v);
			} else if(v instanceof JSONArray) {
				value = jsonArrayDeepClone((JSONArray) v);
			} else if(v instanceof String) {
				value = new String((String) v);
			} else {
				value = v;
			}
			newJson.add(value);
		});
		return newJson;
	}
	
	
	@SuppressWarnings("unchecked")
	public static String jsonToNBTString(Object obj) {
		/*
		 * boolean - true/false
		 * byte - #b
		 * double - #d
		 * float - #f
		 * int - #
		 * long - #L
		 * short - #s
		 * string - "#"
		 */
		StringBuilder text = new StringBuilder();
		// Map
		if(obj instanceof JSONObject) {
			JSONObject json = (JSONObject) obj;
			text.append('{');
			ArrayList<String> values = new ArrayList<String>();
			json.forEach((key, value) -> {
				values.add(key.toString().concat(":").concat(jsonToNBTString(value)));
			});
			text.append(String.join(",", values)).append('}');
		// Array
		} else if(obj instanceof JSONArray) {
			JSONArray json = (JSONArray) obj;
			text.append('[');
			if(!json.isEmpty() && isInstance(json, Integer.class)) text.append("I;");
			ArrayList<String> value = ((Stream<Object>)json.stream()).map(MainUtils::jsonToNBTString).collect(Collectors.toCollection(ArrayList::new));
			text.append(String.join(",", value));
			text.append(']');
		// String
		} else if(obj instanceof String) {
			text.append('"');
			text.append(((String)obj).replace("\\", "\\\\").replace("\"", "\\\""));
			text.append('"');
		// Byte
		} else if(obj instanceof Byte) {
			text.append(((byte)obj)+"b");
		// Double
		} else if(obj instanceof Double) {
			text.append(((double)obj)+"d");
		// Float
		} else if(obj instanceof Float) {
			text.append(((float)obj)+"f");
		// Int
		} else if(obj instanceof Integer) {
			text.append(((int)obj));
		// Long
		} else if(obj instanceof Long) {
			text.append(((long)obj)+"L");
		// Short
		} else if(obj instanceof Short) {
			text.append(((short)obj)+"s");
		// Object
		} else {
			System.out.println("}}}}}}}}}}} "+obj.getClass()+" -> "+obj.toString());
			text.append('"');
			text.append(obj.toString().replace("\\", "\\\\").replace("\"", "\\\""));
			text.append('"');
		}
		return text.toString();
	}
	
	
	public static boolean isInstance(List<?> list, Class<?> cl) {
		if(list == null || cl == null || list.isEmpty()) return false;
		for(Object val : list) {
			if(!cl.isInstance(val)) return false;
		}
		return true;
	}
	
	
/*	public static TextComponent fixedFormat(String str) {
		String[] arr = str.split("§");
		boolean bold = false;
		boolean italic = false;
		boolean underlined = false;
		boolean strikethrough = false;
		boolean obfuscated = false;
		ChatColor color = ChatColor.RESET;
		TextComponent result = new TextComponent(arr[0]!=null? arr[0] : "");
		for(int i=1; i < arr.length; i++) {
			TextComponent text = new TextComponent(arr[i].substring(1));
			switch(arr[i].substring(0, 1)) {
				case "r": {
					bold = false;
					italic = false;
					underlined = false;
					strikethrough = false;
					obfuscated = false;
					color = ChatColor.RESET;
					break;
				}
				case "l": {
					bold = true;
					break;
				}
				case "o": {
					italic = true;
					break;
				}
				case "n": {
					underlined = true;
					break;
				}
				case "m": {
					strikethrough = true;
					break;
				}
				case "k": {
					obfuscated = true;
					break;
				}
				default: {
					try{
						Integer.parseInt(arr[i].substring(0, 1), 16);
						color = ChatColor.getByChar(arr[i].charAt(0));
					} catch(NumberFormatException e) {
						text.setText(arr[i]);
					}
				}
			}
			text.setBold(bold);
			text.setItalic(italic);
			text.setUnderlined(underlined);
			text.setStrikethrough(strikethrough);
			text.setObfuscated(obfuscated);
			text.setColor(color);
			result.addExtra(text);
		}
		return result;
	}
*/	
	
	public static Object getNMSItemStack(ItemStack is) {
		try {
			Class<?> cis = Class.forName(MCVersion.getDefault().getCraftNMS().concat(".inventory.CraftItemStack"));
			return MainUtils.methInvoke(cis, null, "asNMSCopy", new Class<?>[] {ItemStack.class}, is);
		} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static Object methInvoke(Class<?> cl, Object obj, String method, Object... args) {
		Class<?>[] classes = new Class<?>[args.length];
		for(int i=0; i<args.length; i++) {
			classes[i] = args[i].getClass();
		}
		try {
			Method met = cl.getDeclaredMethod(method, classes);
			return met != null? met.invoke(obj, args) : null;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static Object methInvoke(Class<?> cl, Object obj, String method, Class<?>[] classes, Object... args) {
		try {
			Method met = cl.getDeclaredMethod(method, classes);
			return met != null? met.invoke(obj, args) : null;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static Object getField(Class<?> cl, Object obj, String field) {
		try {
			Field f = cl.getDeclaredField(field);
			f.setAccessible(true);
			Object o = f.get(obj);
			f.setAccessible(false);
			return o;
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static Object getField(Object obj, String field) {
		try {
			Field f = obj.getClass().getDeclaredField(field);
			if(f == null) return null;
			f.setAccessible(true);
			Object o = f.get(obj);
			f.setAccessible(false);
			return o;
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public enum MCVersion {
		
		UNKNOWN("UNKNOWN", -1),
		v1_8("v1_8_R3", 1),
		v1_9("v1_9_R2", 2),
		v1_10("v1_10_R1", 3),
		v1_11("v1_11_R1", 4),
		v1_12("v1_12_R1", 5),
		v1_13("v1_13_R2", 6),
		v1_14("v1_14_R1", 7),
		v1_15("v1_15_R1", 8),
		v1_16("v1_16_R1", 9);
		
		
		private String nms;
		private int index;
		
		MCVersion(String str, int index) {
			this.nms = str;
			this.index = index;
		}
		public static MCVersion getPlayerVersion(Player p) {
			try {
				Class.forName("us.myles.ViaVersion.api.Via");
				int version = Via.getAPI().getPlayerVersion(p.getUniqueId());
				switch(version) {
					case 47: {
						return v1_8;
					}
					case 107: case 108: case 109: case 110: {
						return v1_9;
					}
					case 210: {
						return v1_10;
					}
					case 315: case 316: {
						return v1_11;
					}
					case 335: case 338: case 340: {
						return v1_12;
					}
					case 394: case 401: case 404: {
						return v1_13;
					}
					case 477: case 480: case 485: case 490: case 498: {
						return v1_14;
					}
					case 573: case 575: case 578: {
						return v1_15;
					}
					case 735: case 736: {
						return v1_16;
					}
					default: {
						return UNKNOWN;
					}
				}
			} catch(NoClassDefFoundError | ClassNotFoundException e) {}
			
			try {
				Class.forName("protocolsupport.api.ProtocolSupportAPI");
				Class.forName("protocolsupport.api.ProtocolVersion");
				switch(ProtocolSupportAPI.getProtocolVersion(p)) {
					case MINECRAFT_1_8: {
						return v1_8;
					}
					case MINECRAFT_1_9: case MINECRAFT_1_9_1: case MINECRAFT_1_9_2: case MINECRAFT_1_9_4: {
						return v1_9;
					}
					case MINECRAFT_1_10: {
						return v1_10;
					}
					case MINECRAFT_1_11: case MINECRAFT_1_11_1: {
						return v1_11;
					}
					case MINECRAFT_1_12: case MINECRAFT_1_12_1: case MINECRAFT_1_12_2: {
						return v1_12;
					}
					case MINECRAFT_1_13: case MINECRAFT_1_13_1: case MINECRAFT_1_13_2: {
						return v1_13;
					}
					case MINECRAFT_1_14: case MINECRAFT_1_14_1: case MINECRAFT_1_14_2: case MINECRAFT_1_14_3: case MINECRAFT_1_14_4: {
						return v1_14;
					}
					case MINECRAFT_1_15: case MINECRAFT_1_15_1: case MINECRAFT_1_15_2: {
						return v1_15;
					}
					default: {
						return UNKNOWN;
					}
				}
			} catch(NoClassDefFoundError | ClassNotFoundException e) {}
			
			return MCVersion.getDefault();
		}
		public boolean isOlderThan(MCVersion ver) {
			if(this.index == -1) return false;
			if(ver.index == -1) return true;
			return this.index < ver.index;
		}
		public boolean isNewerThan(MCVersion ver) {
			if(this.index == -1) return false;
			if(ver.index == -1) return true;
			return this.index > ver.index;
		}
		public boolean isOlderEqualThan(MCVersion ver) {
			if(this.index == -1) return false;
			if(ver.index == -1) return true;
			return this.index <= ver.index;
		}
		public boolean isNewerEqualThan(MCVersion ver) {
			if(this.index == -1) return false;
			if(ver.index == -1) return true;
			return this.index >= ver.index;
		}
		public MCVersion newer() {
			if(this.index == -1) return UNKNOWN;
			int ver = this.index + 1;
			for(MCVersion version : MCVersion.values()) {
				if(version.index == ver) return version;
			}
			return null;
		}
		public MCVersion older() {
			if(this.index == -1) return UNKNOWN;
			int ver = this.index - 1;
			for(MCVersion version : MCVersion.values()) {
				if(version.index == ver) return version;
			}
			return null;
		}
		public static MCVersion getDefault() {
			String str = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
			System.out.println("++++++"+Bukkit.getServer().getClass().getPackage().getName()+" -> "+str);
			for(MCVersion ver : MCVersion.values()) {
				if(ver.getNMS().equals(str)) return ver;
			}
			return UNKNOWN;
		}
		
		
		public boolean isSupported() {
			try {
				Class.forName("us.myles.ViaVersion.api.Via");
				
				switch(this) {
					case v1_8: {
						if(us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(47)) return true;
						break;
					}
					case v1_9: {
						if(us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(107)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(108)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(109)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(110)) return true;
						break;
					}
					case v1_10: {
						if(us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(210)) return true;
						break;
					}
					case v1_11: {
						if(us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(315)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(316)) return true;
						break;
					}
					case v1_12: {
						if(us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(335)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(338)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(340)) return true;
						break;
					}
					case v1_13: {
						if(us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(394)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(401)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(404)) return true;
						break;
					}
					case v1_14: {
						if(us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(477)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(480)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(485)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(490)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(498)) return true;
						break;
					}
					case v1_15: {
						if(us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(573)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(575)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(578)) return true;
						break;
					}
					case v1_16: {
						if(us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(735)
								|| us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(736)) return true;
						break;
					}
					default: {
						break;
					}
				}
			} catch(NoClassDefFoundError | ClassNotFoundException e) {}
			
			try {
				Class.forName("protocolsupport.api.ProtocolSupportAPI");
				switch(this) {
					case v1_8: {
						if(ProtocolVersion.MINECRAFT_1_8.isSupported())return true;
						break;
					}
					case v1_9: {
						if(ProtocolVersion.MINECRAFT_1_9.isSupported()
								|| ProtocolVersion.MINECRAFT_1_9_1.isSupported()
								|| ProtocolVersion.MINECRAFT_1_9_2.isSupported()
								|| ProtocolVersion.MINECRAFT_1_9_4.isSupported())return true;
						break;
					}
					case v1_10: {
						if(ProtocolVersion.MINECRAFT_1_10.isSupported())return true;
						break;
					}
					case v1_11: {
						if(ProtocolVersion.MINECRAFT_1_11.isSupported()
								|| ProtocolVersion.MINECRAFT_1_11_1.isSupported())return true;
						break;
					}
					case v1_12: {
						if(ProtocolVersion.MINECRAFT_1_12.isSupported()
								|| ProtocolVersion.MINECRAFT_1_12_1.isSupported()
								|| ProtocolVersion.MINECRAFT_1_12_2.isSupported())return true;
						break;
					}
					case v1_13: {
						if(ProtocolVersion.MINECRAFT_1_13.isSupported()
								|| ProtocolVersion.MINECRAFT_1_13_1.isSupported()
								|| ProtocolVersion.MINECRAFT_1_13_2.isSupported())return true;
						break;
					}
					case v1_14: {
						if(ProtocolVersion.MINECRAFT_1_14.isSupported()
								|| ProtocolVersion.MINECRAFT_1_14_1.isSupported()
								|| ProtocolVersion.MINECRAFT_1_14_2.isSupported()
								|| ProtocolVersion.MINECRAFT_1_14_3.isSupported()
								|| ProtocolVersion.MINECRAFT_1_14_4.isSupported())return true;
						break;
					}
					case v1_15: {
						if(ProtocolVersion.MINECRAFT_1_15.isSupported()
								|| ProtocolVersion.MINECRAFT_1_15_1.isSupported()
								|| ProtocolVersion.MINECRAFT_1_15_2.isSupported())return true;
						break;
					}
					default: {
						break;
					}
				}
			} catch(NoClassDefFoundError | ClassNotFoundException e) {}
			
			return MCVersion.getDefault().equals(this);
		}
		
		
		public String getCraftNMS() {
			return String.format("org.bukkit.craftbukkit.%s", this.nms);
		}
		public String getNetNMS() {
			return String.format("net.minecraft.server.%s", this.nms);
		}
		public String getNMS() {
			return new String(this.nms);
		}
		
		public Class<?> getCraftNMSClass(String cl) {
			try {
				return Class.forName(String.format("org.bukkit.craftbukkit.%s.%s", this.nms, cl));
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
		public Class<?> getNetNMSClass(String cl) {
			try {
				System.out.println("#######  "+String.format("net.minecraft.server.%s.%s", this.nms, cl));
				return Class.forName(String.format("net.minecraft.server.%s.%s", this.nms, cl));
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
		
	}
	
	

}
