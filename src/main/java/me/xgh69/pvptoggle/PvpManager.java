package me.xgh69.pvptoggle;

import java.util.ArrayList;
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
	private HashMap<String, Long> inFight;
	private List<String> allowedCommands;
	
	public PvpManager()
	{
		plugin = PvpToggle.getInstance();
		utils = plugin.getUtils();
		allowedCommands = new ArrayList<String>();
		inFight = new HashMap<String, Long>();
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
		return allowedCommands.contains(commandName);
	}
	
	public void addAllowedCommand(String commandName)
	{
		allowedCommands.add(commandName);
	}
	
	public void setFight(String playerName, boolean fight, long time)
	{
		if(time < 0 && fight)
			time = utils.getTimeStamp();
		
		if(fight)
		{
			if(isFight(playerName))
			{
				inFight.remove(playerName);
			}
		}
		
		if(fight)
		{
			inFight.put(playerName, time);
		}
		else if(!fight && isFight(playerName))
		{
			inFight.remove(playerName);
		}
	}
	
	public boolean isFight(String playerName)
	{
		return inFight.containsKey(playerName);
	}
	
	public long getFightTime(String playerName)
	{
		return inFight.get(playerName);
	}
	
	public boolean isTimeOver(String playerName)
	{
		long i = inFight.get(playerName);
		long j = utils.getTimeStamp();
		if(i + 1*60*60 == j || i + 1*60*60 < j)
			return true;
		return false;
	}
	
	public List<Object> getPlayersFights()
	{
		return Arrays.asList(inFight.keySet().toArray());
	}
}
