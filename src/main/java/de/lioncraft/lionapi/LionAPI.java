package de.lioncraft.lionapi;

import de.lioncraft.lionapi.addons.AddonManager;
import de.lioncraft.lionapi.addons.builtIn.TimerAddon;
import de.lioncraft.lionapi.challenge.ChallengeController;
import de.lioncraft.lionapi.challenge.SimpleSpeedrunChallenge;
import de.lioncraft.lionapi.challenge.SurvivalServerChallenge;
import de.lioncraft.lionapi.commands.DebugCommand;
import de.lioncraft.lionapi.commands.Teams;
import de.lioncraft.lionapi.commands.timerCommand;
import de.lioncraft.lionapi.data.ChallengeSettings;
import de.lioncraft.lionapi.data.ConfigManager;
import de.lioncraft.lionapi.data.Settings;
import de.lioncraft.lionapi.events.saveDataEvent;
import de.lioncraft.lionapi.guimanagement.*;
import de.lioncraft.lionapi.guimanagement.lionclient.DisplayManager;
import de.lioncraft.lionapi.hiddenclicks.ClickCommand;
import de.lioncraft.lionapi.listeners.*;
import de.lioncraft.lionapi.messageHandling.ColorGradient;
import de.lioncraft.lionapi.messageHandling.defaultMessages;
import de.lioncraft.lionapi.messageHandling.lang.LanguageFileManager;
import de.lioncraft.lionapi.messageHandling.lionchat.ChannelConfiguration;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.permissions.LionAPIPermissions;
import de.lioncraft.lionapi.playerSettings.PlayerSettings;
import de.lioncraft.lionapi.teams.Backpack;
import de.lioncraft.lionapi.teams.Team;
import de.lioncraft.lionapi.timer.*;
import de.lioncraft.lionapi.velocity.ProxyMessageListeners;
import de.lioncraft.lionapi.velocity.connections.ConnectionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;


public final class LionAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        plugin = this;

        saveResourceIfNotExists("/timer-color-presets.yml", getDataPath().resolve("timer-color-presets.yml"));

        ConfigurationSerialization.registerClass(TimerSnapshot.class);
        ConfigurationSerialization.registerClass(Backpack.class);
        ConfigurationSerialization.registerClass(Team.class);
        ConfigurationSerialization.registerClass(Team.class, "LionTeam");
        ConfigurationSerialization.registerClass(TimerSnapshot.class);
        ConfigurationSerialization.registerClass(ChallengeController.class);
        ConfigurationSerialization.registerClass(ChallengeSettings.class);
        ConfigurationSerialization.registerClass(SimpleSpeedrunChallenge.class);
        ConfigurationSerialization.registerClass(SurvivalServerChallenge.class);
        ConfigurationSerialization.registerClass(PlayerSettings.class);

        saveDefaultConfig();

        LanguageFileManager.saveLangFiles(this);
        languageManager = LanguageFileManager.createManager(plugin, getConfig().getString("language", "en_us"));

        LionChat.registerChannel("system", new ChannelConfiguration(false,
                TextColor.color(100, 200, 200),
                ColorGradient.getNewGradiant("LionSystems", TextColor.color(250, 0, 250), TextColor.color(100, 50, 255)),
                true));
        LionChat.registerChannel("debug", new ChannelConfiguration(true,
                TextColor.color(100, 100, 100),
                Component.text("DEBUG", NamedTextColor.DARK_BLUE),
                false));
        LionChat.registerChannel("msg", new ChannelConfiguration(false, TextColor.color(150, 150, 255),
                Component.text("MSG", TextColor.color(60, 60, 255)),
                true));
        LionChat.registerChannel("teammsg", new ChannelConfiguration(false, TextColor.color(180, 255, 180),
                Component.text("TeamMSG", TextColor.color(60, 255, 60)),
                true));
        LionChat.registerChannel("log", new ChannelConfiguration(true, TextColor.color(180, 180, 180),
                Component.text("LOG", TextColor.color(100, 100, 100)),
                false));


        PlayerSettings.deserializeAll();

        ConfigManager configManager = new ConfigManager(this);
        configManager.loadAndCheckConfig();

        for (LionAPIPermissions lap : LionAPIPermissions.values()){
            Bukkit.getPluginManager().addPermission(new Permission(
                    lap.getMcid(),
                    lap.getDescription(),
                    lap.getPmd()
            ));
        }

        LanguageFileManager.initColorCodeReplacements();
        TimerConfig.init();
        Settings.init();
        defaultMessages.setValues();
        Items.setItems();
        randomizer.InitializeAllowedLists();


        Bukkit.getPluginManager().registerEvents(new invClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new listeners(), this);
        Bukkit.getPluginManager().registerEvents(new timerListeners(), this);
        Bukkit.getPluginManager().registerEvents(new SimpleChallengeRelatedListeners(), this);
        Bukkit.getPluginManager().registerEvents(new LionButtonListeners(), this);
        Bukkit.getPluginManager().registerEvents(new LionGUIElementsListeners(), this);
        Bukkit.getPluginManager().registerEvents(new SettingsListeners(), this);

        registerCommand("timer","Timer & Challenge Management", new timerCommand());
        registerCommand("teams", new Teams());
        //registerCommand("hiddenclickapi", new ClickCommand());
        registerCommand("debug", new DebugCommand());


        ProxyMessageListeners.register(this);
        DisplayManager.register(this);

        MainTimer.getTimer();
        Bukkit.getScheduler().runTaskLater(this, Team::loadAll, 7);

        if (getConfig().getBoolean("settings.challenge-server")){
            if (getConfig().contains("persistent-data.challenge")) ChallengeController.setInstance((ChallengeController) getConfig().get("challenge"));
        }else{
            ChallengeController.setInstance(new SurvivalServerChallenge());
        }

        AddonManager.registerAddon(new TimerAddon());

        ConnectionManager.initialize(getConfig());

        getLogger().info("Successfully enabled LionAPI.");
    }

    public static ClassLoader getLionAPIClassLoader(){
        return plugin.getClassLoader();
    }

    @Override
    public void onDisable() {
        Bukkit.getPluginManager().callEvent(new saveDataEvent());
        new listeners().onSaveEvent(new saveDataEvent());
        plugin.getLogger().info("Successfully disabled LionAPI.");

        try {
            PlayerSettings.serializeAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static LionAPI plugin;

    public static Plugin getPlugin(){
        return plugin;
    }
    private static LanguageFileManager languageManager;
    public static String getLanguage(){
        return plugin.getConfig().getString("language");
    }

    public static LanguageFileManager getLanguageManager() {
        return languageManager;
    }
    public static LanguageFileManager lm(){
        return getLanguageManager();
    }

    private boolean saveResourceIfNotExists(String resource, Path outputPath) {
        if (resource == null || !resource.startsWith("/")) {
            System.err.println("Error: Resource path must be a non-empty absolute path starting with '/' (e.g., /com/example/file.txt).");
            return false;
        }

        File targetFile = outputPath.toFile();

        if (targetFile.exists()) {
            if (targetFile.isDirectory()) {
                return false;
            }
            return false;
        }
        try {
            InputStream resourceStream = LionAPI.class.getResourceAsStream(resource);
            {
                if (resourceStream == null) {
                    return false;
                }
                Path parentDir = outputPath.getParent();
                if (parentDir != null) {
                    if (!Files.exists(parentDir)) {
                        Files.createDirectories(parentDir);
                    }
                }

                Files.copy(resourceStream, outputPath);
                return true;
            }
        }catch(IOException e) {
            System.err.println("Error saving resource '" + resource + "' to '" + outputPath + "': " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (SecurityException e) {
            System.err.println("Error creating directories for '" + outputPath + "' due to security restrictions: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
