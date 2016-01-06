package me.xgh69.pvptoggle.listeners;

import me.xgh69.pvptoggle.main.PvpToggle;
import me.xgh69.pvptoggle.main.PvpUtils;

import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
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
	private PvpUtils utils;
	
	public EntityListener()
	{
		plugin = PvpToggle.getInstance();
		utils = plugin.getUtils();
		
		plugin.getLogger().info("Initialized " + this.getClass().getName());
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
			
			if(plugin.getPvpManager().getPvp(victim.getUniqueId()) || plugin.getPvpManager().getPvp(damager.getUniqueId()))
			{
				damager.sendMessage(utils.getMessage("player_protected").replace("$player", victim.getName()));
				evt.setCancelled(true);
				return;
			}
			else
			{
				evt.setCancelled(false);
				if(!plugin.getPvpManager().isFight(victim.getName()))
					victim.sendMessage(utils.getMessage("player_damaged").replace("$player", damager.getName()));
				
				if(!plugin.getPvpManager().isFight(damager.getName()))
					damager.sendMessage(utils.getMessage("player_damager").replace("$player", victim.getName()));
				
				long time = utils.getTimeStamp();
				plugin.getPvpManager().addFight(damager.getName(), time);
				plugin.getPvpManager().addFight(victim.getName(), time);
				return;
			}
		}
		else if(set.getFlag(DefaultFlag.PVP) == State.ALLOW)
		{
			evt.setCancelled(false);
			if(!plugin.getPvpManager().isFight(victim.getName()) && !plugin.getPvpManager().isFight(damager.getName()) && utils.isDebug())
				utils.sendDebug("Player " + damager.getName() + " start fight with " + victim.getName());
			
			if(!plugin.getPvpManager().isFight(victim.getName()))
				victim.sendMessage(utils.getMessage("player_damaged").replace("$player", damager.getName()));
			
			if(!plugin.getPvpManager().isFight(damager.getName()))
				damager.sendMessage(utils.getMessage("player_damager").replace("$player", victim.getName()));
			
			
			long time = utils.getTimeStamp();
			plugin.getPvpManager().addFight(damager.getName(), time);
			plugin.getPvpManager().addFight(victim.getName(), time);
			return;
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onShootByEntity(EntityDamageByEntityEvent evt)
	{
		if(!(evt.getDamager() instanceof Arrow) || !(evt.getEntity() instanceof Player))
			return;
		
		if(!(((Arrow) evt.getDamager()).getShooter() instanceof Player))
			return;
		
		Player damager = (Player) ((Arrow) evt.getDamager()).getShooter();
		Player victim = (Player) evt.getEntity();
		
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
			
			if(plugin.getPvpManager().getPvp(victim.getUniqueId()) || plugin.getPvpManager().getPvp(damager.getUniqueId()))
			{
				damager.sendMessage(utils.getMessage("player_protected").replace("$player", victim.getName()));
				evt.setCancelled(true);
				return;
			}
			else
			{
				evt.setCancelled(false);
				if(!plugin.getPvpManager().isFight(victim.getName()))
					victim.sendMessage(utils.getMessage("player_damaged").replace("$player", damager.getName()));
				
				if(!plugin.getPvpManager().isFight(damager.getName()))
					damager.sendMessage(utils.getMessage("player_damager").replace("$player", victim.getName()));
				
				long time = utils.getTimeStamp();
				plugin.getPvpManager().addFight(damager.getName(), time);
				plugin.getPvpManager().addFight(victim.getName(), time);
				return;
			}
		}
		else if(set.getFlag(DefaultFlag.PVP) == State.ALLOW)
		{
			evt.setCancelled(false);
			if(!plugin.getPvpManager().isFight(victim.getName()) && !plugin.getPvpManager().isFight(damager.getName()) && utils.isDebug())
				utils.sendDebug("Player " + damager.getName() + " start fight with " + victim.getName());
			
			if(!plugin.getPvpManager().isFight(victim.getName()))
				victim.sendMessage(utils.getMessage("player_damaged").replace("$player", damager.getName()));
			
			if(!plugin.getPvpManager().isFight(damager.getName()))
				damager.sendMessage(utils.getMessage("player_damager").replace("$player", victim.getName()));
			
			
			long time = utils.getTimeStamp();
			plugin.getPvpManager().addFight(damager.getName(), time);
			plugin.getPvpManager().addFight(victim.getName(), time);
			return;
		}
	}
}
