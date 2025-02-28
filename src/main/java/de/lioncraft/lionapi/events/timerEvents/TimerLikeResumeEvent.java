package de.lioncraft.lionapi.events.timerEvents;

import de.lioncraft.lionapi.timer.Stopwatch;
import de.lioncraft.lionapi.timer.Timer;
import de.lioncraft.lionapi.timer.TimerLike;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TimerLikeResumeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private TimerLike timer;
    private Component cancelMessage;
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public TimerLikeResumeEvent(TimerLike timer, Component cancelMessage) {
        this.timer = timer;
        this.cancelMessage = cancelMessage;
    }

    public TimerLike getTimer() {
        return timer;
    }

    public boolean countUpwards(){
        return timer instanceof Stopwatch;
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

    public void setTimer(TimerLike timer) {
        this.timer = timer;
    }

    public Component getCancelMessage() {
        return cancelMessage;
    }

    public void setCancelMessage(Component cancelMessage) {
        this.cancelMessage = cancelMessage;
    }
}
