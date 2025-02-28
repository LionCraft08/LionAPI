package de.lioncraft.lionapi.events.timerEvents;

import de.lioncraft.lionapi.timer.Stopwatch;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class StopwatchTickEvent extends TimerLikeTickEvent {
    private static final HandlerList handlers = new HandlerList();
    public Stopwatch getStopwatch() {
        return stopwatch;
    }
    private Stopwatch stopwatch;
    public StopwatchTickEvent(Stopwatch sw){
        super(sw);
        stopwatch = sw;
    }
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
