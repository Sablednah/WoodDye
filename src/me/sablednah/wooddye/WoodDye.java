package me.sablednah.wooddye;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WoodDye extends JavaPlugin {
    
    public static WoodDye plugin;
    public final static Logger logger = Logger.getLogger("Minecraft");
    public final EntityListener EntityListener = new EntityListener(this);
    
    public static Boolean debugMode;
    public static Boolean useItems;
    public static Boolean fireProof;
    public static String wooddyeMessage;
    
    private PluginCommandExecutor myExecutor;
    public static PluginDescriptionFile pdfFile;
    public static String myName;
    
    public static FileConfiguration LangConfig = null;
    public static File LangConfigurationFile = null;
    
    public static ItemStack wood_birch = new ItemStack(Material.WOOD);
    public static ItemStack wood_oak = new ItemStack(Material.WOOD);
    public static ItemStack wood_jungle = new ItemStack(Material.WOOD);
    public static ItemStack wood_acacia = new ItemStack(Material.WOOD);
    public static ItemStack wood_spruce = new ItemStack(Material.WOOD);
    public static ItemStack wood_darkoak = new ItemStack(Material.WOOD);
    
    public static ItemStack step_birch = new ItemStack(Material.WOOD_STEP);
    public static ItemStack step_oak = new ItemStack(Material.WOOD_STEP);
    public static ItemStack step_jungle = new ItemStack(Material.WOOD_STEP);
    public static ItemStack step_acacia = new ItemStack(Material.WOOD_STEP);
    public static ItemStack step_spruce = new ItemStack(Material.WOOD_STEP);
    public static ItemStack step_darkoak = new ItemStack(Material.WOOD_STEP);
    
    public ItemStack[] slabs = null;
    public ItemStack[] planks = null;
    public Material[] stairs = new Material[] { Material.BIRCH_WOOD_STAIRS, Material.WOOD_STAIRS, Material.JUNGLE_WOOD_STAIRS, Material.ACACIA_STAIRS, Material.SPRUCE_WOOD_STAIRS,
            Material.DARK_OAK_STAIRS };
    public DyeColor[] colours = new DyeColor[] { DyeColor.WHITE, DyeColor.SILVER, DyeColor.YELLOW, DyeColor.ORANGE, DyeColor.BROWN, DyeColor.BLACK };
    
    public static ArrayList<RecipieDetail> recipieList = new ArrayList<RecipieDetail>();

    public WoodDye() {
        wood_birch.setDurability((short) 2);
        wood_oak.setDurability((short) 0);
        wood_jungle.setDurability((short) 3);
        wood_acacia.setDurability((short) 4);
        wood_spruce.setDurability((short) 1);
        wood_darkoak.setDurability((short) 5);
        
        step_birch.setDurability((short) 2);
        step_oak.setDurability((short) 0);
        step_jungle.setDurability((short) 3);
        step_acacia.setDurability((short) 4);
        step_spruce.setDurability((short) 1);
        step_darkoak.setDurability((short) 5);
        
        slabs = new ItemStack[] { step_birch, step_oak, step_jungle, step_acacia, step_spruce, step_darkoak };
        planks = new ItemStack[] { wood_birch, wood_oak, wood_jungle, wood_acacia, wood_spruce, wood_darkoak };
        
        recipieList.add(new RecipieDetail(Material.WOOD, DyeColor.WHITE,wood_birch));
        recipieList.add(new RecipieDetail(Material.WOOD, DyeColor.SILVER,wood_oak));
        recipieList.add(new RecipieDetail(Material.WOOD, DyeColor.YELLOW,wood_jungle));
        recipieList.add(new RecipieDetail(Material.WOOD, DyeColor.ORANGE, wood_acacia));
        recipieList.add(new RecipieDetail(Material.WOOD, DyeColor.BROWN, wood_spruce));
        recipieList.add(new RecipieDetail(Material.WOOD, DyeColor.BLACK, wood_darkoak));
        
        recipieList.add(new RecipieDetail(Material.WOOD_STEP, DyeColor.WHITE,step_birch));
        recipieList.add(new RecipieDetail(Material.WOOD_STEP, DyeColor.SILVER,step_oak));
        recipieList.add(new RecipieDetail(Material.WOOD_STEP, DyeColor.YELLOW, step_jungle));
        recipieList.add(new RecipieDetail(Material.WOOD_STEP, DyeColor.ORANGE, step_acacia));
        recipieList.add(new RecipieDetail(Material.WOOD_STEP, DyeColor.BROWN, step_spruce));
        recipieList.add(new RecipieDetail(Material.WOOD_STEP, DyeColor.BLACK, step_darkoak));
        
        for (int i = 0; i < stairs.length; i++) {
            for (Material m : stairs) {
                //System.out.print("StairsRecipies: " + i + " : " + stairs[i] + " " + m + " " + colours[i]);
                recipieList.add(new RecipieDetail(m, colours[i], new ItemStack(stairs[i])));
            }
        }
        
    }
    
    @Override
    public void onDisable() {
        plugin.getServer().getScheduler().cancelTasks(plugin);
        logger.info("[" + myName + "] --- END OF LINE ---");
    }
    
    @Override
    public void onEnable() {
        plugin = this;
        pdfFile = plugin.getDescription();
        myName = pdfFile.getName();
        
        PluginManager pm = getServer().getPluginManager();
        
        pm.registerEvents(this.EntityListener, this);
        
        myExecutor = new PluginCommandExecutor(plugin);
        getCommand("wooddye").setExecutor(myExecutor);
        
        loadConfiguration();
        
        if (debugMode) {
            logger.info("[" + myName + "] DebugMode Enabled.");
        }
        if (useItems) {
            logger.info("[" + myName + "] Consumes Items.");
        }
        
        this.addRecipies();
        
    }
    
    @SuppressWarnings("deprecation")
    private void addRecipies() {
        ShapelessRecipe recipe = null;
        Dye dye = new Dye();
        MaterialData md = null;
        /* Birch Wood - white (15)
         * Oak Wood - Light Gray (7)
         * Jungle Wood - yellow (11)
         * Acacia Word - orange (14)
         * spruce - brown (3)
         * Dark Oak - black (0) */
        
        //craft stairs - dye slabs
        
        for (RecipieDetail rd : recipieList) {
            ItemStack item = rd.getItem();
            Material m = rd.getMaterial();
            DyeColor d = rd.getDye();
            
            md = new MaterialData(m);
            
            //System.out.print("Adding Recipie: " + item + " " + m + " " + d);
            
            if (m == Material.WOOD || m == Material.WOOD_STEP) {
                for (int i = 0; i < 6; i++) {// loop through so we can change any wood type to any type.
                    // set wood type - awaiting replacement for depreciated method
                    md.setData((byte) i);
                    recipe = new ShapelessRecipe(item);
                    dye.setColor(d);
                    recipe.addIngredient(dye);
                    recipe.addIngredient(md);
                    this.getServer().addRecipe(recipe);
                }
            } else {
                recipe = new ShapelessRecipe(item);
                dye.setColor(d);
                recipe.addIngredient(dye);
                recipe.addIngredient(md);
                this.getServer().addRecipe(recipe);
            }
        }
    }
    
    /**
     * Initialise config file
     */
    public void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        
        String headertext;
        headertext = "Default " + WoodDye.myName + " Config file\r\n";
        headertext += "\r\n";
        headertext += "useItems: [true|false] Consume items when dying.\r\n";
        headertext += "\r\n";
        headertext += "debugMode: [true|false] Enable extra debug info in logs.\r\n";
        headertext += "\r\n";
        
        getConfig().options().header(headertext);
        getConfig().options().copyHeader(true);
        
        debugMode = getConfig().getBoolean("debugMode");
        useItems = getConfig().getBoolean("useItems");
        fireProof = getConfig().getBoolean("fireProof");
        
        saveConfig();
        
        getLangConfig();
        
        wooddyeMessage = getLangConfig().getString("wooddyeMessage");
        
        saveLangConfig();
        
    }
    
    public void reloadLangConfig() {
        if (LangConfigurationFile == null) {
            LangConfigurationFile = new File(getDataFolder(), "lang.yml");
        }
        LangConfig = YamlConfiguration.loadConfiguration(LangConfigurationFile);
        LangConfig.options().copyDefaults(true);
        
        // Look for defaults in the jar
        InputStream defConfigStream = getResource("lang.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            LangConfig.setDefaults(defConfig);
        }
    }
    
    public FileConfiguration getLangConfig() {
        if (LangConfig == null) {
            reloadLangConfig();
        }
        return LangConfig;
    }
    
    public void saveLangConfig() {
        if (LangConfig == null || LangConfigurationFile == null) {
            return;
        }
        try {
            LangConfig.save(LangConfigurationFile);
        } catch (IOException ex) {
            logger.severe("Could not save Lang config to " + LangConfigurationFile + " " + ex);
        }
    }
}
