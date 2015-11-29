package me.xgh69.pvptoggle.commands;

import me.xgh69.pvptoggle.PvpManager;
import me.xgh69.pvptoggle.PvpToggle;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpCommand implements CommandExecutor
{	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
	{
		PvpToggle plugin = PvpToggle.getInstance();
		PvpManager pvpmanager = plugin.getPvpManager();
		
		if(!(sender instanceof Player))
		{
			sender.sendMessage(plugin.getMessage("cmd_pvp_console").replace("$player", sender.getName()));
			return true;
		}
		
		Player player = (Player) sender;
		
		if(args.length != 1)
		{
			player.sendMessage(plugin.getMessage("cmd_pvp_usage").replace("$player", player.getName()));
			return true;
		}

		switch(args[0].toLowerCase())
		{
			case "enable":
			case "on":
				pvpmanager.setPvpProtection(player.getUniqueId(), true);
				player.sendMessage(plugin.getMessage("cmd_pvp_enable").replace("$player", player.getName()));
				break;
			case "disable":
			case "off":
				pvpmanager.setPvpProtection(player.getUniqueId(), false);
				player.sendMessage(plugin.getMessage("cmd_pvp_disable").replace("$player", player.getName()));
				break;
			case "status":
				if(pvpmanager.getPvpProtection(player.getUniqueId()))
				{
					player.sendMessage(plugin.getMessage("cmd_pvp_status").replace("$player", player.getName()) + plugin.getMessage("cmd_pvp_status_enable"));
				}
				else if(!pvpmanager.getPvpProtection(player.getUniqueId()))
				{
					player.sendMessage(plugin.getMessage("cmd_pvp_status").replace("$player", player.getName()) + plugin.getMessage("cmd_pvp_status_disable"));
				}
				break;
			default:
				player.sendMessage(plugin.getMessage("cmd_pvp_usage").replace("$player", player.getName()));
				break;
		}

		return true;
	}
}
