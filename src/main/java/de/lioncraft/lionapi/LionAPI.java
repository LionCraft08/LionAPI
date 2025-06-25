package de.lioncraft.lionapi;

import de.lioncraft.lionapi.challenge.ChallengeController;
import de.lioncraft.lionapi.challenge.SimpleSpeedrunChallenge;
import de.lioncraft.lionapi.commands.Teammsg;
import de.lioncraft.lionapi.commands.Teams;
import de.lioncraft.lionapi.commands.timerCommand;
import de.lioncraft.lionapi.data.ChallengeSettings;
import de.lioncraft.lionapi.data.Settings;
import de.lioncraft.lionapi.events.saveDataEvent;
import de.lioncraft.lionapi.guimanagement.*;
import de.lioncraft.lionapi.guimanagement.Interaction.Button;
import de.lioncraft.lionapi.guimanagement.Interaction.MultipleSelection;
import de.lioncraft.lionapi.hiddenclicks.ClickCommand;
import de.lioncraft.lionapi.hiddenclicks.HiddenKlick;
import de.lioncraft.lionapi.listeners.SimpleChallengeRelatedListeners;
import de.lioncraft.lionapi.listeners.invClickListener;
import de.lioncraft.lionapi.listeners.listeners;
import de.lioncraft.lionapi.listeners.timerListeners;
import de.lioncraft.lionapi.messageHandling.ColorGradient;
import de.lioncraft.lionapi.messageHandling.DM;
import de.lioncraft.lionapi.messageHandling.defaultMessages;
import de.lioncraft.lionapi.messageHandling.lionchat.ChannelConfiguration;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.teams.Backpack;
import de.lioncraft.lionapi.teams.DeserializeTeams;
import de.lioncraft.lionapi.teams.Team;
import de.lioncraft.lionapi.timer.*;
import io.papermc.paper.plugin.PermissionManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public final class LionAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();

        ConfigurationSerialization.registerClass(TimerSnapshot.class);
        ConfigurationSerialization.registerClass(Backpack.class);
        ConfigurationSerialization.registerClass(Team.class);
        ConfigurationSerialization.registerClass(Team.class, "LionTeam");
        ConfigurationSerialization.registerClass(TimerSnapshot.class);
        ConfigurationSerialization.registerClass(ChallengeController.class);
        ConfigurationSerialization.registerClass(ChallengeSettings.class);
        ConfigurationSerialization.registerClass(SimpleSpeedrunChallenge.class);

        Setting.SettingList = new HashMap<>();
        button.activeButtons = new HashMap<>();
        multipleSelection.multipleSelectionMap = new HashMap<>();
        ScrollableInterface.activeInterfaces = new HashMap<>();
        Button.activeButtons = new HashMap<>();
        de.lioncraft.lionapi.guimanagement.Interaction.Setting.SettingList = new HashMap<>();
        MultipleSelection.multipleSelectionMap = new HashMap<>();

        LionChat.registerChannel("debug", new ChannelConfiguration(true,
                TextColor.color(100, 100, 100),
                Component.text("DEBUG", NamedTextColor.DARK_BLUE),
                false));
        LionChat.registerChannel("system", new ChannelConfiguration(false,
                TextColor.color(200, 100, 100),
                ColorGradient.getNewGradiant("LionSystems", TextColor.color(250, 0, 250), TextColor.color(50, 0, 255)),
                true));
        LionChat.registerChannel("msg", new ChannelConfiguration(false, TextColor.color(150, 150, 255),
                Component.text("MSG", TextColor.color(60, 60, 255)),
                true));
        LionChat.registerChannel("teammsg", new ChannelConfiguration(false, TextColor.color(180, 255, 180),
                Component.text("TeamMSG", TextColor.color(60, 255, 60)),
                true));
        LionChat.registerChannel("log", new ChannelConfiguration(true, TextColor.color(180, 180, 180),
                Component.text("LOG", TextColor.color(100, 100, 100)),
                false));

        Settings.init();
        defaultMessages.setValues();
        buttons.setItems();
        Items.setItems();
        randomizer.InitializeAllowedLists();
        if (getConfig().contains("persistent-data.challenge")) ChallengeController.setInstance((ChallengeController) getConfig().get("challenge"));

        Bukkit.getPluginManager().registerEvents(new invClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new listeners(), this);
        Bukkit.getPluginManager().registerEvents(new timerListeners(), this);
        Bukkit.getPluginManager().registerEvents(new SimpleChallengeRelatedListeners(), this);

//        getCommand("timer").setExecutor(new timerCommand());
//        getCommand("hiddenclickapi").setExecutor(new ClickCommand());
//        getCommand("teams").setExecutor(new Teams());
//        getCommand("teammsg").setExecutor(new Teammsg());
//        getCommand("backpack").setExecutor(new de.lioncraft.lionapi.commands.Backpack());
        //getCommand("debug").setExecutor(new DebugCommand());

        MainTimer.getTimer();
        new DeserializeTeams().runTaskLater(this, 10);
        Bukkit.getLogger().info("<LionSystems> Successfully enabled LionAPI.");
    }

    @Override
    public void onDisable() {
        Bukkit.getPluginManager().callEvent(new saveDataEvent());
        new listeners().onSaveEvent(new saveDataEvent());
        Bukkit.getLogger().info("<LionSystems> Successfully disabled LionAPI.");
    }
    static Plugin plugin;

    public static Plugin getPlugin(){
        return plugin;
    }

}
