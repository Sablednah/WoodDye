package me.sablednah.wooddye;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import java.util.Scanner;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;



public class WoodDye extends JavaPlugin {
	public static WoodDye plugin;
	public final static Logger logger = Logger.getLogger("Minecraft");
	public final EntityListener EntityListener = new EntityListener(this);
	
	public static Boolean debugMode;
	public static String wooddyeMessage;
	
	private PluginCommandExecutor myExecutor;
	public static PluginDescriptionFile pdfFile;
	public static String myName;	
	private String VersionNew;
	private String VersionCurrent;

	public static FileConfiguration LangConfig = null;
	public static File LangConfigurationFile = null;

	
//	public static Map<Player, Boolean> pluginEnabled = null;

	public static String[] animalList = { "Pig", "Sheep", "Cow", "Chicken",
			"MushroomCow", "Golem", "IronGolem", "Snowman", "Squid",
			"Villager", "Wolf", "Ocelot" };
	public static String[] monsterList = { "Blaze", "Zombie", "Creeper",
			"Skeleton", "Spider", "Ghast", "MagmaCube", "Slime", "CaveSpider",
			"EnderDragon", "EnderMan", "Giant", "PigZombie", "SilverFish",
			"Spider" };

	public String[] entityList = concat(animalList, monsterList);


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
		VersionCurrent = pdfFile.getVersion();

		// logger.info("[" + myName + "] Version " + pdfFile.getVersion() +
		// " starting.");

		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(this.EntityListener, this);
		
		myExecutor = new PluginCommandExecutor(plugin);
		getCommand("wooddye").setExecutor(myExecutor);

		loadConfiguration();

		if (debugMode) {
			logger.info("[" + myName + "] DebugMode Enabled.");
		}
		
		
		/**
		 * Schedule a version check every 6 hours for update notification .
		 */
		this.getServer().getScheduler()
				.scheduleAsyncRepeatingTask(this, new Runnable() {
					@Override
					public void run() {
						try {
							VersionNew = getNewVersion(VersionCurrent);
							String VersionOld = getDescription().getVersion();
							if (!VersionNew.contains(VersionOld)) {
								logger.warning("["+WoodDye.myName+"] " + VersionNew + " is available. You're using " + VersionOld);
								logger.warning("["+WoodDye.myName+"] http://dev.bukkit.org/server-mods/"+WoodDye.myName+"/");
							}

						} catch (Exception e) {
							// ignore exceptions
						}
					}
				}, 0, 432000);

		logger.info("[" + myName + "] Online.");
	}


	/**
	 * Initialise config file
	 */
	public void loadConfiguration() {
		getConfig().options().copyDefaults(true);

		String headertext;
		headertext = "Default "+WoodDye.myName+" Config file\r\n\r\n";
		headertext += "debugMode: [true|false] Enable extra debug info in logs.\r\n";
		headertext += "\r\n";

		getConfig().options().header(headertext);
		getConfig().options().copyHeader(true);

		debugMode = getConfig().getBoolean("debugMode");

		saveConfig();

		getLangConfig();

		wooddyeMessage = getLangConfig().getString("wooddyeMessage");

		saveLangConfig();


	}

	/**
	 * Get latest version of plugin from remote server.
	 * 
	 * @param VersionCurrent
	 *            String of current version to compare (returned in cases such
	 *            as update server is unavailable).
	 * @return returns Latest version as String
	 * @throws Exception
	 */
	public String getNewVersion(String VersionCurrent) throws Exception {
		String urlStr = "http://sablekisska.co.uk/asp/"+WoodDye.myName+"version.asp";
		try {

			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			HttpURLConnection.setFollowRedirects(true);
			conn.setRequestProperty("User-agent", "["+WoodDye.myName+" Plugin] " + VersionCurrent);
			String inStr = null;
			inStr = convertStreamToString(conn.getInputStream());
			return inStr;

		} catch (Exception localException) {
			// System.out.print("exp: " + localException);
		}
		return VersionCurrent;
	}

	/**
	 * Converts InputStream to String
	 * 
	 * One-line 'hack' to convert InputStreams to strings.
	 * 
	 * @param is
	 *            The InputStream to convert
	 * @return returns a String version of 'is'
	 */
	public String convertStreamToString(InputStream is) {
		return new Scanner(is).useDelimiter("\\A").next();
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
			YamlConfiguration defConfig = YamlConfiguration .loadConfiguration(defConfigStream);
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



	/**
	 * Joins two arrays
	 * 
	 * @param first
	 *            array
	 * @param second
	 *            array
	 * @return Arrays joined
	 */
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
}
