package me.xgh69.pvptoggle.commands;

import me.xgh69.pvptoggle.PvpManager;
import me.xgh69.pvptoggle.PvpToggle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpAdminCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		PvpToggle plugin = PvpToggle.getInstance();
		PvpManager pvpmanager = plugin.getPvpManager();
		
		if(!sender.hasPermission("pvptoggle.admin"))
		{
			sender.sendMessage(plugin.getMessage("cmd_pvpadmin_noperm").replace("$player", sender.getName()));
			return true;
		}

		if(args.length == 1)
		{
			switch(args[0].toLowerCase()) {
				case "reload":
					plugin.reloadConfig();
					sender.sendMessage(plugin.getMessage("cmd_pvpadmin_reload").replace("$player", sender.getName()));
					break;
				case "info":
					if(plugin.getLang().equalsIgnoreCase("pl")) {
						sender.sendMessage(ChatColor.GREEN + "Autor: xgh69");
						sender.sendMessage(ChatColor.GREEN + "Wersja: 1.2");
						sender.sendMessage(ChatColor.GREEN + "Ten plugin dodaje prywatne ustawienia pvp dla graczy. Flagi WorldGuard'a sa wspierane.");
					}
					else
					{
						sender.sendMessage(ChatColor.GREEN + "Author: xgh69");
						sender.sendMessage(ChatColor.GREEN + "Version: 1.2");
						sender.sendMessage(ChatColor.GREEN + "This plugin add private pvp settings for players. WorldGuard flags are supported.");
					}
					break;
				default:
					sender.sendMessage(plugin.getMessage("cmd_pvpadmin_usage").replace("$player", sender.getName()));
					break;
			}
		}
		else if(args.length == 2)
		{
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
			switch(args[0].toLowerCase()) {
				case "enable":
					if(offlinePlayer.isOnline())
					{
						Player player = (Player) offlinePlayer;
						pvpmanager.setPvpProtection(player.getUniqueId(), true);
						sender.sendMessage(plugin.getMessage("cmd_pvpadmin_enable").replace("$player", player.getName()));
					}
					else
					{
						sender.sendMessage(plugin.getMessage("cmd_pvpadmin_offline").replace("$player", offlinePlayer.getName()));
					}
					break;
				case "disable":
					if(offlinePlayer.isOnline())
					{
						Player player = (Player) offlinePlayer;
						pvpmanager.setPvpProtection(player.getUniqueId(), false);
						sender.sendMessage(plugin.getMessage("cmd_pvpadmin_disable").replace("$player", player.getName()));
					}
					else
					{
						sender.sendMessage(plugin.getMessage("cmd_pvpadmin_offline").replace("$player", offlinePlayer.getName()));
					}
					break;
				case "status":
					if(offlinePlayer.isOnline())
					{
						Player player = (Player) offlinePlayer;
						if(pvpmanager.getPvpProtection(player.getUniqueId()))
						{
							sender.sendMessage(plugin.getMessage("cmd_pvpadmin_status").replace("$player", player.getName()) + plugin.getMessage("cmd_pvpadmin_status_enable"));
						}
						else if(!pvpmanager.getPvpProtection(player.getUniqueId()))
						{
							sender.sendMessage(plugin.getMessage("cmd_pvpadmin_status").replace("$player", player.getName()) + plugin.getMessage("cmd_pvpadmin_status_disable"));
						}
					}
					else
					{
						sender.sendMessage(plugin.getMessage("cmd_pvpadmin_offline").replace("$player", offlinePlayer.getName()));
					}
					break;
				default:
					sender.sendMessage(plugin.getMessage("cmd_pvpadmin_usage").replace("$player", sender.getName()));
					break;
			}
		}
		else
		{
			sender.sendMessage(plugin.getMessage("cmd_pvpadmin_usage").replace("$player", sender.getName()));
		}
		return true;
	}
}
