package de.lioncraft.lionapi.events.timerEvents;

import de.lioncraft.lionapi.timer.Timer;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TimerFinishEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    private Timer timerLike;
    private Component finishMessage;
    private boolean sendFinishMessageToPlayers, sendFinishMessageToOperators;

    public TimerFinishEvent(Timer timerLike, Component finishMessage) {
        this.timerLike = timerLike;
        this.finishMessage = finishMessage;
        sendFinishMessageToOperators = true;
        sendFinishMessageToPlayers = true;
    }

    public Timer getTimerLike() {
        return timerLike;
    }

    public Component getFinishMessage() {
        return finishMessage;
    }

    public void setFinishMessage(Component finishMessage) {
        this.finishMessage = finishMessage;
    }

    public boolean sendFinishMessageToPlayers() {
        return sendFinishMessageToPlayers;
    }

    public void setSendFinishMessageToPlayers(boolean sendFinishMessageToPlayers) {
        this.sendFinishMessageToPlayers = sendFinishMessageToPlayers;
    }

    public boolean sendFinishMessageToOperatorsOnly() {
        return sendFinishMessageToOperators;
    }

    public void setSendFinishMessageToOperatorsOnly(boolean sendFinishMessageToOperators) {
        this.sendFinishMessageToOperators = sendFinishMessageToOperators;
    }
}
