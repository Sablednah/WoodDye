package me.sablednah.wooddye;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class PluginCommandExecutor implements CommandExecutor {
	public WoodDye plugin;

	public PluginCommandExecutor(WoodDye instance) {
		this.plugin=instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("wooddye")){
			if (args.length > 0 && args[0].toLowerCase().equals("reload")) {
				Boolean doReload = false;
				
				if (sender instanceof Player) {
					if( sender.hasPermission("wooddye.reload") ) {
						doReload = true;
					} else {
						sender.sendMessage("You do not have permission to reload.");
						return true;
					}
				} else {
					doReload = true;
				}

				if (doReload) {
					plugin.reloadConfig();
					WoodDye.debugMode = plugin.getConfig().getBoolean("debugMode");
					WoodDye.useItems = plugin.getConfig().getBoolean("useItems");
					WoodDye.fireProof = plugin.getConfig().getBoolean("fireProof");
				
					plugin.reloadLangConfig();
					
					WoodDye.wooddyeMessage = plugin.getLangConfig().getString("wooddyeMessage");
	
					if (WoodDye.debugMode) {
						WoodDye.logger.info("[" + WoodDye.myName + "] DebugMode enabled.");
					}
					if (WoodDye.useItems) {
						WoodDye.logger.info("[" + WoodDye.myName + "] Consumes items.");
					}

					return true;
				}
			}
		}
		return false; 
	}
}