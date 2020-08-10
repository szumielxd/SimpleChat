package me.szumielxd.SimpleChat.managers;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsService;

import ca.stellardrift.permissionsex.PermissionsEx;
import ca.stellardrift.permissionsex.bukkit.PermissionsExPlugin;
import ca.stellardrift.permissionsex.subject.CalculatedSubject;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.milkbowl.vault.permission.Permission;

public class PermissionManager {
	
	
	private PermType type = PermType.VANILLA;
	private Permission vaultPerm;
	private LuckPerms luckPerm;
	private PermissionsExPlugin pexPerm;
	private ZPermissionsService zPerm;
	
	
	public PermissionManager() {
		
		if(Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
			type = PermType.LUCKPERMS;
		} else if(Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
			type = PermType.PERMISSIONSEX;
		} else if(Bukkit.getPluginManager().getPlugin("PermissionsEx") != null) {
			type = PermType.ZPERMISSIONS;
		}else if(Bukkit.getPluginManager().getPlugin("zPermissions") != null) {
			type = PermType.VAULT;
		} else {
			type = PermType.VANILLA;
		}
		this.setup();
		
	}
	
	
	private void setup() {
		switch(type) {
			case LUCKPERMS:{
				luckPerm = LuckPermsProvider.get();
				break;
			}
			case PERMISSIONSEX:{
				pexPerm = (PermissionsExPlugin) Bukkit.getPluginManager().getPlugin("PermissionsEx");
				break;
			}
			case VANILLA:{
				break;
			}
			case VAULT:{
				RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);
		        vaultPerm = rsp.getProvider();
				break;
			}
			case ZPERMISSIONS:{
				RegisteredServiceProvider<ZPermissionsService> rsp = Bukkit.getServicesManager().getRegistration(ZPermissionsService.class);
		        zPerm = rsp.getProvider();
				break;
			}
			default:{
				break;
			}
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public String getGroup(Player p) {
		if(p == null || !p.isOnline()) return null;
		switch(type) {
			case LUCKPERMS:{
				net.luckperms.api.model.user.User u = luckPerm.getUserManager().getUser(p.getUniqueId());
				return u!=null? u.getPrimaryGroup() : "default";
			}
			case PERMISSIONSEX:{
				CompletableFuture<CalculatedSubject> fut = pexPerm.getUserSubjects().get(p.getUniqueId().toString());
				try {
					String[] groups = fut.get().getParents().stream().filter(parent -> parent.getKey().equals(PermissionsEx.SUBJECTS_GROUP)).map(Map.Entry::getValue).toArray(String[]::new);
					return groups.length>0? groups[0] : "default";
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			case VANILLA:{
				return "default";
			}
			case VAULT:{
		        return vaultPerm.getPrimaryGroup(null, p);
			}
			case ZPERMISSIONS:{
				return zPerm.getPlayerPrimaryGroup(p.getName());
			}
			default:{
				return "default";
			}
		}
	}
	
	
	public static enum PermType {
		
		LUCKPERMS,
		PERMISSIONSEX,
		ZPERMISSIONS,
		VAULT,
		VANILLA;
		
	}
	

}
