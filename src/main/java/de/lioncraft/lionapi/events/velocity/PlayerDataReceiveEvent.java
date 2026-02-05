package de.lioncraft.lionapi.events.velocity;

import de.lioncraft.lionapi.velocity.data.PlayerConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerDataReceiveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    private PlayerConfiguration playerData;
    public PlayerDataReceiveEvent(@NotNull PlayerConfiguration playerData) {
        this.playerData = playerData;
    }

    public @NotNull PlayerConfiguration getPlayerData() {
        return playerData;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerData.uuid);
    }
}
