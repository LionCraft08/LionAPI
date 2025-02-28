package de.lioncraft.lionapi.timer;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.events.challenge.challengePauseEvent;
import de.lioncraft.lionapi.events.challenge.challengeResumeEvent;
import de.lioncraft.lionapi.events.stopwatchTickEvent;
import de.lioncraft.lionapi.events.timerEvents.MainTimerEvents.MainStopwatchTickEvent;
import de.lioncraft.lionapi.events.timerEvents.StopwatchTickEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Stopwatch extends TimerLike{
    public void tick(){
        boolean cancelled;
        if(MainTimer.getTimer().equals(this)){
            MainStopwatchTickEvent e = new MainStopwatchTickEvent();
            Bukkit.getPluginManager().callEvent(e);
            cancelled = e.isCancelled();
        }else {
            StopwatchTickEvent e = new StopwatchTickEvent(this);
            Bukkit.getPluginManager().callEvent(e);
            cancelled = e.isCancelled();
        }
        if(cancelled){
            return;
        }
        seconds++;
        if(seconds>=60){
            seconds = 0;
            minutes++;
            if(minutes>=60){
                minutes=0;
                hours++;
                if(hours>=24){
                    hours=0;
                    days++;
                }
            }
        }
        Bukkit.getPluginManager().callEvent(new stopwatchTickEvent(this));
    }

    public Stopwatch(int days, int hours, int minutes, int seconds) {
        super(days, hours, minutes, seconds);
    }
    public Stopwatch(int days, int hours, int minutes, int seconds, int secondsAtStart) {
        super(days, hours, minutes, seconds);
        super.secondsAtStart = secondsAtStart;
    }

    public Stopwatch() {
        super(0, 0, 0, 0);
    }
}
