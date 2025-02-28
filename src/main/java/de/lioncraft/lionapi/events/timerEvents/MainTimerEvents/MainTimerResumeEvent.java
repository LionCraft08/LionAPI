package de.lioncraft.lionapi.events.timerEvents.MainTimerEvents;

import de.lioncraft.lionapi.events.timerEvents.TimerLikeResumeEvent;
import de.lioncraft.lionapi.timer.TimerLike;
import net.kyori.adventure.text.Component;

public class MainTimerResumeEvent extends TimerLikeResumeEvent {
    public MainTimerResumeEvent(TimerLike timer, Component message) {
        super(timer, message);
    }
}
