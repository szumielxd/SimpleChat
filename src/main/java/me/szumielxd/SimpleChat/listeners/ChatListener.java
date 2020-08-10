package me.szumielxd.SimpleChat.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.ChatType;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import me.szumielxd.SimpleChat.SimpleChat;
import me.szumielxd.SimpleChat.format.DefinedFormatModule;
import me.szumielxd.SimpleChat.format.FormatModule;
import me.szumielxd.SimpleChat.format.message.ItemMessageModule;
import me.szumielxd.SimpleChat.format.message.MentionMessageModule;
import me.szumielxd.SimpleChat.format.message.MessageModule;
import me.szumielxd.SimpleChat.format.message.TextMessageModule;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.BaseComponent;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.ComponentBuilder.FormatRetention;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.TextComponent;
import me.szumielxd.SimpleChat.libs.bungeechat.chat.ComponentSerializer;
import me.szumielxd.SimpleChat.utils.MainUtils.MCVersion;

public class ChatListener implements Listener {

	
	private final ProtocolManager manager;
	private final SimpleChat plugin;
	
	
	public ChatListener(SimpleChat plugin) {
		this.plugin = plugin;
		this.manager = ProtocolLibrary.getProtocolManager();
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent e) {
		final Player p = e.getPlayer();
		ArrayList<FormatModule> modules = plugin.getChatManager().getModules(p);
		ArrayList<DefinedFormatModule> formats = new ArrayList<>();
		for(FormatModule form : modules) {
			if(form.canShow(p)) formats.add(new DefinedFormatModule(form, p));
		}
		ArrayList<MessageModule> message = this.parseMessage(p, e.getMessage());
		e.getRecipients().forEach(rec -> {
			TextComponent text = new TextComponent();
			for(DefinedFormatModule mod : formats) {
				BaseComponent last = (text.getExtra()==null || text.getExtra().isEmpty())? text : text.getExtra().get(text.getExtra().size()-1);
				if(mod.getName().equals("message")) {
					TextComponent msg = new TextComponent();
					message.forEach(module -> msg.addExtra(module.parse(rec)));
					System.out.println("######## "+ComponentSerializer.toString(msg));
					msg.copyFormatting(last, FormatRetention.FORMATTING, false);
					text.addExtra(msg);
				}
				else if(mod.canSee(rec)) {
					TextComponent msg = mod.parse(rec);
					msg.copyFormatting(last, FormatRetention.FORMATTING, false);
					text.addExtra(msg);
				}
			}
			System.out.println("\\\\\\\\\\\\\\\\\\\\\\ "+ComponentSerializer.toString(text));
			//rec.spigot().send
			try {
				sendChat(rec, text);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		e.getRecipients().clear();
	}
	
	
	public TextComponent colorize(TextComponent text, BaseComponent inherit) {
		
		if(inherit.getExtra()!=null && !inherit.getExtra().isEmpty()) inherit = inherit.getExtra().get(inherit.getExtra().size()-1);
		text.setBold(inherit.isBold());
		text.setItalic(inherit.isItalic());
		text.setStrikethrough(inherit.isStrikethrough());
		text.setUnderlined(inherit.isUnderlined());
		text.setObfuscated(inherit.isObfuscated());
		text.setColor(inherit.getColor());
		
		return text;
	}
	
	
	public void sendChat(Player p, BaseComponent message) throws Exception {
		
		if(message instanceof TextComponent && !MCVersion.getPlayerVersion(p).isNewerEqualThan(MCVersion.v1_16)) {
			message = oldColors((TextComponent) message);
		}
		/*final Class<?> chatSerializer = MCVersion.getDefault().getNetNMSClass("IChatBaseComponent$ChatSerializer");
		final Class<?> iChatBaseComponent = MCVersion.getDefault().getNetNMSClass("IChatBaseComponent");
		final Object comp = MainUtils.methInvoke(chatSerializer, null, "a", ComponentSerializer.toString(message));
		final Class<?> craftPlayer = MCVersion.getDefault().getCraftNMSClass("entity.CraftPlayer");
		final Object handle = MainUtils.methInvoke(craftPlayer, p, "getHandle", new Object[0]);
		final Object conn = MainUtils.getField(handle, "playerConnection");
		final Class<?> packetPlayOutChat = MCVersion.getDefault().getNetNMSClass("PacketPlayOutChat");
		final Class<?> packetClass = MCVersion.getDefault().getNetNMSClass("Packet");
		final Object packet = packetPlayOutChat.getConstructor(iChatBaseComponent).newInstance(comp);
		Bukkit.getScheduler().runTask(this.plugin, new Runnable() {
			@Override
			public void run() {
				MainUtils.methInvoke(conn.getClass(), conn, "sendPacket", new Class<?>[] {packetClass},  new Object[] {packet});
				new ComponentSerializer().
			}
		});
		*/
		PacketContainer chat = new PacketContainer(PacketType.Play.Server.CHAT);
		WrappedChatComponent ch = WrappedChatComponent.fromJson(ComponentSerializer.toString(message));
		chat.getChatComponents().write(0, ch);
		chat.getChatTypes().write(0, ChatType.CHAT);
		//chat.getBytes().write(0, (byte) 0);
		manager.sendServerPacket(p, chat);
	}
	
	
	public TextComponent oldColors(TextComponent text) {
		me.szumielxd.SimpleChat.libs.bungeechat.api.ChatColor c = text.getColorRaw();
		text = text.duplicate();
		if(c != null)text.setColor(c.getOldFormat());
		if(text.getExtra() != null)text.getExtra().replaceAll(extra -> {
			if(extra instanceof BaseComponent) return oldColors((TextComponent) extra);
			return extra.duplicate();
		});
		return text;
	}
	
	
	//public TextComponent colorize(TextComponent text, String pattern) {
		/*String[] colors = pattern.split("§");
		if(colors.length > 0) for(String color : colors) {
			switch(color.charAt(0)) {
				case 'l': {
					text.setBold(true);
					break;
				}
				case 'o': {
					text.setItalic(true);
					break;
				}
				case 'm': {
					text.setStrikethrough(true);
					break;
				}
				case 'n': {
					text.setUnderlined(true);
					break;
				}
				case 'k': {
					text.setObfuscated(true);
					break;
				}
				case 'r': {
					text.setBold(false);
					text.setItalic(false);
					text.setStrikethrough(false);
					text.setUnderlined(false);
					text.setObfuscated(false);
					break;
				}
				default: {
					me.szumielxd.SimpleChat.libs.bungeechat.api.ChatColor c = me.szumielxd.SimpleChat.libs.bungeechat.api.ChatColor.getByChar(color.charAt(0));
					if(c != null) text.setColor(c);
					break;
				}
			}
		}
		return text;
		
	}*/
	
	
	public ArrayList<MessageModule> parseChatMention(Player sender, ArrayList<MessageModule> modules) {
		
		ArrayList<MessageModule> newList = new ArrayList<>();
		if(modules.size() == 0) return newList;
		
		for(MessageModule module : modules) {
			
			if(!(module instanceof TextMessageModule)) {
				newList.add(module);
				continue;
			}
			boolean first = true;
			StringBuilder main = new StringBuilder();
			String[] words = module.toString().split(" ");
			ArrayList<String> text = new ArrayList<>();
			if(module.toString().startsWith(" ")) text.add("");
			for(int i=0; i<words.length; i++) {
				final String raw = words[i].replaceAll("[^a-zA-Z0-9_& ]", "").replaceAll("§[a-fA-F0-9]", "");
				Player target = Bukkit.getPlayerExact(raw);
				if(target == null) {
					if(text.isEmpty()) text.add(ChatColor.getLastColors(main.toString()).concat(words[i]));
					else text.add(words[i]);
				} else {
					if(!text.isEmpty()) {
						newList.add(new TextMessageModule((!first? " " : "").concat(String.join(" ", text))));
						first = false;
						text.clear();
					}
					newList.add(new MentionMessageModule(target, (!first? " " : "").concat(ChatColor.getLastColors(main.toString())).concat(words[i])));
					first = false;
				}
				main.append(words[i]);
			}
			if(module.toString().endsWith(" ")) text.add("");
			if(!text.isEmpty()) {
				newList.add(new TextMessageModule((!first? " " : "").concat(String.join(" ", text))));
				first = false;
				text.clear();
			}
			
		}
		
		return newList;
		
	}
	
	
	public ArrayList<MessageModule> parseChatItem(Player sender, ArrayList<MessageModule> modules) {
		
		ArrayList<MessageModule> newList = new ArrayList<>();
		if(modules.size() == 0) return newList;
		
		ArrayList<String> patterns = new ArrayList<>(plugin.getConfigLoader().ITEM_PATTERN);
		if(patterns.size() == 0) return new ArrayList<>(modules);
		patterns.replaceAll(Pattern::quote);
		String pattern = "(".concat(String.join("|", patterns)).concat(")");
		Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Pattern endRegex = Pattern.compile(".*".concat(pattern).concat("$"), Pattern.CASE_INSENSITIVE);
		
		ItemMessageModule item = null;
		for(MessageModule module : modules) {
			
			if(!(module instanceof TextMessageModule)) {
				newList.add(module);
				continue;
			}
			String msg = module.toString();
			String[] args = regex.split(msg);
			if(args.length > 0) newList.add(new TextMessageModule(args[0]));
			if(args.length < 2 && !endRegex.matcher(msg).matches()) {
				continue;
			}
			StringBuilder main = new StringBuilder(args.length>0? args[0] : "");
			if(args.length>0) args = Arrays.copyOfRange(args, 1, args.length);
			if(args.length>0)for(String element : args) {
				if(item == null) item = new ItemMessageModule(sender);
				newList.add(item);
				newList.add(new TextMessageModule(ChatColor.getLastColors(main.toString()).concat(element)));
				main.append(element);
			}
			if(endRegex.matcher(msg).matches()) {
				if(item == null) item = new ItemMessageModule(sender);
				newList.add(item);
			}
			
		};
		return newList;
	}
	
	public ArrayList<MessageModule> parseMessage(Player sender, String msg) {
		ArrayList<MessageModule> modules = new ArrayList<>(Arrays.asList(new TextMessageModule(msg)));
		modules = this.parseChatItem(sender, modules);
		modules = this.parseChatMention(sender, modules);
		return modules;
	}
	
	
}
