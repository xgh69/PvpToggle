package me.xgh69.pvptoggle.main;

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
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(p.hasPermission("pvptoggle.admin"))
				p.sendMessage(ChatColor.RED + "[PvpToggle Debug] " + ChatColor.DARK_RED + s);
		}
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[PvpToggle Debug] " + ChatColor.DARK_RED + s);
	}
	
	/* public void sendBar(PlayerConnection p, String s)
	{
		IChatBaseComponent message = ChatSerializer.a("{\"text\":\"" + s + " \"}");
		PacketPlayOutChat bar_packet = new PacketPlayOutChat(message, (byte) 2);
		p.sendPacket(bar_packet);
	} */
	
	public void logError(String s)
	{
		Bukkit.getConsoleSender().sendMessage("[PvpToggle] " + ChatColor.DARK_RED + s);
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
		
		return plugin.getConfig().getString("messages." + msg).replace("&", "§");
	}
	
	public Object getSettings(String key)
	{
		key = key.replace(".", "_");
		if(!plugin.getConfig().contains("settings." + key))
		{
			plugin.getLogger().info("Not found settings \"" + key + "\" in config.yml!");
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
