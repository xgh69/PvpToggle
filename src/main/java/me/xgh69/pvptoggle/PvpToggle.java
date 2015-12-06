package me.xgh69.pvptoggle;

import java.io.File;
import java.io.IOException;

import me.xgh69.pvptoggle.commands.PvpAdminCommand;
import me.xgh69.pvptoggle.commands.PvpCommand;
import me.xgh69.pvptoggle.listeners.EntityListener;
import me.xgh69.pvptoggle.listeners.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class PvpToggle extends JavaPlugin
{
	private File configFile;
	private YamlConfiguration config;
	private String lang;
	private EntityListener entityListener;
	private PlayerListener playerListener;
	private static PvpToggle pvptoggle;
	private static PvpManager pvpmanager;
	
	public static final String NAME = "PvpToggle";
	public static final String VERSION = "1.2";
	
	
	public static PvpToggle getInstance()
	{
		return pvptoggle;
	}
	
	public PvpManager getPvpManager()
	{
		return pvpmanager;
	}
	
	public YamlConfiguration getConfig()
	{
		return config;
	}
	
	public void saveConfig()
	{
		try {
			config.save(configFile);
		} catch (IOException e) {
			getLogger().info("Err: " + e);
		}
		
		reloadConfig();
	}
	
	public void reloadConfig()
	{
		config = YamlConfiguration.loadConfiguration(configFile);
	}
	
	public String getLang()
	{
		return lang;
	}
	
	@Override
	public void onEnable()
	{
		pvptoggle = this;
		playerListener = new PlayerListener();
		entityListener = new EntityListener();
		lang = System.getProperty("user.language");
		configFile = new File("plugins" + File.separator + NAME + File.separator + "config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);
		pvpmanager = new PvpManager();
		
		Bukkit.getPluginManager().registerEvents(playerListener, this);
		Bukkit.getPluginManager().registerEvents(entityListener, this);
		getCommand("pvp").setExecutor(new PvpCommand());
		getCommand("pvpadmin").setExecutor(new PvpAdminCommand());
		pvpmanager.addAllowedCommand("/pvp status");
		pvpmanager.addAllowedCommand("/msg");
		if(!configFile.exists())
		{
			getLogger().info("Config file not found: " + configFile.getPath());
			if(lang.equalsIgnoreCase("pl"))
			{
				getLogger().info("Your language detected is: " + lang);
				getLogger().info("Your language is supported.");
				config.addDefault("messages", "");
				config.addDefault("messages.first_join", "&aOchrona przed walka zostala wlaczona.");
				config.addDefault("messages.logout_join", "&cWylogowales sie podczas walki.");
				config.addDefault("messages.logout_left", "&c$player wylogowal sie podczas walki.");
				config.addDefault("messages.logout_left_stopfight", "&aTy juz mozesz sie spokojnie wylogowac, poniewaz twoj kolega lognal.");
				config.addDefault("messages.cuboid_pvponly_enter", "&cWchodzisz na teren z permanentnym PVP.");
				config.addDefault("messages.cuboid_pvponly_exit", "&cWyszedles z terenu z permanentnym PVP.");
				config.addDefault("messages.cuboid_pvponly_cmd_block", "&cNie mozesz uzyc tutaj tej komendy.");
				config.addDefault("messages.stopfight", "&aMozesz sie juz wylogowac.");
				config.addDefault("messages.cmd_infight", "&cMusisz odczekac 2 minuty od konca walki.");
				config.addDefault("messages.player_protected", "&c$player jest chroniony.");
				config.addDefault("messages.player_damager", "&cNie wylogowywuj sie. Uderzyles $player.");
				config.addDefault("messages.player_damaged", "&cNie wylogowywuj sie. $player Cie uderzyl.");
				config.addDefault("messages.cmd_pvp_usage", "&aPoprawne uzycie: /pvp disable | enable | status.");
				config.addDefault("messages.cmd_pvp_enable", "&aWlaczyles ochrone przed walka.");
				config.addDefault("messages.cmd_pvp_disable", "&cWylaczyles ochrone przed walka.");
				config.addDefault("messages.cmd_pvp_status", "&bTwoja ochrona przed walka jest ");
				config.addDefault("messages.cmd_pvp_status_enable", "&cwlaczona&b.");
				config.addDefault("messages.cmd_pvp_status_disable", "&awylaczona&b.");
				config.addDefault("messages.cmd_pvp_console", "&c$player nie potrafi walczyc.");
				config.addDefault("messages.cmd_pvpadmin_reload", "&cPrzeladowales konfiguracje.");
				config.addDefault("messages.cmd_pvpadmin_noperm", "&cBrak uprawnien.");
				config.addDefault("messages.cmd_pvpadmin_usage", "&a/pvptoggle enable <name> | disable <name> | status <name> | info");
				config.addDefault("messages.cmd_pvpadmin_enable", "&aWlaczono ochrone graczowi $player");
				config.addDefault("messages.cmd_pvpadmin_disable", "&cWylaczono ochrone graczowi $player");
				config.addDefault("messages.cmd_pvpadmin_status", "&bOchrona gracza $player jest ");
				config.addDefault("messages.cmd_pvpadmin_status_enable", "&awlaczona");
				config.addDefault("messages.cmd_pvpadmin_status_disable", "&cwylaczona");
				config.addDefault("messages.cmd_pvpadmin_offline", "&c$player jest offline.");
				config.addDefault("users", "");
				config.addDefault("users.JanKowalski", true);
				config.options().copyDefaults(true);
			}
			else if(lang.equalsIgnoreCase("en"))
			{
				getLogger().info("Your language detected is: " + lang);
				getLogger().info("Your language is supported.");
				config.addDefault("messages", "");
				config.addDefault("messages.first_join", "&aFight protected is enabled.");
				config.addDefault("messages.logout_join", "&cYou are logout.");
				config.addDefault("messages.logout_left", "&c$player logout in fight.");
				config.addDefault("messages.logout_left_stopfight", "&aYou can logout, because your friend logout");
				config.addDefault("messages.cuboid_pvponly_enter", "&cYou are entering on perm pvp region.");
				config.addDefault("messages.cuboid_pvponly_exit", "&cYou are exiting from perm pvp region.");
				config.addDefault("messages.cuboid_pvponly_cmd_block", "&cYou cannot use this command here.");
				config.addDefault("messages.stopfight", "&aYou can logout now.");
				config.addDefault("messages.cmd_infight", "&cPlease wait 2 minutes after fight.");
				config.addDefault("messages.player_protected", "&c$player is protected.");
				config.addDefault("messages.player_damager", "&cDon't logout. You hurted $player.");
				config.addDefault("messages.player_damaged", "&cDon't logout. $player hurt you.");
				config.addDefault("messages.cmd_pvp_usage", "&aUsage: /pvp disable | enable | status.");
				config.addDefault("messages.cmd_pvp_enable", "&cYour pvp protection is enabled.");
				config.addDefault("messages.cmd_pvp_disable", "&cYour pvp protection is disabled.");
				config.addDefault("messages.cmd_pvp_status", "&bYour pvp protection is ");
				config.addDefault("messages.cmd_pvp_status_enable", "&cenabled&b.");
				config.addDefault("messages.cmd_pvp_status_disable", "&adisabled&b.");
				config.addDefault("messages.cmd_pvp_console", "&cConsole cannot fight.");
				config.addDefault("messages.cmd_pvpadmin_reload", "&cConfig reloaded.");
				config.addDefault("messages.cmd_pvpadmin_noperm", "&cNo permission.");
				config.addDefault("messages.cmd_pvpadmin_usage", "&a/pvptoggle enable <name> | disable <name> | status <name> | info");
				config.addDefault("messages.cmd_pvpadmin_enable", "&aEnabled $player's pvp proection.");
				config.addDefault("messages.cmd_pvpadmin_disable", "&cDisabled $player's pvp protection.");
				config.addDefault("messages.cmd_pvpadmin_status", "&b$player's protection is ");
				config.addDefault("messages.cmd_pvpadmin_status_enable", "&aenabled");
				config.addDefault("messages.cmd_pvpadmin_status_disable", "&cdisabled");
				config.addDefault("messages.cmd_pvpadmin_offline", "&c$player is offline.");
				config.addDefault("users", "");
				config.addDefault("users.JohnDoo", true);
				config.options().copyDefaults(true);
			}
			else
			{
				getLogger().info("Your language detected is: " + lang);
				getLogger().info("Your language is not supported and using English.");
				config.addDefault("messages", "");
				config.addDefault("messages.first_join", "&aFight protected is enabled.");
				config.addDefault("messages.logout_join", "&cYou are logout.");
				config.addDefault("messages.logout_left", "&c$player logout in fight.");
				config.addDefault("messages.logout_left_stopfight", "&aYou can logout, because your friend logout");
				config.addDefault("messages.cuboid_pvponly_enter", "&cYou are entering on perm pvp region.");
				config.addDefault("messages.cuboid_pvponly_exit", "&cYou are exiting from perm pvp region.");
				config.addDefault("messages.cuboid_pvponly_cmd_block", "&cYou cannot use this command here.");
				config.addDefault("messages.stopfight", "&aYou can logout now.");
				config.addDefault("messages.cmd_infight", "&cPlease wait 2 minutes after fight.");
				config.addDefault("messages.player_protected", "&c$player is protected.");
				config.addDefault("messages.player_damager", "&cDon't logout. You hurted $player.");
				config.addDefault("messages.player_damaged", "&cDon't logout. $player hurt you.");
				config.addDefault("messages.cmd_pvp_usage", "&aUsage: /pvp disable | enable | status.");
				config.addDefault("messages.cmd_pvp_enable", "&cYour pvp protection is enabled.");
				config.addDefault("messages.cmd_pvp_disable", "&cYour pvp protection is disabled.");
				config.addDefault("messages.cmd_pvp_status", "&bYour pvp protection is ");
				config.addDefault("messages.cmd_pvp_status_enable", "&cenabled&b.");
				config.addDefault("messages.cmd_pvp_status_disable", "&adisabled&b.");
				config.addDefault("messages.cmd_pvp_console", "&cConsole cannot fight.");
				config.addDefault("messages.cmd_pvpadmin_reload", "&cConfig reloaded.");
				config.addDefault("messages.cmd_pvpadmin_noperm", "&cNo permission.");
				config.addDefault("messages.cmd_pvpadmin_usage", "&a/pvptoggle enable <name> | disable <name> | status <name> | info");
				config.addDefault("messages.cmd_pvpadmin_enable", "&aEnabled $player's pvp proection.");
				config.addDefault("messages.cmd_pvpadmin_disable", "&cDisabled $player's pvp protection.");
				config.addDefault("messages.cmd_pvpadmin_status", "&b$player's protection is ");
				config.addDefault("messages.cmd_pvpadmin_status_enable", "&aenabled");
				config.addDefault("messages.cmd_pvpadmin_status_disable", "&cdisabled");
				config.addDefault("messages.cmd_pvpadmin_offline", "&c$player is offline.");
				config.addDefault("users", "");
				config.addDefault("users.JohnDoo", true);
				config.options().copyDefaults(true);
			}
			config.addDefault("settings.first_join_pvp_protection", true);
			saveConfig();
		}
		else
		{
			getLogger().info("Found file: " + configFile.getPath());
		}
		
		reloadConfig();
	}
	
	
	public String getMessage(String msg)
	{
		return config.getString("messages." + msg).replace("&", "§");
	}
	
	public boolean getSettings(String key)
	{
		return config.getBoolean("settings." + key);
	}

}
