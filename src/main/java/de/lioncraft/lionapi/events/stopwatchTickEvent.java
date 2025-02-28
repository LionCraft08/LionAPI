package de.lioncraft.lionapi.events;

import de.lioncraft.lionapi.timer.Stopwatch;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Deprecated(since = "1.1")
public class stopwatchTickEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public Stopwatch getStopwatch() {
        return stopwatch;
    }

    Stopwatch stopwatch;
    public stopwatchTickEvent(Stopwatch sw){
        stopwatch = sw;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
