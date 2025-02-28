package de.lioncraft.lionapi.events;

import de.lioncraft.lionapi.timer.Timer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Deprecated(since = "1.2")
public class timerFinishEvent extends Event {
    public Timer getTimer() {
        return timer;
    }

    private final Timer timer;
    public timerFinishEvent(Timer timer){
        this.timer = timer;
    }
    private static final HandlerList handlers = new HandlerList();

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
