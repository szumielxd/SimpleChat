package me.szumielxd.SimpleChat.format.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import me.szumielxd.SimpleChat.SimpleChat;
import me.szumielxd.SimpleChat.format.TranslatableItem;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.ComponentBuilder;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.HoverEvent;
import me.szumielxd.SimpleChat.libs.bungeechat.api.chat.TextComponent;
import me.szumielxd.SimpleChat.utils.MainUtils.MCVersion;

public class ItemMessageModule extends MessageModule {

	
	//private TranslatableItem item;
	private String displayName;
	private int amount;
	private ArrayList<String> emptyHover = new ArrayList<>();
	private HashMap<MCVersion, TranslatableItem> items = new HashMap<>();
	
	
	public ItemMessageModule(Player sender) {
		ItemStack is = sender.getInventory().getItemInHand();
		if(is == null || is.getType().equals(Material.AIR)) {
			SimpleChat plugin = SimpleChat.getInst();
			this.emptyHover = plugin.getConfigLoader().ITEM_AIR_HOVER.stream().map(line -> plugin.getParser().parse(sender, line)).collect(Collectors.toCollection(ArrayList::new));
			this.displayName = plugin.getParser().parse(sender, plugin.getConfigLoader().ITEM_AIR_FORMAT);
			return;
		}
		System.out.println(new JSONObject(is.serialize()).toJSONString());
		this.amount = is.getAmount();
		this.displayName = is.getItemMeta().getDisplayName();
		if(this.displayName == null) this.displayName = is.getType().name().toLowerCase();
		try {
			MCVersion ver = MCVersion.getDefault();
			TranslatableItem base = new TranslatableItem(is);
			items.put(ver, base);
			
			TranslatableItem latest = base;
			while(ver.isOlderThan(MCVersion.v1_16)) {
				ver = ver.newer();
				if(ver == null/* || !ver.isSupported()*/) break;
				items.put(ver, latest = latest.upgrade());
				System.out.println(ver+" - "+latest);
			}
			
			ver = MCVersion.getDefault();
			latest = base;
			while(ver.isNewerThan(MCVersion.v1_8)) {
				ver = ver.older();
				if(ver == null /*|| !ver.isSupported()*/) break;
				items.put(ver, latest = latest.downgrade());
				System.out.println(ver+" - "+latest);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public TextComponent parse(Player p) {
		if(items.isEmpty()) {
			TextComponent text = new TextComponent(TextComponent.fromLegacyText(displayName));
			if(this.emptyHover.size() > 0) {
				String hover = String.join("\n", emptyHover);
				text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
			}
			return text;
		}
		MCVersion version = MCVersion.getPlayerVersion(p);
		TextComponent text = new TextComponent(SimpleChat.getInst().getConfigLoader().ITEM_FORMAT.replace("%name%", this.displayName).replace("%amount%", this.amount+""));
		String item = items.get(version)+"";
		System.out.println("............. "+version+" -> "+item);
		
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ComponentBuilder(item).create()));
		return text;
	}

}
