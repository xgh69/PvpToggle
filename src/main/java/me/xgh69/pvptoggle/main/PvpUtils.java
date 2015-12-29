package me.xgh69.pvptoggle.main;

import me.xgh69.pvptoggle.events.PvpToggleDebugEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PvpUtils
{
	private PvpToggle plugin;
	private boolean debug;
	
	public PvpUtils()
	{
		plugin = PvpToggle.getInstance();
		debug = false;
	}
	
	public boolean isDebug()
	{
		return debug;
	}
	
	public void setDebug(boolean b)
	{
		debug = b;
	}
	
	public void sendDebug(String s)
	{
		if(!isDebug())
			return;
			
		PvpToggleDebugEvent event = new PvpToggleDebugEvent(ChatColor.RED + "[PvpToggle Debug] " + ChatColor.DARK_RED + s);
		Bukkit.getPluginManager().callEvent(event);
		
		if(event.isCancelled())
			return;
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(p.hasPermission(event.getPermission()))
				p.sendMessage(event.getMessage());
		}
		
		Bukkit.getConsoleSender().sendMessage(event.getMessage());
	}
	
	public void logError(String s)
	{
		Bukkit.getConsoleSender().sendMessage("[PvpToggle] " + ChatColor.RED + s);
	}
	
	public String getMessage(String msg)
	{
		msg = msg.replace(".", "_");
		if(!plugin.getConfig().contains("messages." + msg))
		{
			plugin.getLogger().info("Not found message \"" + msg + "\" in config.yml!");
			plugin.getLogger().info("Please delete conifg.yml and restart server.");
			return ChatColor.RED + "Error in config.yml! Check console log!\n";
		}
		
		return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages." + msg));
	}
	
	public Object getOption(String key)
	{
		key = key.replace(".", "_");
		if(!plugin.getConfig().contains("settings." + key))
		{
			plugin.getLogger().info("Not found option \"" + key + "\" in config.yml!");
			plugin.getLogger().info("Please delete config.yml and restart server.");
			return null;
		}
		return plugin.getConfig().get("settings." + key);
	}
	
	public long getTimeStamp()
	{
		return System.currentTimeMillis();
	}
}
