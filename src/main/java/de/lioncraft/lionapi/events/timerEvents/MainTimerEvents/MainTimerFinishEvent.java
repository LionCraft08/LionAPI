package de.lioncraft.lionapi.events.timerEvents.MainTimerEvents;

import de.lioncraft.lionapi.events.timerEvents.TimerFinishEvent;
import de.lioncraft.lionapi.timer.Timer;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;

public class MainTimerFinishEvent extends TimerFinishEvent {
    public MainTimerFinishEvent(Timer timerLike, Component finishMessage) {
        super(timerLike, finishMessage);
    }
}
