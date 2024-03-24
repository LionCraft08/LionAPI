package de.lioncraft.lionapi.listeners;

import de.lioncraft.lionapi.events.saveDataEvent;
import de.lioncraft.lionapi.messageHandling.defaultMessages;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

public class listeners implements Listener {
    public static long delay;
    @EventHandler
    public void onSaveEvent(saveDataEvent e){
        Bukkit.getConsoleSender().sendMessage(defaultMessages.messagePrefix.append(Component.text("Saved all persistence-required Data")));
    }
    @EventHandler
    public void onWorldSave(WorldSaveEvent e){
        if (System.currentTimeMillis() - delay > 10000) {
            delay = System.currentTimeMillis();
            Bukkit.getPluginManager().callEvent(new saveDataEvent());
        }
    }
}
