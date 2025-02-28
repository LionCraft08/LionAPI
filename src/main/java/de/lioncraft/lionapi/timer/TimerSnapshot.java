package de.lioncraft.lionapi.timer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TimerSnapshot implements ConfigurationSerializable {
    private int days, hours, minutes, seconds, secondsatstart;
    private int color;
    private boolean upwards, hasEverBeenActive;

    public TimerSnapshot(int days, int hours, int minutes, int seconds, int secondsatstart, int color, boolean upwards) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.secondsatstart = secondsatstart;
        this.color = color;
        this.upwards = upwards;
        hasEverBeenActive = true;
    }
    public TimerSnapshot(TimerLike t){
        days = t.getDays();
        hours = t.getHours();
        minutes = t.getMinutes();
        seconds = t.getSeconds();
        secondsatstart = t.getSecondsAtStart();
        upwards = t instanceof Stopwatch;
        color = t.getCurrentColor();
        hasEverBeenActive = t.isHasEverBeenActive();
    }

    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getSecondsatstart() {
        return secondsatstart;
    }

    public int getColor() {
        return color;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("seconds", seconds);
        map.put("minutes", minutes);
        map.put("hours", hours);
        map.put("days", days);
        map.put("secondsatstart", secondsatstart);
        map.put("color", color);
        map.put("up", upwards);
        map.put("has-ever-been-active", hasEverBeenActive);
        return map;
    }
    public TimerSnapshot(Map<String, Object> map){
        seconds = (int) map.get("seconds");
        minutes = (int) map.get("minutes");
        hours = (int) map.get("hours");
        days = (int) map.get("days");
        secondsatstart = (int) map.get("secondsatstart");
        if(map.containsKey("color")){
            color = (int) map.get("color");
        }
        upwards = (boolean) map.get("up");
        if(map.get("has-ever-been-active") != null) hasEverBeenActive = (boolean) map.get("has-ever-been-active");
        else hasEverBeenActive = false;
    }

    public boolean isUpwards() {
        return upwards;
    }
    @Deprecated (forRemoval = true)
    public TimerLike createNewTimer(){
        TimerLike t;
        if(upwards){
            t = new Stopwatch(days, hours, minutes, seconds, secondsatstart);
        }else t = new Timer(days, hours, minutes, seconds, secondsatstart);
        t.setHasEverBeenActive(hasEverBeenActive);
        return t;
    }
    public int getCurrentSeconds(){
        return seconds + minutes*60 + hours*60*60 + days*60*60*24;
    }
    private Component message;
    public Component getMessage(){
        if (message == null) message = TimerLike.getColoredMessage(color, getRawMessage());
        return message;
    }
    private String getRawMessage(){
        StringBuilder message = new StringBuilder();
        if(days > 0){
            message.append(days).append("d ");
        }
        if(hours > 0){
            message.append(hours).append("h ");
        }
        if(minutes > 0){
            message.append(minutes).append("m ");
        }
        message.append(seconds).append("s");
        return message.toString();
    }

    public TimerLike createTimer(){
        if(upwards){
            return new Stopwatch(days, hours, minutes, seconds, secondsatstart);
        }else return new Timer(days, hours, minutes, seconds, secondsatstart);
    }
    public String getStringMessage(){
        return getRawMessage();
    }

    public boolean isHasEverBeenActive() {
        return hasEverBeenActive;
    }
}
