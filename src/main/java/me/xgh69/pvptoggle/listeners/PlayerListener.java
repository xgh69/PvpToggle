package me.xgh69.pvptoggle.listeners;

import java.util.ArrayList;
import java.util.List;

import me.xgh69.pvptoggle.PvpManager;
import me.xgh69.pvptoggle.PvpToggle;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
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
	private List<String> inPvp = new ArrayList<String>();
	private PvpToggle plugin = PvpToggle.getInstance();
	private PvpManager pvpmanager = plugin.getPvpManager();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent evt)
	{
		Player player = evt.getPlayer();
		if(!pvpmanager.containsPvpSettings(player.getUniqueId()))
		{
			pvpmanager.setPvpProtection(player.getUniqueId(), true);
			player.sendMessage(plugin.getMessage("first_join").replace("$player", player.getName()));
		}
		
		if(pvpmanager.isInFight(player.getName()))
		{
			player.sendMessage(plugin.getMessage("logout_join").replace("$player", player.getName()));
			pvpmanager.setInFight(player.getName(), false);
		}
	}
	
	@EventHandler
	@SuppressWarnings("deprecation")
	public void onMove(PlayerMoveEvent evt)
	{
		Player player = evt.getPlayer();
		
		if(pvpmanager.isInFight(player.getName()))
		{
			if(pvpmanager.isTimeOver(player.getName()))
			{
				pvpmanager.setInFight(player.getName(), false);
				player.sendMessage(plugin.getMessage("stopfight").replace("$player", player.getName()));
			}
		}
		
		RegionManager rm = WorldGuardPlugin.inst().getRegionManager(player.getWorld());
		ApplicableRegionSet set = rm.getApplicableRegions(player.getLocation());
		if(set.getFlag(DefaultFlag.PVP) == State.ALLOW)
		{
			if(!inPvp.contains(player.getName()))
			{
				player.sendMessage(plugin.getMessage("cuboid_pvponly_enter").replace("$player", player.getName()));
				inPvp.add(player.getName());
			}
		}
		else
		{
			if(inPvp.contains(plugin.getName()))
			{
				player.sendMessage(plugin.getMessage("cuboid_pvponly_exit").replace("$player", player.getName()));
				inPvp.remove(plugin.getName());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent evt)
	{
		Player player = evt.getPlayer();
		if(pvpmanager.isInFight(player.getName()))
		{
			if(pvpmanager.isTimeOver(player.getName()))
			{
				pvpmanager.setInFight(player.getName(), false);
			}
			else
			{
				if(!pvpmanager.isAllowedCommand(evt.getMessage()))
				{
					player.sendMessage(plugin.getMessage("cmd_infight").replace("$player", player.getName()));
					evt.setCancelled(true);
				}
				else
				{
					evt.setCancelled(false);
				}
			}
		}
		
		if(evt.getMessage().startsWith("/pvp"))
		{
			RegionManager rm = WorldGuardPlugin.inst().getRegionManager(player.getWorld());
			ApplicableRegionSet set = rm.getApplicableRegions(player.getLocation());
		
			if(set.getFlag(DefaultFlag.PVP) == State.ALLOW)
			{
				player.sendMessage(plugin.getMessage("cuboid_pvponly_cmd_block").replace("$player", player.getName()));
				evt.setCancelled(true);
			}
		}
	}
	
	@SuppressWarnings({ "deprecation" })
	@EventHandler
	public void onLeft(PlayerQuitEvent evt)
	{
		Player player = evt.getPlayer();
		PvpToggle plugin = PvpToggle.getInstance();
		
		if(pvpmanager.isInFight(player.getName()))
		{
			int i = pvpmanager.getFightTime(player.getName());
			Bukkit.broadcastMessage(plugin.getMessage("logout_left").replace("$player", player.getName()));
			player.setHealth(0.00D);
			
			for(Object o : pvpmanager.getPlayersInFight())
			{
				String key = (String) o;
				if(pvpmanager.getFightTime(key) == i)
				{
					pvpmanager.setInFight(key, false);
					Player target = Bukkit.getPlayer(key);
					target.sendMessage(plugin.getMessage("logout_left_stopfight").replace("$player", target.getName()));
					break;
				}
			}
		}
	}
	
	
}
