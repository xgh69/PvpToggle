package me.xgh69.pvptoggle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

public class PvpManager
{
	private YamlConfiguration config;
	private PvpToggle plugin;
	private Map<String, Long> inFight;
	private List<String> allowedCommands;
	
	public PvpManager()
	{
		plugin = PvpToggle.getInstance();
		config = plugin.getConfig();
		allowedCommands = new ArrayList<String>();
		inFight = new HashMap<String, Long>();
	}
	
	public boolean containsPvp(UUID uid)
	{
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uid);
		plugin.reloadConfig();
		if(config.contains("users." + offlinePlayer.getName()))
			return config.getBoolean("users." + offlinePlayer.getName());
		return config.contains("users." + uid.toString());
	}
	
	public boolean getPvp(UUID uid)
	{
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uid);
		plugin.reloadConfig();
		if(!containsPvp(uid))
			return true;
		if(config.contains("users." + offlinePlayer.getName()))
			return config.getBoolean("users." + offlinePlayer.getName());
		return config.getBoolean("users." + uid.toString());
	}
	
	public void setPvp(UUID uid, boolean b)
	{
		config.set("users." + uid.toString(), b);
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
			time = plugin.getTimeStamp();
		
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
		long j = plugin.getTimeStamp();
		if(i + 1*60*60 == j || i + 1*60*60 < j)
			return true;
		return false;
	}
	
	public List<Object> getPlayersFights()
	{
		return Arrays.asList(inFight.keySet().toArray());
	}
}
