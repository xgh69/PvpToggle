package me.xgh69.pvptoggle.commands;

import me.xgh69.pvptoggle.PvpToggle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpToggleCommand implements CommandExecutor
{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		PvpToggle plugin = PvpToggle.getInstance();
		
		if(!sender.hasPermission("pvptoggle.admin"))
		{
			sender.sendMessage(plugin.getMessage("cmd_pvptoggle_noperm").replace("$player", sender.getName()));
			return false;
		}
		if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("reload"))
			{
				plugin.reloadConfig();
				sender.sendMessage(plugin.getMessage("cmd_pvptoggle_reload").replace("$player", sender.getName()));
				return true;
			}
			else if(args[0].equalsIgnoreCase("info"))
			{
				if(plugin.getLang().equalsIgnoreCase("pl"))
				{
					sender.sendMessage(ChatColor.GREEN + "Autor: xgh69");
					sender.sendMessage(ChatColor.GREEN + "Wersja: 1.1");
					sender.sendMessage(ChatColor.GREEN + "Ten plugin dodaje prywatne ustawienia pvp dla graczy. Flagi WorldGuard'a sa wspierane.");
					return true;
				}
				else
				{
					sender.sendMessage(ChatColor.GREEN + "Author: xgh69");
					sender.sendMessage(ChatColor.GREEN + "Version: 1.1");
					sender.sendMessage(ChatColor.GREEN + "This plugin add private pvp settings for players. WorldGuard flags are supported.");
					return true;
				}
			}
			else
			{
				sender.sendMessage(plugin.getMessage("cmd_pvptoggle_usage").replace("$player", sender.getName()));
				return true;
			}
		}
		else if(args.length == 2)
		{
			if(args[0].equalsIgnoreCase("enable"))
			{
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
				if(offlinePlayer.isOnline())
				{
					Player player = (Player) offlinePlayer;
					plugin.setPvpProtection(player.getUniqueId(), true);
					sender.sendMessage(plugin.getMessage("cmd_pvptoggle_enable").replace("$player", player.getName()));
					return true;
				}
				else
				{
					sender.sendMessage(plugin.getMessage("cmd_pvptoggle_offline").replace("$player", offlinePlayer.getName()));
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("disable"))
			{
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
				if(offlinePlayer.isOnline())
				{
					Player player = (Player) offlinePlayer;
					plugin.setPvpProtection(player.getUniqueId(), false);
					sender.sendMessage(plugin.getMessage("cmd_pvptoggle_disable").replace("$player", player.getName()));
					return true;
				}
				else
				{
					sender.sendMessage(plugin.getMessage("cmd_pvptoggle_offline").replace("$player", offlinePlayer.getName()));
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("status"))
			{
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
				if(offlinePlayer.isOnline())
				{
					Player player = (Player) offlinePlayer;
					if(plugin.getPvpProtection(player.getUniqueId()))
					{
						sender.sendMessage(plugin.getMessage("cmd_pvptoggle_status").replace("$player", player.getName()) + plugin.getMessage("cmd_pvptoggle_status_enable"));
						return true;
					}
					else if(!plugin.getPvpProtection(player.getUniqueId()))
					{
						sender.sendMessage(plugin.getMessage("cmd_pvptoggle_status").replace("$player", player.getName()) + plugin.getMessage("cmd_pvptoggle_status_disable"));
						return true;					
					}
				}
				else
				{
					sender.sendMessage(plugin.getMessage("cmd_pvptoggle_offline").replace("$player", offlinePlayer.getName()));
					return true;
				}
			}
			else
			{
				sender.sendMessage(plugin.getMessage("cmd_pvptoggle_usage").replace("$player", sender.getName()));
				return true;
			}
		}
		else
		{
			sender.sendMessage(plugin.getMessage("cmd_pvptoggle_usage").replace("$player", sender.getName()));
			return true;
		}
		return false;
	}

}
