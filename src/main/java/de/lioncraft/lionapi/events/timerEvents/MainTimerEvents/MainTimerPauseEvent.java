package de.lioncraft.lionapi.events.timerEvents.MainTimerEvents;

import de.lioncraft.lionapi.events.timerEvents.TimerLikePauseEvent;
import de.lioncraft.lionapi.timer.TimerLike;

public class MainTimerPauseEvent extends TimerLikePauseEvent {
    public MainTimerPauseEvent(TimerLike timer) {
        super(timer);
    }
}
