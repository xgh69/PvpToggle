package me.xgh69.pvptoggle.listeners;

import me.xgh69.pvptoggle.PvpManager;
import me.xgh69.pvptoggle.PvpToggle;
import me.xgh69.pvptoggle.PvpUtils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class EntityListener implements Listener
{
	
	private PvpToggle plugin;
	private PvpManager pvpmanager;
	private PvpUtils utils;
	
	public EntityListener()
	{
		plugin = PvpToggle.getInstance();
		pvpmanager = plugin.getPvpManager();
		utils = plugin.getUtils();
		
		plugin.getLogger().info("Initialized EntityListener.");
	}
	
	@EventHandler
	@SuppressWarnings("deprecation")
	public void onDamageByEntity(EntityDamageByEntityEvent evt)
	{
		if(!(evt.getDamager() instanceof Player) || !(evt.getEntity() instanceof Player))
			return;
		
		final Player victim = (Player) evt.getEntity();
		final Player damager = (Player) evt.getDamager();
		victim.setFlying(false);
		damager.setFlying(false);
		victim.setGameMode(GameMode.SURVIVAL);
		damager.setGameMode(GameMode.SURVIVAL);
		RegionManager rm = WorldGuardPlugin.inst().getRegionManager(damager.getWorld());
		ApplicableRegionSet set = rm.getApplicableRegions(victim.getLocation());
		
		if(set.getFlag(DefaultFlag.PVP) != State.ALLOW)
		{
			if(set.getFlag(DefaultFlag.PVP) == State.DENY)
			{
				evt.setCancelled(true);
				return;
			}
			
			if(pvpmanager.getPvp(victim.getUniqueId()) || pvpmanager.getPvp(damager.getUniqueId()))
			{
				damager.sendMessage(utils.getMessage("player_protected").replace("$player", victim.getName()));
				evt.setCancelled(true);
				return;
			}
			else
			{
				evt.setCancelled(false);
				if(!pvpmanager.isFight(victim.getName()))
					victim.sendMessage(utils.getMessage("player_damaged").replace("$player", damager.getName()));
				
				if(!pvpmanager.isFight(damager.getName()))
					damager.sendMessage(utils.getMessage("player_damager").replace("$player", victim.getName()));
				
				long time = utils.getTimeStamp();
				pvpmanager.setFight(damager.getName(), true, time);
				pvpmanager.setFight(victim.getName(), true, time);
				return;
			}
		}
		else if(set.getFlag(DefaultFlag.PVP) == State.ALLOW)
		{
			evt.setCancelled(false);
			if(!pvpmanager.isFight(victim.getName()))
				victim.sendMessage(utils.getMessage("player_damaged").replace("$player", damager.getName()));
			
			if(!pvpmanager.isFight(damager.getName()))
				damager.sendMessage(utils.getMessage("player_damager").replace("$player", victim.getName()));
			long time = utils.getTimeStamp();
			pvpmanager.setFight(damager.getName(), true, time);
			pvpmanager.setFight(victim.getName(), true, time);
			return;
		}
	}
}
