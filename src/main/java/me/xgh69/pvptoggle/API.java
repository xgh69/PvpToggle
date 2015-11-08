package me.xgh69.pvptoggle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class API
{
	PvpToggle plugin = PvpToggle.getInstance();
	
	public boolean containsPvpSettings(String playerName)
	{
		return plugin.containsPvpSettings(playerName);
	}
	
	public boolean getPvpSettings(String playerName)
	{
		return plugin.getPvpSettings(playerName);
	}
	
	public void setPvpSettings(String playerName, boolean b)
	{
		plugin.setPvpSettings(playerName, b);
	}
	
	public boolean isAllowedCommand(String commandName)
	{
		return plugin.isAllowedCommand(commandName);
	}
	
	public void addAllowedCommand(String commandName)
	{
		plugin.addAllowedCommand(commandName);
	}
	
	public void setInFight(String playerName, boolean fight)
	{
		plugin.setInFight(playerName, fight);
	}
	
	public boolean isInFight(String playerName)
	{
		return plugin.isInFight(playerName);
	}
	
	public int getFightTime(String playerName)
	{
		return plugin.getFightTime(playerName);
	}
	
	public boolean compareFightAndCurrentTimes(String playerName)
	{
		return plugin.compareFightAndCurrentTimes(playerName);
	}
	
	public boolean compareTimes(int a, int b)
	{
		if(a == b || a < b)
			return true;
		return false;
	}
	
	public List<Object> getPlayersInFight()
	{
		return plugin.getPlayersInFight();
	}
	
	public String getLang()
	{
		return plugin.getLang();
	}
	
	public int getTimeStamp()
	{
		int now = -1;
		try {
			Method f = PvpToggle.class.getDeclaredMethod("getTimeStamp", null);
			f.setAccessible(true);
			try {
				now = (Integer) f.invoke(plugin, null);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			f.setAccessible(false);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		return now;
	}
}
