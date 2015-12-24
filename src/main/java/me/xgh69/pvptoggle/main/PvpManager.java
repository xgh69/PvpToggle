package me.xgh69.pvptoggle.main;

import java.util.ArrayList;
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
		inFight = new HashMap<String, Long>();
		allowedCommands = new ArrayList<String>();
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
		if(!containsPvp(uid))
			return true;
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
	
	public void addFight(String playerName, long time)
	{
		if(inFight.containsKey(playerName))
			inFight.remove(playerName);
		
		inFight.put(playerName, time);
	}
	
	public void removeFight(String playerName)
	{
		if(inFight.containsKey(playerName))
			inFight.remove(playerName);
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
		int minutes = (int) utils.getSettings("fight_minutes");
		long i = inFight.get(playerName);
		long j = utils.getTimeStamp();
		if(i + minutes*60*1000 == j || j > i + minutes*60*1000)
			return true;
		return false;
	}
}
