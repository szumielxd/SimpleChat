package me.szumielxd.SimpleChat.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.szumielxd.SimpleChat.SimpleChat;
import me.szumielxd.SimpleChat.format.FormatModule;
import me.szumielxd.SimpleChat.format.TextLineAccess;

public class ChatFormatManager {
	
	
	private YamlConfiguration conf = new YamlConfiguration();
	private HashMap<String, ArrayList<FormatModule>> moduleList = new HashMap<>();
	
	
	public ChatFormatManager(File f) {
		try {
			FileInputStream fis = new FileInputStream(f);
			this.conf.load(new InputStreamReader(fis, Charset.forName("UTF-8")));
			if(createDefaultFormat(f)) {
				fis = new FileInputStream(f);
				this.conf.load(new InputStreamReader(fis, Charset.forName("UTF-8")));
			}
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		ConfigurationSection groups = this.conf.getConfigurationSection("groups");
		moduleList = new HashMap<>();
		groups.getKeys(false).forEach(group -> {
			ConfigurationSection section = groups.getConfigurationSection(group);
			if(section.isSet("format")) {
				String format = section.getString("format").trim();
				ArrayList<FormatModule> modules = new ArrayList<>();
				boolean message = false;
				Bukkit.getLogger().info(String.format("####### format: %s", format));
				for(String mod : format.toLowerCase().split(",")) {
					Bukkit.getLogger().info(String.format("####### loop: %s (%s)", mod, section.getCurrentPath()));
					if(mod.equals("message")) {
						FormatModule form = new FormatModule(mod, "message");
						modules.add(form);
						message = true;
						continue;
					}
					if(!section.isConfigurationSection(String.format("modules.%s", mod))) continue;
					ConfigurationSection module = section.getConfigurationSection(String.format("modules.%s", mod));
					Bukkit.getLogger().info(String.format("####### %s -> DONE#1 (%s)", mod, module.getCurrentPath()));
					if(!module.isSet("text")) continue;
					Bukkit.getLogger().info(String.format("####### %s -> DONE#2", mod));
					FormatModule form = new FormatModule(mod, module.getString("text"));
					
					if(module.isSet("hover")) {
						ArrayList<TextLineAccess> list = new ArrayList<>();
						for(String line : module.getStringList("hover")) list.add(new TextLineAccess(line));
						form.setHover(list);
					}
					if(module.isSet("see-permission")) form.setSeePermission(module.getString("see-permission"));
					if(module.isSet("show-permission")) form.setShowPermission(module.getString("show-permission"));
					if(module.isSet("command")) form.setCommand(new TextLineAccess(module.getString("command")));
					if(module.isSet("url")) form.setUrl(new TextLineAccess(module.getString("url")));
					if(module.isSet("suggest")) form.setSuggestCommand(new TextLineAccess(module.getString("suggest")));
					modules.add(form);
				}
				if(!message) {
					FormatModule form = new FormatModule("message", "message");
					modules.add(form);
				}
				moduleList.put(group.toLowerCase(), modules);
			}
		});
		
	}
	
	
	public boolean createDefaultFormat(File f) throws IOException {
		if(!conf.isConfigurationSection("groups")) conf.createSection("groups");
		if(conf.getConfigurationSection("groups").isConfigurationSection("default")) return false;
		ConfigurationSection def = conf.getConfigurationSection("groups").createSection("default");
		def.set("format", "prefix,nick,suffix,color,message");
		ConfigurationSection modules = def.createSection("modules");
		{
			ConfigurationSection sec = modules.createSection("prefix");
			sec.set("text", "%vault_prefix%");
			sec.set("hover", Arrays.asList("&7Rank: &b%vault_group%"));
		}
		{
			ConfigurationSection sec = modules.createSection("nick");
			sec.set("text", "%displayname%");
			sec.set("hover", Arrays.asList("&7Nick: &b%name%", "&7Konto: %vault_eco%", "{view:simplechat.ip}&7IP: &b%ip%"));
			sec.set("command", "/tpa %name%");
		}
		{
			ConfigurationSection sec = modules.createSection("suffix");
			sec.set("text", "%vault_suffix%");
		}
		{
			ConfigurationSection sec = modules.createSection("color");
			sec.set("text", "&r &8&l»&7 ");
		}
		conf.save(f);
		return true;
	}
	
	
	public ArrayList<FormatModule> getModules(Player sender) {
		String group = SimpleChat.getInst().getPermissionManager().getGroup(sender);
		if(group == null) group = "default";
		group = group.toLowerCase();
		return this.moduleList.getOrDefault(group, this.moduleList.get("default"));
	}
	
	

}
