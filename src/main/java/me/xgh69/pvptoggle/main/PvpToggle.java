package me.xgh69.pvptoggle.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.xgh69.pvptoggle.commands.PvpAdminCommand;
import me.xgh69.pvptoggle.commands.PvpCommand;
import me.xgh69.pvptoggle.listeners.EntityListener;
import me.xgh69.pvptoggle.listeners.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PvpToggle extends JavaPlugin
{
	private File configFile;
	private EntityListener entityListener;
	private PlayerListener playerListener;
	private PvpAdminCommand pvpAdminCommandExecutor;
	private PvpCommand pvpCommandExecutor;
	private PvpManager pvpmanager;
	private PvpUtils utils;
	private static PvpToggle pvptoggle;
	
	public static final String NAME = "PvpToggle";
	public static final String VERSION = "2.1";
	
	public static boolean isInit()
	{
		PluginManager pluginManager = Bukkit.getPluginManager();
		Plugin pvpToggle = pluginManager.getPlugin("PvpToggle");
		if(pvpToggle == null || !(pvpToggle instanceof PvpToggle))
			return false;
		return true;
	}
	
	public static boolean checkDepends()
	{
		PluginManager pluginManager = Bukkit.getPluginManager();
		List<Plugin> depends = new ArrayList<Plugin>();
		
		depends.add(pluginManager.getPlugin("WorldEdit"));
		depends.add(pluginManager.getPlugin("WorldGuard"));
		
		for(Plugin p : depends)
		{
			if(p == null)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static PvpToggle getInstance()
	{
		return pvptoggle;
	}
	
	public PvpManager getPvpManager()
	{
		return pvpmanager;
	}
	
	public PvpUtils getUtils()
	{
		return utils;
	}
	
	@Override
	public void onLoad()
	{
		pvptoggle = this;
		utils = new PvpUtils();
		pvpmanager = new PvpManager();
		boolean dep = checkDepends();
		if(!dep)
		{
			PluginManager pluginManager = Bukkit.getPluginManager();
			utils.logError("Please install WorldEdit and WorldGuard to use PvpToggle plugin.");
			pluginManager.disablePlugin((Plugin) pvptoggle);
		}
		else
		{
			pvptoggle.getLogger().info("Loaded dependencies.");
		}
		return;
		
	}
	
	@Override
	public void onEnable()
	{
		configFile = new File("plugins" + File.separator + NAME + File.separator + "config.yml");
		playerListener = new PlayerListener();
		entityListener = new EntityListener();
		pvpAdminCommandExecutor = new PvpAdminCommand();
		pvpCommandExecutor = new PvpCommand();
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(playerListener, this);
		pluginManager.registerEvents(entityListener, this);
		getCommand("pvp").setExecutor(pvpCommandExecutor);
		getCommand("pvpadmin").setExecutor(pvpAdminCommandExecutor);
		pvpmanager.addAllowedCommand("/pvp status");
		pvpmanager.addAllowedCommand("/msg");
		if(!configFile.exists())
		{
			getLogger().info("Config file not found: " + configFile.getPath());
			pvptoggle.saveDefaultConfig();
			getLogger().info("Created default configuration.");
		}
		else
		{
			getLogger().info("Found file: " + configFile.getPath());
		}
		
		reloadConfig();
	}
}
