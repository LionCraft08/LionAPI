package de.lioncraft.lionapi.listeners;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.challenge.ChallengeController;
import de.lioncraft.lionapi.events.saveDataEvent;
import de.lioncraft.lionapi.teams.Team;
import de.lioncraft.lionapi.timer.MainTimer;
import de.lioncraft.lionapi.timer.TimerConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.IOException;

public class listeners implements Listener {
    public static long delay;
    @EventHandler
    public void onSaveEvent(saveDataEvent e){
        LionAPI.getPlugin().getConfig().set("persistent-data.timer", MainTimer.getTimer().getNewSnapshot());
        YamlConfiguration config = new YamlConfiguration();
        config.set("controller", ChallengeController.getInstance());
        try {
            config.save(ChallengeController.getConfigFile());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        LionAPI.getPlugin().saveConfig();
        TimerConfig.save();
        Team.saveAll();
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent e){
        if (System.currentTimeMillis() - delay > 10000) {
            delay = System.currentTimeMillis();
            Bukkit.getPluginManager().callEvent(new saveDataEvent());
        }
    }
    @EventHandler
    public void onJoin(PlayerCommandSendEvent e){
        e.getCommands().remove("hiddenclickapi");
        e.getCommands().remove("lionapi:hiddenclickapi");
    }
}
