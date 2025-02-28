package de.lioncraft.lionapi.events.timerEvents;

import de.lioncraft.lionapi.timer.Timer;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TimerTickEvent extends TimerLikeTickEvent {

    public @NotNull Timer getTimer() {
        return timer;
    }

    private Timer timer;
    private boolean cancelled;
    public TimerTickEvent(@NotNull Timer timer){
        super(timer);
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
