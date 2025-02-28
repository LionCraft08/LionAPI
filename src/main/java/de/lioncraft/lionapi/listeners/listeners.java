package de.lioncraft.lionapi.listeners;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.events.saveDataEvent;
import de.lioncraft.lionapi.messageHandling.DM;
import de.lioncraft.lionapi.messageHandling.defaultMessages;
import de.lioncraft.lionapi.teams.Team;
import de.lioncraft.lionapi.timer.MainTimer;
import io.papermc.paper.util.Tick;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class listeners implements Listener {
    public static long delay;
    @EventHandler
    public void onSaveEvent(saveDataEvent e){
        LionAPI.getPlugin().getConfig().set("persistent-data.timer", MainTimer.getTimer().getNewSnapshot());
        LionAPI.getPlugin().saveConfig();
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
