package de.lioncraft.lionapi.events.timerEvents.MainTimerEvents;

import de.lioncraft.lionapi.events.timerEvents.StopwatchTickEvent;
import de.lioncraft.lionapi.timer.MainTimer;
import de.lioncraft.lionapi.timer.Stopwatch;

public class MainStopwatchTickEvent extends StopwatchTickEvent {
    public MainStopwatchTickEvent() {
        super((Stopwatch) MainTimer.getTimer());
    }
}
