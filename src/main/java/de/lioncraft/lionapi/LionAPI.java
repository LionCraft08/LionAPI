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
import de.lioncraft.lionapi.guimanagement.Interaction.Button;
import de.lioncraft.lionapi.guimanagement.Interaction.MultipleSelection;
import de.lioncraft.lionapi.guimanagement.lionclient.DisplayManager;
import de.lioncraft.lionapi.hiddenclicks.ClickCommand;
import de.lioncraft.lionapi.listeners.*;
import de.lioncraft.lionapi.messageHandling.ColorGradient;
import de.lioncraft.lionapi.messageHandling.defaultMessages;
import de.lioncraft.lionapi.messageHandling.lionchat.ChannelConfiguration;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.playerSettings.PlayerSettings;
import de.lioncraft.lionapi.teams.Backpack;
import de.lioncraft.lionapi.teams.DeserializeTeams;
import de.lioncraft.lionapi.teams.Team;
import de.lioncraft.lionapi.timer.*;
import de.lioncraft.lionapi.velocity.ProxyMessageListeners;
import de.lioncraft.lionapi.velocity.connections.ConnectionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;


public final class LionAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        plugin = this;

        saveResource("timer-color-presets.yml", false);

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

        registerCommand("timer","Timer & Challenge Management",new timerCommand());
        registerCommand("teams", new Teams());
        registerCommand("hiddenclickapi", new ClickCommand());
        registerCommand("debug", new DebugCommand());


        ProxyMessageListeners.register(this);
        DisplayManager.register(this);

        MainTimer.getTimer();
        new DeserializeTeams().runTaskLater(this, 10);

        if (getConfig().getBoolean("settings.challenge-server")){
            if (getConfig().contains("persistent-data.challenge")) ChallengeController.setInstance((ChallengeController) getConfig().get("challenge"));
        }else{
            ChallengeController.setInstance(new SurvivalServerChallenge());
        }

        AddonManager.registerAddon(new TimerAddon());

        ConnectionManager.initialize(getConfig());

        getLogger().info("Successfully enabled LionAPI.");
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
    static Plugin plugin;

    public static Plugin getPlugin(){
        return plugin;
    }

}
