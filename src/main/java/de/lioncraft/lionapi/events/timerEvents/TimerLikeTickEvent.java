package de.lioncraft.lionapi.events.timerEvents;

import de.lioncraft.lionapi.timer.TimerLike;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**Called when every Type of Timer, no matter if upwards or downwards, gets Ticked.
 *
 */
public class TimerLikeTickEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    private TimerLike timerLike;

    public TimerLikeTickEvent(TimerLike timerLike) {
        this.timerLike = timerLike;
    }

    public TimerLike getTimerLike() {
        return timerLike;
    }

    private boolean cancelled;
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
