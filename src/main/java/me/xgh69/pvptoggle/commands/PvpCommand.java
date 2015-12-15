package me.xgh69.pvptoggle.commands;

import me.xgh69.pvptoggle.main.PvpManager;
import me.xgh69.pvptoggle.main.PvpToggle;
import me.xgh69.pvptoggle.main.PvpUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class PvpCommand implements CommandExecutor
{
	private PvpToggle plugin;
	private PvpManager pvpmanager;
	private PvpUtils utils;
	
	public PvpCommand()
	{
		plugin = PvpToggle.getInstance();
		pvpmanager = plugin.getPvpManager();
		utils = plugin.getUtils();
		
		plugin.getLogger().info("Initialized PvpCommand executor.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage(utils.getMessage("cmd_pvp_console").replace("$player", sender.getName()));
			return false;
		}
		
		Player player = (Player) sender;
		
		if(args.length == 1)
		{	
			if(args[0].equalsIgnoreCase("help"))
			{
				sender.sendMessage(utils.getMessage("cmd_pvp_usage").replace("$player", sender.getName()));
				return true;
			}
			else if(args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("on"))
			{
				pvpmanager.setPvp(player.getUniqueId(), true);
				player.sendMessage(utils.getMessage("cmd_pvp_enable").replace("$player", player.getName()));
				return true;
			}
			else if(args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("off"))
			{
				pvpmanager.setPvp(player.getUniqueId(), false);
				player.sendMessage(utils.getMessage("cmd_pvp_disable").replace("$player", player.getName()));
				return true;
			}
			else if(args[0].equalsIgnoreCase("status"))
			{
				if(pvpmanager.getPvp(player.getUniqueId()))
				{
					player.sendMessage(utils.getMessage("cmd_pvp_status").replace("$player", player.getName()) + utils.getMessage("cmd_pvp_status_enable"));
					return true;
				}
				else if(!pvpmanager.getPvp(player.getUniqueId()))
				{
					player.sendMessage(utils.getMessage("cmd_pvp_status").replace("$player", player.getName()) + utils.getMessage("cmd_pvp_status_disable"));
					return true;
				}
			}
			else
			{
				player.sendMessage(utils.getMessage("cmd_pvp_usage").replace("$player", player.getName()));
				return false;
			}
		}
		else
		{
			player.sendMessage(utils.getMessage("cmd_pvp_usage").replace("$player", player.getName()));
			return false;
		}
		
		return false;
	}
	
}
