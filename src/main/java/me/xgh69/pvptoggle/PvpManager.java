package me.xgh69.pvptoggle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PvpManager
{
	private PvpToggle plugin;
	private PvpUtils utils;
	
	public PvpManager()
	{
		plugin = PvpToggle.getInstance();
		utils = plugin.getUtils();
	}
	
	public boolean containsPvp(UUID uid)
	{
		plugin.reloadConfig();
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uid);
		if(plugin.getConfig().contains("users." + offlinePlayer.getName()))
			return plugin.getConfig().getBoolean("users." + offlinePlayer.getName());
		return plugin.getConfig().contains("users." + uid.toString());
	}
	
	public boolean getPvp(UUID uid)
	{
		plugin.reloadConfig();
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uid);
		if(!containsPvp(uid))
			return true;
		if(plugin.getConfig().contains("users." + offlinePlayer.getName()))
			return plugin.getConfig().getBoolean("users." + offlinePlayer.getName());
		return plugin.getConfig().getBoolean("users." + uid.toString());
	}
	
	public void setPvp(UUID uid, boolean b)
	{
		plugin.getConfig().set("users." + uid.toString(), b);
		plugin.saveConfig();
	}
	
	public boolean isAllowedCommand(String commandName)
	{
		return plugin.getAllowedCommands().contains(commandName);
	}
	
	public void addAllowedCommand(String commandName)
	{
		plugin.getAllowedCommands().add(commandName);
	}
	
	public void setFight(String playerName, boolean fight, long time)
	{
		HashMap<String, Long> fight_temp = plugin.getFights();
		
		if(time < 0 && fight)
			time = utils.getTimeStamp();
		
		if(fight)
		{
			if(isFight(playerName))
			{
				fight_temp.remove(playerName);
			}
		}
		
		if(fight)
		{
			fight_temp.put(playerName, time);
		}
		else if(!fight && isFight(playerName))
		{
			fight_temp.remove(playerName);
		}
		
		plugin.setFights(fight_temp);
	}
	
	public boolean isFight(String playerName)
	{
		return plugin.getFights().containsKey(playerName);
	}
	
	public long getFightTime(String playerName)
	{
		return plugin.getFights().get(playerName);
	}
	
	public boolean isTimeOver(String playerName)
	{
		int minutes = (int) utils.getSettings("fight_minutes");
		long i = plugin.getFights().get(playerName);
		long j = utils.getTimeStamp();
		if(i + minutes*60*60 == j || i + minutes*60*60 < j)
			return true;
		return false;
	}
	
	public List<Object> getPlayersFights()
	{
		return Arrays.asList(plugin.getFights().keySet().toArray());
	}
}
