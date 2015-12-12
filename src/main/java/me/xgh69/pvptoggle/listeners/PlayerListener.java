package me.xgh69.pvptoggle.listeners;

import java.util.ArrayList;
import java.util.List;

import me.xgh69.pvptoggle.PvpManager;
import me.xgh69.pvptoggle.PvpToggle;
import me.xgh69.pvptoggle.PvpUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
			boolean pvp = utils.getSettings("pvp_on_first_join");
			pvpmanager.setPvp(player.getUniqueId(), pvp);
			player.sendMessage(utils.getMessage("first_join").replace("$player", player.getName()));
		}
		
		if(pvpmanager.isFight(player.getName()))
		{
			player.sendMessage(utils.getMessage("logout_join").replace("$player", player.getName()));
			pvpmanager.setFight(player.getName(), false, -1);
		}
		
		if(pvpmanager.getPvp(player.getUniqueId()))
		{
			player.sendMessage(utils.getMessage("join").replace("$player", player.getName()) + utils.getMessage("join_enable").replace("$player", player.getName()));
		}
		else
		{
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
				pvpmanager.setFight(player.getName(), false, -1);
				player.sendMessage(utils.getMessage("stopfight").replace("$player", player.getName()));
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
				pvpmanager.setFight(player.getName(), false, -1);
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
	
	@SuppressWarnings({ "deprecation" })
	@EventHandler
	public void onLeft(PlayerQuitEvent evt)
	{
		Player player = evt.getPlayer();
		
		if(pvpmanager.isFight(player.getName()))
		{
			long i = pvpmanager.getFightTime(player.getName());
			Bukkit.broadcastMessage(utils.getMessage("logout_left").replace("$player", player.getName()));
			player.setHealth(0.00D);
			
			for(Object o : pvpmanager.getPlayersFights())
			{
				String key = (String) o;
				if(pvpmanager.getFightTime(key) == i)
				{
					pvpmanager.setFight(key, false, -1);
					Player target = Bukkit.getPlayer(key);
					target.sendMessage(utils.getMessage("logout_left_stopfight").replace("$player", target.getName()));
					break;
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent evt)
	{
		if(evt.getAction() == Action.RIGHT_CLICK_AIR && evt.getMaterial() == Material.ENDER_PEARL)
		{
			Player player = evt.getPlayer();
			evt.setCancelled(true);
			player.sendMessage(utils.getMessage("fight_enderpearl"));
		}
	}
	
	
	
}
