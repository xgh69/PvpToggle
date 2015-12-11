package me.xgh69.pvptoggle;

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
	
	public boolean getSettings(String key)
	{
		key = key.replace(".", "_");
		if(!config.contains("settings." + key))
		{
			plugin.getLogger().info("Not found settings \"" + key + "\" in config.yml!");
			plugin.getLogger().info("Please delete config.yml and restart server.");
			return true;
		}
		return config.getBoolean("settings." + key);
	}
}
