package me.sablednah.wooddye;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class RecipieDetail {
    private ItemStack item;
    private Material material;
    private DyeColor dye;
    /**
     * @return the material
     */
    public RecipieDetail(Material m, DyeColor d, ItemStack i) {
        this.material = m;
        this.dye = d;
        this.item = i;
    }
    
    public Material getMaterial() {
        return material;
    }
    /**
     * @param material the material to set
     */
    public void setMaterial(Material material) {
        this.material = material;
    }
    /**
     * @return the dye
     */
    public DyeColor getDye() {
        return dye;
    }
    /**
     * @param dye the dye to set
     */
    public void setDye(DyeColor dye) {
        this.dye = dye;
    }

    /**
     * @return the item
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(ItemStack item) {
        this.item = item;
    }
    
}
