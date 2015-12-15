package me.xgh69.pvptoggle.main;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R1.PacketPlayOutNamedEntitySpawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

public class PvpUtils
{
	private PvpToggle plugin;
	private boolean debug;
	
	public PvpUtils()
	{
		plugin = PvpToggle.getInstance();
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
				p.sendMessage(ChatColor.RED + "[Debug] " + ChatColor.DARK_RED + s);
		}
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[PvpToggle Debug] " + ChatColor.DARK_RED + s);
	}
	
	public void sendAction(Player p, String s)
	{
		IChatBaseComponent message = ChatSerializer.a("{\"text\":\"" + s + " \"}");
		PacketPlayOutChat packet = new PacketPlayOutChat(message, (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
	
	public void logError(String s)
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "[PvpToggle] " + ChatColor.RESET + s);
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
			plugin.getLogger().info("Not found settings \"" + key + "\" in plugin.getConfig().yml!");
			plugin.getLogger().info("Please delete plugin.getConfig().yml and restart server.");
			return null;
		}
		return plugin.getConfig().get("settings." + key);
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
