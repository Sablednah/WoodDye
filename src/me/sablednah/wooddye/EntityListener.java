package me.sablednah.wooddye;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EntityListener implements Listener {

	public WoodDye plugin;

	public EntityListener(WoodDye instance) {
		this.plugin=instance;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event){
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;  //Not using on a block!
		}
		Block target = event.getClickedBlock();
		ItemStack item = event.getItem();
		if (target.getType() != Material.WOOD) {
			return; // Not clicked on wood
		}
		if (item.getType() != Material.INK_SACK) {
			return; // Not clicked with dye
		}
		
		if (WoodDye.debugMode) {
			System.out.print(target.getType() + ":" + target.getData() + " - " + item.getType() + ":" + item.getData().getData());
		}
		boolean hasDyed = false;
		
		switch (target.getData()) {
			case 1:
				if (item.getData().getData()==15) { // white
					target.setData((byte) 3);
					hasDyed = true;
				}
				break;
			case 3:
				if (item.getData().getData()==0 || item.getData().getData()==3) { // black - brown
					target.setData((byte) 1);
					hasDyed = true;
				}
				if (item.getData().getData()==15) { // white
					target.setData((byte) 0);
					hasDyed = true;
				}
				break;
			case 0:
				if (item.getData().getData()==0 || item.getData().getData()==3) { // black - brown
					target.setData((byte) 3);
					hasDyed = true;
				}
				if (item.getData().getData()==15) { // white
					target.setData((byte) 2);
					hasDyed = true;
				}
				break;
			case 2:
				if (item.getData().getData()==0 || item.getData().getData()==3) { // black - brown
					target.setData((byte) 0);
					hasDyed = true;
				}
				break;
			default:
				System.out.print("New wood type? - " + item.getType());
		}
		
		if (WoodDye.useItems && hasDyed) {
			ItemStack holding = item;
			holding.setAmount(holding.getAmount() - 1);
			event.getPlayer().setItemInHand(holding);
		}
	}
}
