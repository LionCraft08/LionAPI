package de.lioncraft.lionapi.timer;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.events.challenge.challengePauseEvent;
import de.lioncraft.lionapi.events.challenge.challengeResumeEvent;
import de.lioncraft.lionapi.events.timerEvents.MainTimerEvents.MainTimerPauseEvent;
import de.lioncraft.lionapi.events.timerEvents.MainTimerEvents.MainTimerResumeEvent;
import de.lioncraft.lionapi.events.timerEvents.TimerLikePauseEvent;
import de.lioncraft.lionapi.events.timerEvents.TimerLikeResumeEvent;
import de.lioncraft.lionapi.guimanagement.Interaction.Interactor;
import de.lioncraft.lionapi.messageHandling.DM;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;

public abstract class TimerLike {
    protected int days, hours, minutes, seconds, secondsAtStart;
    protected boolean hasEverBeenActive = false;
    protected BukkitTask tick, colorTick;
    protected List<OfflinePlayer> viewers;
    public abstract void tick();
    public TimerLike(int days, int hours, int minutes, int seconds) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        updateSecondsAtStart();
        viewers = new ArrayList<>();
        checkTimes();
    }
    public TimerLike(int days, int hours, int minutes, int seconds, Collection<? extends Player> players) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        viewers = new ArrayList<>();
        viewers.addAll(players);
        updateSecondsAtStart();
        checkTimes();
    }

    public int getCurrentSeconds(){
        return seconds + minutes*60 + hours*60*60 + days*60*60*24;
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
    public int getSecondsAtStart() {
        return secondsAtStart;
    }
    public void setDays(int days) {
        this.days = days;
        checkTimes();
    }
    public void setHours(int hours) {
        this.hours = hours;
        checkTimes();
    }
    public void setMinutes(int minutes) {
        this.minutes = minutes;
        checkTimes();
    }
    public void setSeconds(int seconds) {
        this.seconds = seconds;
        checkTimes();
    }

    public void setHasEverBeenActive(boolean hasEverBeenActive) {
        this.hasEverBeenActive = hasEverBeenActive;
    }

    public void updateSecondsAtStart(){
        secondsAtStart = getCurrentSeconds();
    }


    private int currentColor;
    private Component message;

    int getCurrentColor(){
        return currentColor;
    }

    public boolean isHasEverBeenActive() {
        return hasEverBeenActive;
    }

    public Component getMessage(){
        if (message == null) updateMessage(0);
        return message;
    }

    private void updateMessage(int color){
        message = getColoredMessage(color, getRawMessage());
    }
    static Component getColoredMessage(int color, String rawMessage){
        Component c = Component.text("");
        for(char ch : rawMessage.toCharArray()){
            if (color>= map.size()) color=0;
            c = c.append(Component.text(ch, map.get(color)));
            color++;
        }
        return c.style(Style.style().decorate(TextDecoration.BOLD).build());
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
    protected void checkTimes(){
        if(seconds >= 60){
            int i = seconds / 60;
            seconds -= i*60;
            minutes += i;
        }
        if(minutes >= 60){
            int i = minutes / 60;
            minutes -= i*60;
            hours += i;
        }
        if(hours >= 24){
            int i = hours / 24;
            hours -= i*24;
            days += i;
        }
    }

    private boolean isStarting;
    public Component start(){
        if (isStarting) return Component.text("Der Timer ist bereits aktiv!");
        isStarting = true;
        TimerLikeResumeEvent e;
        if(MainTimer.getTimer() == this){
            e = new MainTimerResumeEvent(this, Component.text("The Timer couldn't be started!"));
            Bukkit.getPluginManager().callEvent(e);
        }else{
            e = new TimerLikeResumeEvent(this, Component.text("The Timer couldn't be started!"));
            Bukkit.getPluginManager().callEvent(e);
        }
        isStarting = false;
        if(e.isCancelled()){
            return e.getCancelMessage();
        }

        hasEverBeenActive = true;
        if(tick == null){
            tick = new TimerTick(this).runTaskTimerAsynchronously(LionAPI.getPlugin(), 0, 20);
            if(colorTick != null){
                colorTick.cancel();
            }
            colorTick = new colorTick(this).runTaskTimer(LionAPI.getPlugin(), 0, 2);
        } else return Component.text("The Timer is already active!");
        Bukkit.getPluginManager().callEvent(new challengeResumeEvent());
        return Component.text("Timer Resumed");
    }
    public List<OfflinePlayer> getViewer() {
        return viewers;
    }
    public boolean isActive(){
        return tick != null;
    }
    public void addViewers(Player... p){
        if (p!=null) {
            viewers.addAll(Arrays.stream(p).toList());
        }
    }

    public void pause(){
        TimerLikePauseEvent e;
        if(equals(MainTimer.getTimer())){
            e = new MainTimerPauseEvent(this);
        }else e = new TimerLikePauseEvent(this);
        Bukkit.getPluginManager().callEvent(e);
        if(e.isCancelled()){
            return;
        }
        if(tick != null){
            tick.cancel();
        }
        if(colorTick != null){
            colorTick.cancel();
        }
        tick = null;
        colorTick = null;
        for(OfflinePlayer p : viewers){
            if(p.isOnline()) p.getPlayer().sendActionBar(Component.text("Timer Paused!", TextColor.color(255, 128, 0)));
        }
        Bukkit.getPluginManager().callEvent(new challengePauseEvent());
    }
    /**
     * r+, b-. g+, r-, b+, g-
     */
    public void colorTick(){
        currentColor++;
        if(currentColor >= map.size()) currentColor=0;
        updateMessage(currentColor);
        Component c = getMessage();
        for(OfflinePlayer p : viewers){
            if(p.isOnline()){
                if(!Interactor.hasActiveInteraction(p)){
                    Component suffix = MainTimer.getSuffix(p);
                    if(suffix != null){
                        p.getPlayer().sendActionBar(c.append(suffix));
                    }else p.getPlayer().sendActionBar(c);
                }
            }
        }

    }



    private static List<TextColor> map = restoreDefaultColors();
    private static List<TextColor> restoreDefaultColors(){
        map = new ArrayList<>();
        for(int i = 0;i<100;i++) addColorValue(TextColor.color(255, 128, 0));
        for(int i = 1;i<=10;i++) addColorValue(TextColor.color(255, 128-i*12, 0));
        for(int i = 0;i<20;i++) addColorValue(TextColor.color(255, 0, 0));
        for(int i = 0;i<=20;i++) addColorValue(TextColor.color(255, i*6, 0));
        for(int i = 0;i<20;i++) addColorValue(TextColor.color(255, 128, 0));
        for(int i = 0;i<=10;i++) addColorValue(TextColor.color(255, 128-i*12, 0));
        addColorValue(TextColor.color(255, 0, 0));
        for(int i = 0;i<=25;i++) addColorValue(TextColor.color(255, 0, i*10));
        addColorValue(TextColor.color(255, 0, 255));
        for(int i = 0;i<=12;i++) addColorValue(TextColor.color(255-i*10, 0, 255));
        addColorValue(TextColor.color(128, 0, 255));
        for(int i = 0;i<50;i++) addColorValue(TextColor.color(128, 0, 255));
        for(int i = 0;i<=12;i++) addColorValue(TextColor.color(128+i*10, 0, 255-i*20));
        addColorValue(TextColor.color(255, 0, 0));
        for(int i = 0;i<=10;i++) addColorValue(TextColor.color(255, i*12, 0));
        return map;
    }

    private static YamlConfiguration timerConfig;

    public static void loadTimerData(){
        File f = LionAPI.getPlugin().getDataPath().resolve("timer-color-presets.yml").toFile();
        timerConfig = YamlConfiguration.loadConfiguration(f);
        String colorCode = timerConfig.getString("selected-preset");
        if (colorCode == null || colorCode.isBlank()){
            LionAPI.getPlugin().getLogger().warning("You didn't select a valid color gradient maker, restoring default.");
            restoreDefaultColors();
        }else{
            initColorSet(timerConfig.getStringList(colorCode));
        }

    }

    private static void initColorSet(List<String> colorSet){
        List<TextColor> colors = new ArrayList<>();
        TextColor lastColor = null;
        for (String s : colorSet){
            if (s.contains(":")){
                String[] temp = s.split(":");
                TextColor targetedColor = TextColor.fromCSSHexString(temp[1]);
                if (targetedColor != null){
                    if (lastColor == null) lastColor = targetedColor;
                    try {
                        colors.addAll(generateGradient(lastColor, targetedColor, Integer.parseInt(temp[0])));
                        lastColor = colors.get(colors.size()-1);
                        continue;
                    }catch (NumberFormatException e){
                        LionAPI.getPlugin().getLogger().warning(temp[0] + " is not a valid Integer value!");
                    }
                }
            }
            LionAPI.getPlugin().getLogger().warning("Found a malformed gradient String, skipping this value: "+s);
        }
        if (colors.isEmpty()){
            LionAPI.getPlugin().getLogger().warning("The selected color set for the timer does not contain valid gradient strings, resetting to default.");
            restoreDefaultColors();
        }else map = colors;
    }
    public static List<TextColor> generateGradient(TextColor startColor, TextColor endColor, int steps) {
        List<TextColor> gradient = new ArrayList<>();

        if (steps < 2) {
            // A gradient needs at least a start and an end color.
            if (steps == 1) {
                gradient.add(endColor);
            }
            return gradient;
        }

        // Calculate the step size for each color component (Red, Green, Blue)
        // Note: steps - 1 is used because the 'steps' include both the start and end colors.
        // We only need to calculate 'steps - 1' intermediate steps/intervals.
        double rStep = (endColor.red() - startColor.red()) / (double) (steps - 1);
        double gStep = (endColor.green() - startColor.green()) / (double) (steps - 1);
        double bStep = (endColor.blue() - startColor.blue()) / (double) (steps - 1);

        for (int i = 0; i < steps; i++) {
            // Calculate the current RGB values by adding 'i' steps to the start color's components
            int r = (int) Math.round(startColor.red() + (rStep * i));
            int g = (int) Math.round(startColor.green() + (gStep * i));
            int b = (int) Math.round(startColor.blue() + (bStep * i));

            // Ensure the calculated values stay within the valid 0-255 range
            // (Math.round and the nature of the division should generally keep it in range,
            // but clamping is a good practice for robustness).
            r = Math.max(0, Math.min(255, r));
            g = Math.max(0, Math.min(255, g));
            b = Math.max(0, Math.min(255, b));

            // Create and add the new interpolated color
            gradient.add(TextColor.color(r, g, b));
        }

        return gradient;
    }

    private static void addColorValue(TextColor color){
        map.add(color);
    }
    public TimerSnapshot getNewSnapshot(){
        return new TimerSnapshot(this);
    }

    public void addViewers(Collection<? extends Player> players) {
        viewers.addAll(players);
    }
}
