package me.xgh69.pvptoggle.commands;

import me.xgh69.pvptoggle.PvpToggle;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class PvpCommand implements CommandExecutor
{	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
	{
		PvpToggle plugin = PvpToggle.getInstance();
		
		if(!(sender instanceof Player))
		{
			sender.sendMessage(plugin.getMessage("cmd_pvp_console").replace("$player", sender.getName()));
			return false;
		}
		
		Player player = (Player) sender;
		
		if(args.length == 1)
		{	
			if(args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("on"))
			{
				plugin.setPvpSettings(player.getName(), true);
				player.sendMessage(plugin.getMessage("cmd_pvp_enable").replace("$player", player.getName()));
				return true;
			}
			else if(args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("off"))
			{
				plugin.setPvpSettings(player.getName(), false);
				player.sendMessage(plugin.getMessage("cmd_pvp_disable").replace("$player", player.getName()));
				return true;
			}
			else if(args[0].equalsIgnoreCase("status"))
			{
				if(plugin.getPvpSettings(player.getName()))
				{
					player.sendMessage(plugin.getMessage("cmd_pvp_status") + plugin.getMessage("cmd_pvp_status_disable"));
					return true;
				}
				else if(!plugin.getPvpSettings(player.getName()))
				{
					player.sendMessage(plugin.getMessage("cmd_pvp_status") + plugin.getMessage("cmd_pvp_status_enable"));
					return true;
				}
			}
			else
			{
				player.sendMessage(plugin.getMessage("cmd_pvp_usage").replace("$player", player.getName()));
				return false;
			}
		}
		else
		{
			player.sendMessage(plugin.getMessage("cmd_pvp_usage").replace("$player", player.getName()));
			return false;
		}
		
		return false;
	}
	
}
