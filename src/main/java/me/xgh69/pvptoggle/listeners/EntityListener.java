package me.xgh69.pvptoggle.listeners;

import me.xgh69.pvptoggle.PvpToggle;

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
	@EventHandler
	@SuppressWarnings("deprecation")
	public void onDamageByEntity(EntityDamageByEntityEvent evt)
	{
		if(!(evt.getDamager() instanceof Player) || !(evt.getEntity() instanceof Player))
			return;
		
		final Player victim = (Player) evt.getEntity();
		final Player damager = (Player) evt.getDamager();
		PvpToggle plugin = PvpToggle.getInstance();
		RegionManager rm = WorldGuardPlugin.inst().getRegionManager(damager.getWorld());
		ApplicableRegionSet set = rm.getApplicableRegions(victim.getLocation());
		
		if(set.getFlag(DefaultFlag.PVP) != State.ALLOW)
		{
			if(set.getFlag(DefaultFlag.PVP) == State.DENY)
			{
				evt.setCancelled(true);
				return;
			}
			
			if(plugin.getPvp(victim.getUniqueId()) || plugin.getPvp(damager.getUniqueId()))
			{
				damager.sendMessage(plugin.getMessage("player_protected").replace("$player", victim.getName()));
				evt.setCancelled(true);
				return;
			}
			else
			{
				evt.setCancelled(false);
				if(!plugin.isFight(victim.getName()))
					victim.sendMessage(plugin.getMessage("player_damaged").replace("$player", damager.getName()));
				
				if(!plugin.isFight(damager.getName()))
					damager.sendMessage(plugin.getMessage("player_damager").replace("$player", victim.getName()));
				
				if(!plugin.isFight(victim.getName()) && !plugin.isFight(damager.getName()))
				{
					long time = plugin.getTimeStamp();
					plugin.setFight(victim.getName(), true, time);
					plugin.setFight(damager.getName(), true, time);
				}
				return;
			}
		}
		else if(set.getFlag(DefaultFlag.PVP) == State.ALLOW)
		{
			evt.setCancelled(false);
			if(!plugin.isFight(victim.getName()))
				victim.sendMessage(plugin.getMessage("player_damaged").replace("$player", damager.getName()));
			
			if(!plugin.isFight(damager.getName()))
				damager.sendMessage(plugin.getMessage("player_damager").replace("$player", victim.getName()));
			long time = plugin.getTimeStamp();
			plugin.setFight(damager.getName(), true, time);
			plugin.setFight(victim.getName(), true, time);
			return;
		}
	}
}
