package me.xgh69.pvptoggle.commands;

import me.xgh69.pvptoggle.PvpManager;
import me.xgh69.pvptoggle.PvpToggle;
import me.xgh69.pvptoggle.PvpUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpAdminCommand implements CommandExecutor
{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		PvpToggle plugin = PvpToggle.getInstance();
		PvpManager pvpmanager = plugin.getPvpManager();
		PvpUtils utils = plugin.getUtils();
		
		if(!sender.hasPermission("pvptoggle.admin"))
		{
			sender.sendMessage(utils.getMessage("cmd_pvpadmin_noperm").replace("$player", sender.getName()));
			return false;
		}
		if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("help"))
			{
				sender.sendMessage(utils.getMessage("cmd_pvpadmin_usage").replace("$player", sender.getName()));
				return true;
			}
			else if(args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver"))
			{
				sender.sendMessage(ChatColor.GREEN + "Version: " + PvpToggle.VERSION);
				sender.sendMessage(ChatColor.GREEN + "Author: xgh69");
				return true;
			}
			else if(args[0].equalsIgnoreCase("reload"))
			{
				plugin.reloadConfig();
				sender.sendMessage(utils.getMessage("cmd_pvpadmin_reload").replace("$player", sender.getName()));
				return true;
			}
			else
			{
				sender.sendMessage(utils.getMessage("cmd_pvpadmin_usage").replace("$player", sender.getName()));
				return true;
			}
		}
		else if(args.length == 2)
		{
			if(args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("on"))
			{
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
				if(offlinePlayer.isOnline())
				{
					Player player = (Player) offlinePlayer;
					pvpmanager.setPvp(player.getUniqueId(), true);
					sender.sendMessage(utils.getMessage("cmd_pvpadmin_enable").replace("$player", player.getName()));
					return true;
				}
				else
				{
					sender.sendMessage(utils.getMessage("cmd_pvpadmin_offline").replace("$player", offlinePlayer.getName()));
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("off"))
			{
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
				if(offlinePlayer.isOnline())
				{
					Player player = (Player) offlinePlayer;
					pvpmanager.setPvp(player.getUniqueId(), false);
					sender.sendMessage(utils.getMessage("cmd_pvpadmin_disable").replace("$player", player.getName()));
					return true;
				}
				else
				{
					sender.sendMessage(utils.getMessage("cmd_pvpadmin_offline").replace("$player", offlinePlayer.getName()));
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("status"))
			{
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
				if(offlinePlayer.isOnline())
				{
					Player player = (Player) offlinePlayer;
					if(pvpmanager.getPvp(player.getUniqueId()))
					{
						sender.sendMessage(utils.getMessage("cmd_pvpadmin_status").replace("$player", player.getName()) + utils.getMessage("cmd_pvpadmin_status_enable"));
						return true;
					}
					else if(!pvpmanager.getPvp(player.getUniqueId()))
					{
						sender.sendMessage(utils.getMessage("cmd_pvpadmin_status").replace("$player", player.getName()) + utils.getMessage("cmd_pvpadmin_status_disable"));
						return true;					
					}
				}
				else
				{
					sender.sendMessage(utils.getMessage("cmd_pvpadmin_offline").replace("$player", offlinePlayer.getName()));
					return true;
				}
			}
			else
			{
				sender.sendMessage(utils.getMessage("cmd_pvpadmin_usage").replace("$player", sender.getName()));
				return true;
			}
		}
		else
		{
			sender.sendMessage(utils.getMessage("cmd_pvpadmin_usage").replace("$player", sender.getName()));
			return true;
		}
		return false;
	}

}
