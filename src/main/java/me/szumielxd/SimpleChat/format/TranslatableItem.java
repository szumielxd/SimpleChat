package me.szumielxd.SimpleChat.format;

import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import me.szumielxd.SimpleChat.translation.Translate_112_113;
import me.szumielxd.SimpleChat.translation.Translate_113_114;
import me.szumielxd.SimpleChat.translation.Translate_114_115;
import me.szumielxd.SimpleChat.translation.Translate_115_116;
import me.szumielxd.SimpleChat.utils.MainUtils;
import me.szumielxd.SimpleChat.utils.MainUtils.MCVersion;

public class TranslatableItem {
	
	private byte count;
	private short damage;
	private MCVersion version;
	private JSONObject tag;
	private String namespace;
	
	public TranslatableItem(String namespace, byte count, short damage, JSONObject tag, MCVersion version) {
		this.count = count;
		this.damage = damage;
		this.version = version;
		this.tag = tag;
		this.namespace = namespace;
	}
	
	public byte getCount() {
		return this.count;
	}
	public short getDamage() {
		return this.damage;
	}
	public MCVersion getVersion() {
		return this.version;
	}
	public JSONObject getTag() {
		return MainUtils.jsonObjectDeepClone(this.tag);
	}
	public String getNamespace() {
		return new String(this.namespace);
	}
	
	public TranslatableItem(ItemStack item) throws Exception {
		Object nbt = MCVersion.getDefault().getNetNMSClass("NBTTagCompound").newInstance();
		Object nms = MainUtils.getNMSItemStack(item);
		nbt = MainUtils.methInvoke(nms.getClass(), nms, "save", nbt);
		JSONObject json = MainUtils.getNBTToJSON(nbt);
		this.namespace = (String) json.get("id");
		this.count = (byte) item.getAmount();
		this.damage = item.getDurability();
		this.version = MCVersion.getDefault();
		this.tag = (JSONObject) json.get("tag");
		
	}
	
	
	
	
	public TranslatableItem downgrade() {
		switch(version) {
			case UNKNOWN:
				return null;
			case v1_8:
				return null;
			case v1_9:
				return new TranslatableItem(this.getNamespace(), this.getCount(), this.getDamage(), this.getTag(), MCVersion.v1_8);
			case v1_10:
				return new TranslatableItem(this.getNamespace(), this.getCount(), this.getDamage(), this.getTag(), MCVersion.v1_9);
			case v1_11:
				return new TranslatableItem(this.getNamespace(), this.getCount(), this.getDamage(), this.getTag(), MCVersion.v1_10);
			case v1_12:
				return new TranslatableItem(this.getNamespace(), this.getCount(), this.getDamage(), this.getTag(), MCVersion.v1_11);
			case v1_13:
				return Translate_112_113.get(false).downgrade(this);
			case v1_14:
				return Translate_113_114.get(false).downgrade(this);
			case v1_15:
				return Translate_114_115.get(false).downgrade(this);
			case v1_16:
				return Translate_115_116.get(false).downgrade(this);
			default:
				return null;
			
		}
	}
	
	
	public TranslatableItem upgrade() {
		switch(version) {
			case UNKNOWN:
				return null;
			case v1_8:
				return new TranslatableItem(this.getNamespace(), this.getCount(), this.getDamage(), this.getTag(), MCVersion.v1_9);
			case v1_9:
				return new TranslatableItem(this.getNamespace(), this.getCount(), this.getDamage(), this.getTag(), MCVersion.v1_10);
			case v1_10:
				return new TranslatableItem(this.getNamespace(), this.getCount(), this.getDamage(), this.getTag(), MCVersion.v1_11);
			case v1_11:
				return new TranslatableItem(this.getNamespace(), this.getCount(), this.getDamage(), this.getTag(), MCVersion.v1_12);
			case v1_12:
				return Translate_112_113.get(false).upgrade(this);
			case v1_13:
				return Translate_113_114.get(false).upgrade(this);
			case v1_14:
				return Translate_114_115.get(false).upgrade(this);
			case v1_15:
				return Translate_115_116.get(false).upgrade(this);
			case v1_16:
				return null;
			default:
				return null;
			
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		JSONObject json = new JSONObject();
		json.put("id", this.namespace);
		json.put("Count", this.count);
		if(this.damage != 0) json.put("Damage", this.damage);
		if(this.tag != null) json.put("tag", this.tag);
		String str = MainUtils.jsonToNBTString(json);
		System.out.println("::::::::::::"+str);
		return str;
	}

}
