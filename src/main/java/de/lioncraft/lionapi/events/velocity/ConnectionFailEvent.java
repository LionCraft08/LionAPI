package de.lioncraft.lionapi.events.velocity;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ConnectionFailEvent extends Event {
    private final FailReason failReason;
    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public FailReason getFailReason() {
        return failReason;
    }

    public ConnectionFailEvent(FailReason failReason) {
        this.failReason = failReason;
    }

    public enum FailReason {
        TimedOut,
        Rejected,
        UnknownServer,
        NoVelocityResponse
    }
}
