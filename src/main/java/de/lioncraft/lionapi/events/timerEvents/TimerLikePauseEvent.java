package de.lioncraft.lionapi.events.timerEvents;

import de.lioncraft.lionapi.timer.TimerLike;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TimerLikePauseEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private TimerLike timer;

    public TimerLikePauseEvent(TimerLike timer) {
        this.timer = timer;
    }

    public TimerLike getTimer() {
        return timer;
    }
    private boolean cancelled;
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
