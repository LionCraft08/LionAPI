package de.lioncraft.lionapi.timer;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.events.timerEvents.MainTimerEvents.MainTimerFinishEvent;
import de.lioncraft.lionapi.events.timerEvents.MainTimerEvents.MainTimerTickEvent;
import de.lioncraft.lionapi.events.timerEvents.TimerFinishEvent;
import de.lioncraft.lionapi.events.timerEvents.TimerTickEvent;
import de.lioncraft.lionapi.events.timerFinishEvent;
import de.lioncraft.lionapi.events.timerTickEvent;
import de.lioncraft.lionapi.messageHandling.DM;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
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

    @Override
    public Component start(){
        if (seconds<=0 && minutes<=0 && hours <=0 && days <= 0) return Component.text("You need to set a time to start the timer!");
        else return super.start();
    }

    private void finish(){
        days = 0;
        hours = 0;
        minutes = 0;
        seconds = 0;
        timerFinishEvent e = new timerFinishEvent(this);
        Bukkit.getPluginManager().callEvent(e);
        TimerFinishEvent event;
        if(this == MainTimer.getTimer()){
            event = new MainTimerFinishEvent(this, LionAPI.lm().msg("features.timer.expired"));
        }else event = new TimerFinishEvent(this, LionAPI.lm().msg("features.timer.expired"));
        Bukkit.getPluginManager().callEvent(event);

        super.pause();
        if(event.sendFinishMessageToPlayers()){
            for(OfflinePlayer p : getViewer()){
                if (!p.isOnline()) return;
                if(p.isOp()){
                    LionChat.sendMessageOnChannel("timer", event.getFinishMessage(), p.getPlayer());
                }else if (!event.sendFinishMessageToOperatorsOnly()){
                    LionChat.sendMessageOnChannel("timer", event.getFinishMessage(), p.getPlayer());
                }
            }
        }
        for(OfflinePlayer p : viewers){
            if(p.isOnline()) p.getPlayer().sendActionBar(LionAPI.lm().msg("features.timer.expired"));
        }
    }

}
