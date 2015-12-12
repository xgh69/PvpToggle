package me.xgh69.pvptoggle;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class PvpUtils
{
	private YamlConfiguration config;
	private PvpToggle plugin;
	
	public PvpUtils()
	{
		plugin = PvpToggle.getInstance();
		config = plugin.getConfig();
	}
	
	public String getMessage(String msg)
	{
		msg = msg.replace(".", "_");
		if(!config.contains("messages." + msg))
		{
			plugin.getLogger().info("Not found message \"" + msg + "\" in config.yml!");
			plugin.getLogger().info("Please delete config.yml and restart server.");
			return ChatColor.RED + "Error in config.yml! Check console log!\n";
		}
		
		return config.getString("messages." + msg).replace("&", "§");
	}
	
	public Object getSettings(String key)
	{
		key = key.replace(".", "_");
		if(!config.contains("settings." + key))
		{
			plugin.getLogger().info("Not found settings \"" + key + "\" in config.yml!");
			plugin.getLogger().info("Please delete config.yml and restart server.");
			return true;
		}
		return config.get("settings." + key);
	}
	
	public long getTimeStamp()
	{
		Date date = new Date();
		long now = Integer.parseInt((new SimpleDateFormat("HH")).format(date)) * 60 * 60 * 60;
		now += Integer.parseInt((new SimpleDateFormat("mm")).format(date)) * 60 * 60;
		now += Integer.parseInt((new SimpleDateFormat("ss")).format(date)) * 60; 
		now += Integer.parseInt((new SimpleDateFormat("SS")).format(date));
		
		return now;
	}
}
