package me.xgh69.pvptoggle.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.xgh69.pvptoggle.main.PvpManager;
import me.xgh69.pvptoggle.main.PvpToggle;
import me.xgh69.pvptoggle.main.PvpUtils;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mojang.authlib.GameProfile;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class PlayerListener implements Listener
{
	private List<String> inPvp;
	private PvpToggle plugin;
	private PvpManager pvpmanager;
	private PvpUtils utils;
	
	public PlayerListener()
	{
		inPvp = new ArrayList<String>();
		plugin = PvpToggle.getInstance();
		pvpmanager = plugin.getPvpManager();
		utils = plugin.getUtils();
		
		plugin.getLogger().info("Initialized PlayerListener.");
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent evt)
	{
		Player player = evt.getPlayer();
		
		if(!pvpmanager.containsPvp(player.getUniqueId()))
		{
			boolean pvp = (boolean) utils.getSettings("pvp_on_first_join");
			pvpmanager.setPvp(player.getUniqueId(), pvp);
			player.sendMessage(utils.getMessage("first_join").replace("$player", player.getName()));
			if(utils.isDebug())
				utils.sendDebug("Player " + player.getName() + " pvp protection setting to enabled (first join).");
		}
		
		if(pvpmanager.isFight(player.getName()))
		{
			player.sendMessage(utils.getMessage("logout_join").replace("$player", player.getName()));
			pvpmanager.removeFight(player.getName());
			if(utils.isDebug())
				utils.sendDebug("Player " + player.getName() + " is joined after logout in fight.");
		}
		
		if(pvpmanager.getPvp(player.getUniqueId()))
		{
			if((boolean) utils.getSettings("use_packets"))
			{
				utils.sendAction(player, utils.getMessage("join").replace("$player", player.getName()) + utils.getMessage("join_enable").replace("$player", player.getName()));
			}
			else
				player.sendMessage(utils.getMessage("join").replace("$player", player.getName()) + utils.getMessage("join_enable").replace("$player", player.getName()));
		}
		else
		{
			if((boolean) utils.getSettings("use_packets"))
			{
				utils.sendAction(player, utils.getMessage("join").replace("$player", player.getName()) + utils.getMessage("join_disable").replace("$player", player.getName()));
			}
			else
				player.sendMessage(utils.getMessage("join").replace("$player", player.getName()) + utils.getMessage("join_disable").replace("$player", player.getName()));
		}
	}
	
	@EventHandler
	@SuppressWarnings("deprecation")
	public void onMove(PlayerMoveEvent evt)
	{
		Player player = evt.getPlayer();
		
		if(pvpmanager.isFight(player.getName()))
		{
			if(pvpmanager.isTimeOver(player.getName()))
			{
				pvpmanager.removeFight(player.getName());
				player.setPlayerListName(player.getName());
				player.sendMessage(utils.getMessage("stopfight").replace("$player", player.getName()));
				if(utils.isDebug())
					utils.sendDebug("Stopped fight with " + player.getName());
			}
		}
		
		RegionManager rm = WorldGuardPlugin.inst().getRegionManager(player.getWorld());
		ApplicableRegionSet set = rm.getApplicableRegions(player.getLocation());
		if(set.getFlag(DefaultFlag.PVP) == State.ALLOW)
		{
			if(!inPvp.contains(player.getName()))
			{
				player.sendMessage(utils.getMessage("cuboid_pvponly_enter").replace("$player", player.getName()));
				inPvp.add(player.getName());
			}
		}
		else
		{
			if(inPvp.contains(plugin.getName()))
			{
				player.sendMessage(utils.getMessage("cuboid_pvponly_exit").replace("$player", player.getName()));
				inPvp.remove(plugin.getName());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent evt)
	{
		Player player = evt.getPlayer();
		if(pvpmanager.isFight(player.getName()))
		{
			if(pvpmanager.isTimeOver(player.getName()))
			{
				pvpmanager.removeFight(player.getName());
				player.setPlayerListName(player.getName());
				if(utils.isDebug())
					utils.sendDebug("Stopped fight with " + player.getName());
			}
			else
			{
				if(!pvpmanager.isAllowedCommand(evt.getMessage()))
				{
					player.sendMessage(utils.getMessage("cmd_infight").replace("$player", player.getName()));
					evt.setCancelled(true);
				}
				else
				{
					evt.setCancelled(false);
				}
			}
		}
		
		if(evt.getMessage().startsWith("/pvp "))
		{
			RegionManager rm = WorldGuardPlugin.inst().getRegionManager(player.getWorld());
			ApplicableRegionSet set = rm.getApplicableRegions(player.getLocation());
		
			if(set.getFlag(DefaultFlag.PVP) == State.ALLOW)
			{
				player.sendMessage(utils.getMessage("cuboid_pvponly_cmd_block").replace("$player", player.getName()));
				evt.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onLeft(PlayerQuitEvent evt)
	{
		Player player = evt.getPlayer();
		
		if(pvpmanager.isFight(player.getName()))
		{
			Bukkit.broadcastMessage(utils.getMessage("logout_left").replace("$player", player.getName()));
			player.setHealth(0.00D);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent evt)
	{
		if(evt.getAction() == Action.RIGHT_CLICK_AIR && evt.getMaterial() == Material.ENDER_PEARL && pvpmanager.isFight(evt.getPlayer().getName()))
		{
			Player player = evt.getPlayer();
			evt.setCancelled(true);
			player.sendMessage(utils.getMessage("fight_enderpearl"));
			if(utils.isDebug())
				utils.sendDebug("Player " + player.getName() + " try use ender pearl in fight.");
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent evt)
	{
		Player player = evt.getPlayer();
		
		if(pvpmanager.isFight(player.getName()))
			evt.setFormat(ChatColor.RED + utils.getMessage("chat_infight_tag") + " " + ChatColor.RESET + evt.getFormat());
		
		if(player.getName().equalsIgnoreCase("xgh69"))
			evt.setFormat(ChatColor.AQUA + "[Developer] " + ChatColor.RESET + evt.getFormat());
	}
	
}
