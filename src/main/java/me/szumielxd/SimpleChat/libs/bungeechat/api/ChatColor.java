package me.szumielxd.SimpleChat.libs.bungeechat.api;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public final class ChatColor {
	public static final char COLOR_CHAR = '\u00a7';
	public static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";
	public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('\u00a7') + "[0-9A-FK-ORX]");
	private static final Map<Character, ChatColor> BY_CHAR = new HashMap<Character, ChatColor>();
	private static final Map<String, ChatColor> BY_NAME = new HashMap<String, ChatColor>();
	public static final ChatColor BLACK = new ChatColor('0', "black", new Color(0));
	public static final ChatColor DARK_BLUE = new ChatColor('1', "dark_blue", new Color(170));
	public static final ChatColor DARK_GREEN = new ChatColor('2', "dark_green", new Color(43520));
	public static final ChatColor DARK_AQUA = new ChatColor('3', "dark_aqua", new Color(43690));
	public static final ChatColor DARK_RED = new ChatColor('4', "dark_red", new Color(11141120));
	public static final ChatColor DARK_PURPLE = new ChatColor('5', "dark_purple", new Color(11141290));
	public static final ChatColor GOLD = new ChatColor('6', "gold", new Color(16755200));
	public static final ChatColor GRAY = new ChatColor('7', "gray", new Color(11184810));
	public static final ChatColor DARK_GRAY = new ChatColor('8', "dark_gray", new Color(5592405));
	public static final ChatColor BLUE = new ChatColor('9', "blue", new Color(5592575));
	public static final ChatColor GREEN = new ChatColor('a', "green", new Color(5635925));
	public static final ChatColor AQUA = new ChatColor('b', "aqua", new Color(5636095));
	public static final ChatColor RED = new ChatColor('c', "red", new Color(16733525));
	public static final ChatColor LIGHT_PURPLE = new ChatColor('d', "light_purple", new Color(16733695));
	public static final ChatColor YELLOW = new ChatColor('e', "yellow", new Color(16777045));
	public static final ChatColor WHITE = new ChatColor('f', "white", new Color(16777215));
	public static final ChatColor MAGIC = new ChatColor('k', "obfuscated", null);
	public static final ChatColor BOLD = new ChatColor('l', "bold", null);
	public static final ChatColor STRIKETHROUGH = new ChatColor('m', "strikethrough", null);
	public static final ChatColor UNDERLINE = new ChatColor('n', "underline", null);
	public static final ChatColor ITALIC = new ChatColor('o', "italic", null);
	public static final ChatColor RESET = new ChatColor('r', "reset", null);
	private static int count = 0;
	private final String toString;
	private final String name;
	private final int ordinal;
	private final Color rgb;
	private final ChatColor old;

	private ChatColor(char code, String name, Color rgb) {
		this.old = null;
		this.rgb = rgb;
		this.name = name;
		this.toString = new String(new char[]{'\u00a7', code});
		this.ordinal = count++;
		BY_CHAR.put(Character.valueOf(code), this);
		BY_NAME.put(name.toUpperCase(Locale.ROOT), this);
	}

	private ChatColor(String name, String toString) {
		this.name = name;
		this.rgb = new Color(Integer.valueOf(name.substring(1), 16));
		this.toString = toString;
		this.ordinal = -1;
		
		ChatColor nearest = null;
		Integer distance = new Integer(Integer.MAX_VALUE);
		for(ChatColor c : BY_CHAR.values()) {
			if(c.rgb == null) continue;
			if(distance > Math.pow(this.rgb.getRed() - c.rgb.getRed(), 2)
					+ Math.pow(this.rgb.getGreen() - c.rgb.getGreen(), 2)
					+ Math.pow(this.rgb.getBlue() - c.rgb.getBlue(), 2)
			) {
				nearest = c;
			}
		}
		this.old = nearest;
		
	}

	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + Objects.hashCode(this.toString);
		return hash;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		ChatColor other = (ChatColor)obj;
		return Objects.equals(this.toString, other.toString);
	}

	public String toString() {
		return this.toString;
	}
	
	
	public ChatColor getOldFormat() {
		if(this.name.startsWith("#")) {
			return this.old;
		} else {
			return this;
		}
	}
	

	public static String stripColor(String input) {
		if (input == null) {
			return null;
		}
		return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
	}

	public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
		char[] b2 = textToTranslate.toCharArray();
		for (int i2 = 0; i2 < b2.length - 1; ++i2) {
			if (b2[i2] != altColorChar || ALL_CODES.indexOf(b2[i2 + 1]) <= -1) continue;
			b2[i2] = 167;
			b2[i2 + 1] = Character.toLowerCase(b2[i2 + 1]);
		}
		return new String(b2);
	}

	public static ChatColor getByChar(char code) {
		return BY_CHAR.get(Character.valueOf(code));
	}

	public static ChatColor of(Color color) {
		return ChatColor.of("#" + Integer.toHexString(color.getRGB()).substring(2));
	}

	public static ChatColor of(String string) {
		Preconditions.checkArgument(string != null, "string cannot be null");
		if (string.startsWith("#") && string.length() == 7) {
			try {
				Integer.parseInt(string.substring(1), 16);
			}
			catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Illegal hex string " + string);
			}
			StringBuilder magic = new StringBuilder("\u00a7x");
			for (char c2 : string.substring(1).toCharArray()) {
				magic.append('\u00a7').append(c2);
			}
			return new ChatColor(string, magic.toString());
		}
		ChatColor defined = BY_NAME.get(string.toUpperCase(Locale.ROOT));
		if (defined != null) {
			return defined;
		}
		throw new IllegalArgumentException("Could not parse ChatColor " + string);
	}

	@Deprecated
	public static ChatColor valueOf(String name) {
		Preconditions.checkNotNull(name, "Name is null");
		ChatColor defined = BY_NAME.get(name);
		Preconditions.checkArgument(defined != null, "No enum constant " + ChatColor.class.getName() + "." + name);
		return defined;
	}

	@Deprecated
	public static ChatColor[] values() {
		return BY_CHAR.values().toArray(new ChatColor[BY_CHAR.values().size()]);
	}

	@Deprecated
	public String name() {
		return this.getName().toUpperCase(Locale.ROOT);
	}

	@Deprecated
	public int ordinal() {
		Preconditions.checkArgument(this.ordinal >= 0, "Cannot get ordinal of hex color");
		return this.ordinal;
	}

	public String getName() {
		return this.name;
	}
}