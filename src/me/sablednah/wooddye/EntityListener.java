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
        this.plugin = instance;
    }
    
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;  // Not using on a block!
        }
        Block target = event.getClickedBlock();
        ItemStack item = event.getItem();
        
        if (item == null || (item.getType() != Material.INK_SACK && item.getType() != Material.MAGMA_CREAM)) {
            return; // Not clicked with dye
        }
        
        if (event.getPlayer().hasPermission("wooddye.candye") ) { return; }
        
        boolean hasDyed = false;
        
        Material tt = target.getType();
        
        if (item.getType() == Material.INK_SACK) {
            
            boolean lighten = false;
            boolean darken = false;
            
            switch (item.getData().getData()) {
                case 0:// black
                case 3:// brown
                    darken = true;
                    break;
                case 7: // silver
                case 15: // white
                    lighten = true;
                    break;
            }
            if (item.getData().getData() == 0 || item.getData().getData() == 3) {
                darken = true;
            }// black - brown : darken
            if (item.getData().getData() == 15 || item.getData().getData() == 7) {
                lighten = true;
            } // white or silver :lighten
            
            if (WoodDye.debugMode) {
                System.out.print("WoodDye Click: " + target.getType() + ":" + target.getData() + " - " + item.getType() + ":" + item.getData().getData());
            }
            
            switch (tt) {
                case WOOD:
                case WOOD_DOUBLE_STEP:
                case WOOD_STEP:
                    /* 2 Birch Wood - white (15)
                     * 0 Oak Wood - Light Gray (7)
                     * 3 Jungle Wood - yellow (11)
                     * 4 Acacia Word - orange (14)
                     * 1 spruce - brown (3)
                     * 5 Dark Oak - black (0) */
                    switch (target.getData()) {
                        case 2: // birch
                            if (darken) {
                                target.setData((byte) 0);
                                hasDyed = true;
                            }
                            break;
                        case 0: // oak
                            if (darken) {
                                target.setData((byte) 3);
                                hasDyed = true;
                            } else if (lighten) {
                                target.setData((byte) 2);
                                hasDyed = true;
                            }
                            break;
                        case 3: // jungle
                            if (darken) {
                                target.setData((byte) 4);
                                hasDyed = true;
                            } else if (lighten) {
                                target.setData((byte) 0);
                                hasDyed = true;
                            }
                            break;
                        case 4: // accacia
                            if (darken) {
                                target.setData((byte) 1);
                                hasDyed = true;
                            } else if (lighten) {
                                target.setData((byte) 3);
                                hasDyed = true;
                            }
                            break;
                        case 1: // spruce
                            if (darken) {
                                target.setData((byte) 5);
                                hasDyed = true;
                            } else if (lighten) {
                                target.setData((byte) 4);
                                hasDyed = true;
                            }
                            break;
                        case 5:  // dark oak
                            if (lighten) { // white or silver :lighten
                                target.setData((byte) 1);
                                hasDyed = true;
                            }
                            break;
                        
                        case 10: // birch slab top
                            if (darken) {
                                target.setData((byte) 8);
                                hasDyed = true;
                            }
                            break;
                        case 8: // oak slab top
                            if (darken) {
                                target.setData((byte) 11);
                                hasDyed = true;
                            } else if (lighten) {
                                target.setData((byte) 10);
                                hasDyed = true;
                            }
                            break;
                        case 11: // jungle slab top
                            if (darken) {
                                target.setData((byte) 12);
                                hasDyed = true;
                            } else if (lighten) {
                                target.setData((byte) 8);
                                hasDyed = true;
                            }
                            break;
                        case 12: // accacia slab top
                            if (darken) {
                                target.setData((byte) 9);
                                hasDyed = true;
                            } else if (lighten) {
                                target.setData((byte) 11);
                                hasDyed = true;
                            }
                            break;
                        case 9: // spruce slab top
                            if (darken) {
                                target.setData((byte) 13);
                                hasDyed = true;
                            } else if (lighten) {
                                target.setData((byte) 12);
                                hasDyed = true;
                            }
                            break;
                        case 13:  // dark oak slab top
                            if (lighten) {
                                target.setData((byte) 9);
                                hasDyed = true;
                            }
                            break;
                        
                        default:
                            System.out.print("New wood type? - " + item.getType());
                    }
                    
                    break;
                case BIRCH_WOOD_STAIRS:
                    if (darken) {
                        target.setType(Material.WOOD_STAIRS);
                        hasDyed = true;
                    }
                    break;
                case WOOD_STAIRS:
                    if (darken) {
                        target.setType(Material.JUNGLE_WOOD_STAIRS);
                        hasDyed = true;
                    } else if (lighten) {
                        target.setType(Material.BIRCH_WOOD_STAIRS);
                        hasDyed = true;
                    }
                    break;
                case JUNGLE_WOOD_STAIRS:
                    if (darken) {
                        target.setType(Material.ACACIA_STAIRS);
                        hasDyed = true;
                    } else if (lighten) {
                        target.setType(Material.BIRCH_WOOD_STAIRS);
                        hasDyed = true;
                    }
                    break;
                case ACACIA_STAIRS:
                    if (darken) {
                        target.setType(Material.SPRUCE_WOOD_STAIRS);
                        hasDyed = true;
                    } else if (lighten) {
                        target.setType(Material.JUNGLE_WOOD_STAIRS);
                        hasDyed = true;
                    }
                    break;
                case SPRUCE_WOOD_STAIRS:
                    if (darken) {
                        target.setType(Material.DARK_OAK_STAIRS);
                        hasDyed = true;
                    } else if (lighten) {
                        target.setType(Material.ACACIA_STAIRS);
                        hasDyed = true;
                    }
                    break;
                case DARK_OAK_STAIRS:
                    if (lighten) {
                        target.setType(Material.SPRUCE_WOOD_STAIRS);
                        hasDyed = true;
                    }
                    break;
                default:
                    break;
            }
        } else if (item.getType() == Material.MAGMA_CREAM && WoodDye.fireProof) {
            switch (tt) {
                case WOOD:
                case WOOD_DOUBLE_STEP:
                    target.setType(Material.DOUBLE_STEP);
                    target.setData((byte) 2);
                    hasDyed = true;
                    break;
                case WOOD_STEP:
                    if (target.getData() > 7) {
                        target.setType(Material.STEP);
                        target.setData((byte) 10);
                    } else {
                        target.setType(Material.STEP);
                        target.setData((byte) 2);
                    }
                    hasDyed = true;
                    break;
            
            }
        }
        
        if (WoodDye.useItems && hasDyed) {
            ItemStack holding = item;
            holding.setAmount(holding.getAmount() - 1);
            event.getPlayer().setItemInHand(holding);
        }
    }
}
