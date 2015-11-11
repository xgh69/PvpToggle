package me.xgh69.pvptoggle;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.xgh69.pvptoggle.commands.PvpCommand;
import me.xgh69.pvptoggle.commands.PvpToggleCommand;
import me.xgh69.pvptoggle.listeners.EntityListener;
import me.xgh69.pvptoggle.listeners.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class PvpToggle extends JavaPlugin
{
	private File configFile;
	private YamlConfiguration config;
	private Map<String, Integer> inFight;
	private List<String> allowedCommands;
	private String lang;
	private EntityListener entityListener;
	private PlayerListener playerListener;
	private static PvpToggle pvptoggle;
	
	public static PvpToggle getInstance()
	{
		return pvptoggle;
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
		allowedCommands = new ArrayList<String>();
		inFight = new HashMap<String, Integer>();
		configFile = new File("plugins" + File.separator + this.getName() + File.separator + "config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);
		
		Bukkit.getPluginManager().registerEvents(playerListener, this);
		Bukkit.getPluginManager().registerEvents(entityListener, this);
		getCommand("pvp").setExecutor(new PvpCommand());
		getCommand("pvptoggle").setExecutor(new PvpToggleCommand());
		addAllowedCommand("/pvp status");
		addAllowedCommand("/msg");
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
				config.addDefault("messages.cmd_pvptoggle_reload", "&cPrzeladowales konfiguracje.");
				config.addDefault("messages.cmd_pvptoggle_noperm", "&cBrak uprawnien.");
				config.addDefault("messages.cmd_pvptoggle_usage", "&a/pvptoggle enable <name> | disable <name> | status <name> | info");
				config.addDefault("messages.cmd_pvptoggle_enable", "&aWlaczono ochrone graczowi $player");
				config.addDefault("messages.cmd_pvptoggle_disable", "&cWylaczono ochrone graczowi $player");
				config.addDefault("messages.cmd_pvptoggle_status", "&bOchrona gracza $player jest ");
				config.addDefault("messages.cmd_pvptoggle_status_enable", "&awlaczona");
				config.addDefault("messages.cmd_pvptoggle_status_disable", "&cwylaczona");
				config.addDefault("messages.cmd_pvptoggle_offline", "&c$player jest offline.");
				config.addDefault("users", "");
				config.addDefault("users.JanKowalski", true);
				config.options().copyDefaults(true);
				
				saveConfig();
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
				config.addDefault("messages.cmd_pvptoggle_reload", "&cConfig reloaded.");
				config.addDefault("messages.cmd_pvptoggle_noperm", "&cNo permission.");
				config.addDefault("messages.cmd_pvptoggle_usage", "&a/pvptoggle enable <name> | disable <name> | status <name> | info");
				config.addDefault("messages.cmd_pvptoggle_enable", "&aEnabled $player's pvp proection.");
				config.addDefault("messages.cmd_pvptoggle_disable", "&cDisabled $player's pvp protection.");
				config.addDefault("messages.cmd_pvptoggle_status", "&b$player's protection is ");
				config.addDefault("messages.cmd_pvptoggle_status_enable", "&aenabled");
				config.addDefault("messages.cmd_pvptoggle_status_disable", "&cdisabled");
				config.addDefault("messages.cmd_pvptoggle_offline", "&c$player is offline.");
				config.addDefault("users", "");
				config.addDefault("users.JohnDoo", true);
				config.options().copyDefaults(true);
				
				saveConfig();
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
				config.addDefault("messages.cmd_pvptoggle_reload", "&cConfig reloaded.");
				config.addDefault("messages.cmd_pvptoggle_noperm", "&cNo permission.");
				config.addDefault("messages.cmd_pvptoggle_usage", "&a/pvptoggle enable <name> | disable <name> | status <name> | info");
				config.addDefault("messages.cmd_pvptoggle_enable", "&aEnabled $player's pvp proection.");
				config.addDefault("messages.cmd_pvptoggle_disable", "&cDisabled $player's pvp protection.");
				config.addDefault("messages.cmd_pvptoggle_status", "&b$player's protection is ");
				config.addDefault("messages.cmd_pvptoggle_status_enable", "&aenabled");
				config.addDefault("messages.cmd_pvptoggle_status_disable", "&cdisabled");
				config.addDefault("messages.cmd_pvptoggle_offline", "&c$player is offline.");
				config.addDefault("users", "");
				config.addDefault("users.JohnDoo", true);
				config.options().copyDefaults(true);
				
				saveConfig();
			}
		}
		else
		{
			getLogger().info("Found file: " + configFile.getPath());
		}
		
		reloadConfig();
	}
	
	
	public boolean containsPvpSettings(UUID uid)
	{
		reloadConfig();
		return config.contains("users." + uid.toString());
	}
	
	public boolean getPvpSettings(UUID uid)
	{
		reloadConfig();
		if(!containsPvpSettings(uid))
			return true;
		return config.getBoolean("users." + uid.toString());
	}
	
	public void setPvpSettings(UUID uid, boolean b)
	{
		config.set("users." + uid.toString(), b);
		saveConfig();
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
	
	public boolean compareFightAndCurrentTimes(String playerName)
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
	
	public String getMessage(String msg)
	{
		return config.getString("messages." + msg).replace("&", "§");
	}
	
	private int getTimeStamp()
	{
		int now = Integer.parseInt((new SimpleDateFormat("HH")).format(new Date())) * 60 * 60;
		now += Integer.parseInt((new SimpleDateFormat("mm")).format(new Date())) * 60;
		now += Integer.parseInt((new SimpleDateFormat("ss")).format(new Date()));
		
		return now;
	}

}
