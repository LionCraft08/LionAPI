package de.lioncraft.lionapi.timer;

import de.lioncraft.lionapi.events.timerEvents.MainTimerEvents.MainTimerFinishEvent;
import de.lioncraft.lionapi.events.timerEvents.MainTimerEvents.MainTimerTickEvent;
import de.lioncraft.lionapi.events.timerEvents.TimerFinishEvent;
import de.lioncraft.lionapi.events.timerEvents.TimerTickEvent;
import de.lioncraft.lionapi.events.timerFinishEvent;
import de.lioncraft.lionapi.events.timerTickEvent;
import de.lioncraft.lionapi.messageHandling.DM;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Timer extends TimerLike{
    public Timer(int days, int hours, int minutes, int seconds) {
        super(days, hours, minutes, seconds);
    }
    public Timer(int days, int hours, int minutes, int seconds, int secondsAtStart) {
        super(days, hours, minutes, seconds);
        super.secondsAtStart = secondsAtStart;
    }
    public void tick(){
        TimerTickEvent e = new TimerTickEvent(this);
        if(MainTimer.getTimer().equals(this)){
            MainTimerTickEvent me = new MainTimerTickEvent();
            Bukkit.getPluginManager().callEvent(me);
            e = me;
        }else Bukkit.getPluginManager().callEvent(e);

        if(e.isCancelled()){
            return;
        }
        seconds--;
        if(seconds < 0){
            seconds = 59;
            minutes--;
            if(minutes < 0){
                minutes = 59;
                hours--;
                if(hours < 0){
                    hours = 23;
                    days--;
                    if(days < 0){
                        finish();
                        return;
                    }
                }
            }
        }

        Bukkit.getPluginManager().callEvent(new timerTickEvent(this));
    }
    private void finish(){
        timerFinishEvent e = new timerFinishEvent(this);
        Bukkit.getPluginManager().callEvent(e);
        TimerFinishEvent event;
        if(this == MainTimer.getTimer()){
            event = new MainTimerFinishEvent(this, DM.info("The Timer has expired!"));
        }else event = new TimerFinishEvent(this, DM.info("The Timer has expired!"));
        Bukkit.getPluginManager().callEvent(event);

        super.pause();
        if(event.sendFinishMessageToPlayers()){
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.isOp()){
                    p.sendMessage(event.getFinishMessage());
                }else if (!event.sendFinishMessageToOperatorsOnly()){
                    p.sendMessage(event.getFinishMessage());
                }
            }
        }
        for(OfflinePlayer p : viewers){
            if(p.isOnline()) p.getPlayer().sendActionBar(Component.text("Timer has expired", TextColor.color(255, 128, 0)));
        }
    }

}
