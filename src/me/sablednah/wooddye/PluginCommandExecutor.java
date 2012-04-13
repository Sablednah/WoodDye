package me.sablednah.wooddye;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class PluginCommandExecutor implements CommandExecutor {
	public WoodDye plugin;

	public PluginCommandExecutor(WoodDye instance) {
		this.plugin=instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("impact")){
			//command
		}
		return false; 
	}
}