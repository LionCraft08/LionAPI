package de.lioncraft.lionapi.listeners;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.lionclient.DisplayManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LionGUIElementsListeners implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Bukkit.getServer().getScheduler().runTaskLater(LionAPI.getPlugin(), () -> {
            DisplayManager.sendDisplayCheck(e.getPlayer());
        }, 10);
    }
}
