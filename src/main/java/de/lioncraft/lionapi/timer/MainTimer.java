package de.lioncraft.lionapi.timer;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.messageHandling.DM;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public final class MainTimer {
    private MainTimer(){}
    private static Inventory opInv;
    private static TimerLike timer;
    private static boolean useProtocolBridge = false;

    /**
     * Gets the current Main Timer. Can be a {@link Stopwatch} or a {@link Timer}
     * @return the Timer/Stopwatch
     */
    @NotNull
    public static TimerLike getTimer(){
        if (timer == null) {
            TimerSnapshot ts = (TimerSnapshot) LionAPI.getPlugin().getConfig().get("persistent-data.timer");
            if(ts != null){
                timer = ts.createNewTimer();
            }
            if(timer == null){
                timer = new Stopwatch();
            }
            for(Player p : Bukkit.getOnlinePlayers()){
                timer.addViewers(p);
            }
        }
        return timer;
    }

    /**
     * Changes the direction of the Timer. Keeps the current Time (if not the default values) and pauses the Timer.
     */
    public static void changeDirection(){
        boolean b = !(timer instanceof Stopwatch);
        if (timer.isHasEverBeenActive()) {
            int days = timer.getDays();
            int hours = timer.getHours();
            int minutes = timer.getMinutes();
            int seconds = timer.getSeconds();
            timer.pause();
            timer.forceDelete();
            if (b) {
                timer = new Stopwatch(days, hours, minutes, seconds);
            } else {
                if (timer.getCurrentSeconds() < 10){
                    timer = new Timer(0, 1, 0, 0);
                }else timer = new Timer(days, hours, minutes, seconds);
            }

        }else{
            timer.forceDelete();
            if (b) {
                timer = new Stopwatch();
            } else {
                timer = new Timer(0, 1, 0, 0);
            }
        }
        timer.addViewers(Bukkit.getOnlinePlayers());
    }

    /**
     * Whether ProtocolLib is used to automatically add ActionBar Messages to the timer.
     * If this Method returns true, you can feel free to use {@link Player#sendActionBar(Component)}, as the messages are detected and displayed next to the active timer instead.<br>
     * If it returns false, you should consider using {@link MainTimer#setPlayerSuffix(OfflinePlayer, Component, int)} instead, as direct ActionBar messages might be overwritten by the Timer.
     * @return Whether ProtocolLib is used to automatically detect ActionBar Messages.
     */
    public static boolean isUseProtocolBridge() {
        return useProtocolBridge;
    }

    /**
     * Sets whether the protocol lib should be used to improve the timer appearance.
     * ONLY use when you are sure that ProtocolLib is loaded.
     * @param useProtocolBridge Whether the protocol lib should be used.
     * @throws RuntimeException When an error occurred while trying to interact with ProtocolLib.
     */
    public static void setUseProtocolBridge(boolean useProtocolBridge) {
        try {
            if(useProtocolBridge){
                ProtocolLibBridge.register();
            }else ProtocolLibBridge.unregister();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        MainTimer.useProtocolBridge = useProtocolBridge;
    }

    /**Checks if the current Timer counts upwards and therefore is a stopwatch.
     * @return Whether the current timer is a Stopwatch
     */
    public static boolean isCountUpwards(){
        return timer instanceof Stopwatch;
    }

    /**
     * see {@link MainTimer#isCountUpwards()}
     * @return Whether the current timer is a Stopwatch
     */
    public static boolean isStopwatch(){
        return isCountUpwards();
    }
    public static void openUI(HumanEntity player){
        if(opInv == null){
            Initialize();
        }
        if(player.hasPermission("lionapi.timer.main.configure")){
            player.openInventory(opInv);
        }else player.sendMessage(DM.noPermission);
    }

    /**
     * Resets the Timer to its default values (1h if Timer, 0s if Stopwatch)
     */
    public static void reset(){
        getTimer().pause();
        if(isCountUpwards()){
            timer = new Timer(0,1,0,0);
        }else {
            timer = new Stopwatch();
        }
    }
    public static void Initialize(){
        opInv = Bukkit.createInventory(null, 54, Component.text("Timer"));

        for(int i = 19; i <= 22; i++){
            opInv.setItem(i-9, Items.plusButton);
            opInv.setItem(i+9, Items.MinusButton);
        }
        opInv.setItem(19, Items.get("Days", Material.CLOCK, "Current Days"));
        opInv.setItem(20, Items.get("Hours", Material.CLOCK, "Current Hours"));
        opInv.setItem(21, Items.get("Minutes", Material.CLOCK, "Current Minutes"));
        opInv.setItem(22, Items.get("Seconds", Material.CLOCK, "Current Seconds"));
    }

    /**
     * Sets a message that is displayed on the right side of the timer for everyone.
     * @param suffix The message to display
     */
    public static void setGlobalSuffix(Component suffix){
        globalSuffix = suffix;
    }

    /**
     * Sets a message that is displayed on the right side of the timer for everyone.<br>
     * Deletes the Message after the given amount of ticks.
     * @param suffix The message to display
     * @param ticks The amount of ticks to delete the message after
     */
    public static void setGlobalSuffix(Component suffix, int ticks){
        globalSuffix = suffix;
        if(ticks > 0){
            if(globalRemover != null){
                globalRemover.cancel();
            }
           globalRemover = new psRemover(null).runTaskLater(LionAPI.getPlugin(), ticks);
        }

    }
    /**
     * Sets a message that is displayed on the right side of the timer for the Player.<br>
     * Deletes the Message after the given amount of ticks.
     * @param suffix The message to display
     * @param ticks The amount of ticks to delete the message after
     */
    public static void setPlayerSuffix(OfflinePlayer player, Component suffix, int ticks){
        MainTimer.suffix.put(player, suffix);
        if(ticks > 0) {
            if(remover.containsKey(player)){
                remover.get(player).cancel();
            }
            remover.put(player, new psRemover(player).runTaskLater(LionAPI.getPlugin(), ticks));
        }
    }
    protected static HashMap<OfflinePlayer, Component> suffix = new HashMap<>();
    private static HashMap<OfflinePlayer, BukkitTask> remover = new HashMap<>();
    private static Component globalSuffix;
    private static BukkitTask globalRemover;
    public static Component getSuffix(OfflinePlayer p){
        if(suffix.get(p) == null){
            return getFullSuffix(globalSuffix);
        }
        return getFullSuffix(suffix.get(p));
    }
    private static Component getFullSuffix(Component singleSuffix){
        if (singleSuffix != null)
            return Component.text(" | ", Style.style(TextColor.color(255, 255, 255))
                    .decoration(TextDecoration.BOLD, false))
                .append(singleSuffix);
        return null;
    }
    static class psRemover extends BukkitRunnable {
        OfflinePlayer player;
        private psRemover(OfflinePlayer player){
            this.player = player;
        }
        @Override
        public void run() {
            if(player == null){
                globalSuffix = null;
                globalRemover = null;
            }else {
                MainTimer.suffix.remove(player);
                remover.remove(player);
            }
            this.cancel();
        }
    }

    public static void Initialize(TimerLike t){
        timer = t;
    }
}

