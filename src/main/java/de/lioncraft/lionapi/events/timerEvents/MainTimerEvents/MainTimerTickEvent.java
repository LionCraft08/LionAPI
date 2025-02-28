package de.lioncraft.lionapi.events.timerEvents.MainTimerEvents;

import de.lioncraft.lionapi.events.timerEvents.TimerTickEvent;
import de.lioncraft.lionapi.timer.MainTimer;
import de.lioncraft.lionapi.timer.Timer;
import org.jetbrains.annotations.NotNull;

public class MainTimerTickEvent extends TimerTickEvent {
    public MainTimerTickEvent() {
        super((Timer) MainTimer.getTimer());
    }
}
