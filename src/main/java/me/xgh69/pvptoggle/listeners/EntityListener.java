package me.xgh69.pvptoggle.listeners;

import me.xgh69.pvptoggle.PvpManager;
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
		PvpManager pvpmanager = plugin.getPvpManager();
		RegionManager rm = WorldGuardPlugin.inst().getRegionManager(damager.getWorld());
		ApplicableRegionSet set = rm.getApplicableRegions(victim.getLocation());
		
		if(set.getFlag(DefaultFlag.PVP) != State.ALLOW)
		{
			if(set.getFlag(DefaultFlag.PVP) == State.DENY)
			{
				evt.setCancelled(true);
				return;
			}
			if(pvpmanager.getPvpProtection(victim.getUniqueId()) || pvpmanager.getPvpProtection(damager.getUniqueId()))
			{
				damager.sendMessage(plugin.getMessage("player_protected").replace("$player", victim.getName()));
				evt.setCancelled(true);
				return;
			}
			else
			{
				evt.setCancelled(false);
				if(!pvpmanager.isInFight(victim.getName()))
					victim.sendMessage(plugin.getMessage("player_damaged").replace("$player", damager.getName()));
				if(!pvpmanager.isInFight(damager.getName()))
					damager.sendMessage(plugin.getMessage("player_damager").replace("$player", victim.getName()));
				pvpmanager.setInFight(victim.getName(), true);
				pvpmanager.setInFight(damager.getName(), true);
				return;
			}
		}
		else if(set.getFlag(DefaultFlag.PVP) == State.ALLOW)
		{
			evt.setCancelled(false);
			if(!pvpmanager.isInFight(victim.getName()))
				victim.sendMessage(plugin.getMessage("player_damaged").replace("$player", damager.getName()));
			
			if(!pvpmanager.isInFight(damager.getName()))
				damager.sendMessage(plugin.getMessage("player_damager").replace("$player", victim.getName()));
			
			pvpmanager.setInFight(victim.getName(), true);
			pvpmanager.setInFight(damager.getName(), true);
			return;
		}
	}
}
