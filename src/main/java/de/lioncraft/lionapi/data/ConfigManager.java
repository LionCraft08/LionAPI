package de.lioncraft.lionapi.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.logging.Level;

/**
 * Manages the plugin's config.yml.
 * It handles version checking and updates the configuration from the
 * embedded default config, preserving user-set values.
 */
public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration savedConfig;
    private final File configFile;
    private static final String CONFIG_VERSION_PATH = "config-version";

    /**
     * Constructor for the ConfigManager.
     * @param plugin The instance of your plugin.
     */
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
    }

    /**
     * Loads the configuration and runs the update check.
     * This should be called from your plugin's onEnable() method.
     */
    public void loadAndCheckConfig() {
        // Ensure the plugin data folder exists
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        // Save the default config if it doesn't exist
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }

        // Load the saved configuration from the file
        savedConfig = YamlConfiguration.loadConfiguration(configFile);

        // Load the default configuration from the plugin's JAR
        YamlConfiguration defaultConfig = loadDefaultConfig();
        if (defaultConfig == null) {
            plugin.getLogger().severe("Could not load the default config.yml from the plugin JAR.");
            return;
        }

        // Check if an update is needed
        if (isUpdateNeeded(savedConfig, defaultConfig)) {
            plugin.getLogger().info("A newer configuration file was found. Updating your config.yml...");
            updateConfig(savedConfig, defaultConfig);
            saveConfig();
            plugin.getLogger().info("Your config.yml has been successfully updated with new values!");
        } else {
            plugin.getLogger().info("Config.yml is up to date.");
        }

        // Reload the config into memory to ensure all parts of the plugin use the latest values
        plugin.reloadConfig();
    }

    /**
     * Checks if the saved config version is older than the default config version.
     *
     * @param savedConfig   The configuration loaded from the user's file.
     * @param defaultConfig The configuration loaded from the plugin's JAR.
     * @return True if an update is required, false otherwise.
     */
    private boolean isUpdateNeeded(FileConfiguration savedConfig, FileConfiguration defaultConfig) {
        // Default to -1 if version numbers are not found to handle potential errors.
        double savedVersion = savedConfig.getDouble(CONFIG_VERSION_PATH, -1);
        double defaultVersion = defaultConfig.getDouble(CONFIG_VERSION_PATH, -1);

        return savedVersion < defaultVersion;
    }

    /**
     * Updates the saved config by adding new keys from the default config.
     * It recursively checks for new paths and preserves existing user values.
     *
     * @param savedConfig   The configuration to update.
     * @param defaultConfig The source configuration with the new values.
     */
    private void updateConfig(ConfigurationSection savedConfig, ConfigurationSection defaultConfig) {
        if (savedConfig == null || defaultConfig == null) return;

        Set<String> defaultKeys = defaultConfig.getKeys(true);

        for (String key : defaultKeys) {
            // If the saved config doesn't have this path, add it from the default config
            if (!savedConfig.contains(key, true)) {
                savedConfig.set(key, defaultConfig.get(key));
            }
        }

        // Finally, update the version number in the saved config
        savedConfig.set(CONFIG_VERSION_PATH, defaultConfig.getDouble(CONFIG_VERSION_PATH));
    }


    /**
     * Loads the default config.yml from within the plugin's JAR file.
     *
     * @return A YamlConfiguration object of the default config, or null on error.
     */
    private YamlConfiguration loadDefaultConfig() {
        try (InputStream defaultConfigStream = plugin.getResource("config.yml")) {
            if (defaultConfigStream == null) {
                return null;
            }
            return YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream));
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not read default config.yml", e);
            return null;
        }
    }

    /**
     * Saves the in-memory configuration to the config.yml file.
     */
    private void saveConfig() {
        try {
            savedConfig.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save the updated config.yml", e);
        }
    }
}
