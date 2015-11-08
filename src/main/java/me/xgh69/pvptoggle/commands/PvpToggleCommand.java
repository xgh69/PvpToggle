package me.xgh69.pvptoggle.commands;

import me.xgh69.pvptoggle.PvpToggle;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PvpToggleCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		PvpToggle plugin = PvpToggle.getInstance();
		
		if(!sender.hasPermission("pvptoggle.admin"))
		{
			sender.sendMessage(plugin.getMessage("cmd_pvptoggle_noperm").replace("$player", sender.getName()));
		}
		
		if(args[0].equalsIgnoreCase("reload"))
		{
			plugin.reloadConfig();
			sender.sendMessage(plugin.getMessage("cmd_pvptoggle_reload").replace("$player", sender.getName()));
			return true;
		}
		else
		{
			if(plugin.getLang().equalsIgnoreCase("pl"))
			{
				sender.sendMessage(ChatColor.GREEN + "Autor: xgh69");
				sender.sendMessage(ChatColor.GREEN + "Wersja: 1.0-beta");
				sender.sendMessage("Ten plugin dodaje prywatne ustawienia pvp dla graczy. Flagi WorldGuard'a sa wspierane.");
			}
			else
			{
				sender.sendMessage(ChatColor.GREEN + "Author: xgh69");
				sender.sendMessage(ChatColor.GREEN + "Version: 1.0-beta");
				sender.sendMessage("This plugin add private pvp settings for players. WorldGuard flags are supported.");
			}
		}
		return false;
	}

}
