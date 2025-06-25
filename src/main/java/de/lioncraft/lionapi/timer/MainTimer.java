package de.lioncraft.lionapi.timer;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.Interaction.Button;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.MainMenu;
import de.lioncraft.lionapi.messageHandling.DM;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import javax.management.Attribute;
import java.util.HashMap;

public abstract class MainTimer{
    private static Inventory opInv;
    private static TimerLike timer;

    @NotNull
    public static TimerLike getTimer(){
        if (timer == null) {
            TimerSnapshot ts = (TimerSnapshot) LionAPI.getPlugin().getConfig().get("persistent-data.timer");
            if(ts != null){
                timer = ts.createTimer();
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
    public static void changeDirection(){
        boolean b = !(timer instanceof Stopwatch);
        if (timer.isHasEverBeenActive()) {
            int days = timer.getDays();
            int hours = timer.getHours();
            int minutes = timer.getMinutes();
            int seconds = timer.getSeconds();
            timer.pause();
            if (b) {
                timer = new Stopwatch(days, hours, minutes, seconds);
            } else {
                timer = new Timer(days, hours, minutes, seconds);
            }
        }else{
            if (b) {
                timer = new Stopwatch();
            } else {
                timer = new Timer(0, 1, 0, 0);
            }
        }
        timer.addViewers(Bukkit.getOnlinePlayers());
    }
    public static boolean isCountUpwards(){
        return timer instanceof Stopwatch;
    }
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
    public static void update(TimerLike timer){
        if(!opInv.getViewers().isEmpty()){

        }
    }
    public static void reset(){
        getTimer().pause();
        if(isCountUpwards()){
            timer = new Timer(0,0,0,0);
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
    public static void setGlobalSuffix(Component suffix){
        globalSuffix = suffix;
    }
    public static void setGlobalSuffix(Component suffix, int ticks){
        globalSuffix = suffix;
        if(ticks > 0){
            if(globalRemover != null){
                globalRemover.cancel();
            }
           globalRemover = new psRemover(null).runTaskLater(LionAPI.getPlugin(), ticks);
        }

    }
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
            return globalSuffix;
        }
        return suffix.get(p);
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

