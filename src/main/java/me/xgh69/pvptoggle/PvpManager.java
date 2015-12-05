package me.xgh69.pvptoggle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PvpManager
{
	private Map<String, Integer> inFight = new HashMap<String, Integer>();
	private List<String> allowedCommands = new ArrayList<String>();
	
	private PvpToggle plugin = PvpToggle.getInstance();
	
	public boolean containsPvpSettings(UUID uid)
	{
		plugin.reloadConfig();
		return plugin.getConfig().contains("users." + uid.toString());
	}
	
	public boolean getPvpProtection(UUID uid)
	{
		plugin.reloadConfig();
		if(!containsPvpSettings(uid))
			return true;
		return plugin.getConfig().getBoolean("users." + uid.toString());
	}
	
	public void setPvpProtection(UUID uid, boolean b)
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
	
	public void setInFight(String playerName, boolean fight)
	{
		if(fight && !inFight.containsKey(playerName))
		{
			inFight.put(playerName, getTimeStamp());
		}
		else if(!fight && inFight.containsKey(playerName))
		{
			inFight.remove(playerName);
		}
	}
	
	public boolean isInFight(String playerName)
	{
		return inFight.containsKey(playerName);
	}
	
	public int getFightTime(String playerName)
	{
		return inFight.get(playerName);
	}
	
	public boolean isTimeOver(String playerName)
	{
		int i = inFight.get(playerName);
		int j = getTimeStamp();
		if(i + 2*60 == j || i + 2*60 < j)
			return true;
		return false;
	}
	
	public List<Object> getPlayersInFight()
	{
		return Arrays.asList(inFight.keySet().toArray());
	}
	
	private int getTimeStamp()
	{
		int now = Integer.parseInt((new SimpleDateFormat("HH")).format(new Date())) * 60 * 60;
		now += Integer.parseInt((new SimpleDateFormat("mm")).format(new Date())) * 60;
		now += Integer.parseInt((new SimpleDateFormat("ss")).format(new Date()));
		
		return now;
	}
}
