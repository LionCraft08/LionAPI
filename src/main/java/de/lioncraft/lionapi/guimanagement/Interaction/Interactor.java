package de.lioncraft.lionapi.guimanagement.Interaction;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.functions.SimpleFunction;
import de.lioncraft.lionapi.listeners.invClickListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class Interactor {
    private static final HashMap<OfflinePlayer, Interactor> map = new HashMap<>();
    private static final HashMap<ItemStack, SimpleFunction> map2 = new HashMap<>();
    public static boolean sendInteraction(OfflinePlayer p, ItemStack is){
        Interactor i = map.get(p);
        if(i == null){
            i = new Interactor(30, p, map2.get(is));
            map.put(p, i);
        }
        return i.newTick();
    }
    public static boolean hasActiveInteraction(OfflinePlayer p){
        if(map.containsKey(p)) return map.get(p) != null;
        return false;
    }
    public static void registerInteractor(ItemStack is, SimpleFunction onFinish){
        if(onFinish == null){
            Bukkit.getConsoleSender().sendMessage(Component.text("Error 4"));
        }
        map2.put(is, onFinish);
        if(map2.get(is) == null){
            Bukkit.getConsoleSender().sendMessage(Component.text("Error 2"));
        }
        invClickListener.addInteractionItem(is);
    }
    private final OfflinePlayer p;
    private int stage;
    private final int max;
    private int lastTick;
    private BukkitTask reset;
    private final SimpleFunction function;
    private Interactor(int max, OfflinePlayer p, SimpleFunction function){
        this.max = max;
        this.p = p;
        stage = 0;
        lastTick = 0;
        if(function == null){
            Bukkit.getConsoleSender().sendMessage(Component.text("Error 1"));
        }
        this.function = function;
        reset = new ResetTask(this).runTaskTimer(LionAPI.getPlugin(), 0, 2);
    }

    public OfflinePlayer getP() {
        return p;
    }

    public int getStage() {
        return stage;
    }

    public int getMax() {
        return max;
    }
    public boolean newTick(){
        lastTick = stage;
        if(stage >= max){
            onFinish();
            return true;
        }
        return false;
    }
    public void countUp(){
        stage++;
        if(stage >= max){
            stage = max;
        }
        sendMessage();
    }
    public void onFinish(){
        p.getPlayer().playSound(p.getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
        map.remove(p);
        reset.cancel();
        reset = null;
        function.run(p.getPlayer());
    }
    public void countDown(){
        stage-=2;
        if(stage <= 0){
            sendMessage();
            map.remove(p);
            reset.cancel();
            reset = null;
        }else{
            if(p.isOnline()){
                sendMessage();
            }
        }
    }
    private void sendMessage(){
        Component c = Component.text("[", TextColor.color(128, 128, 128));
        for(int i = 0; i < max ; i++){
            if(i < stage){
                if(i < 51){
                    c = c.append(Component.text("|", TextColor.color(255, 0, 255-(i*5))));
                }else{
                    c = c.append(Component.text("|", TextColor.color(255, 0, 0)));
                }

            }else{
                c = c.append(Component.text("|", TextColor.color(70, 70, 70)));
            }

        }
        c = c.append(Component.text("]", TextColor.color(128, 128, 128)));
        p.getPlayer().sendActionBar(c);
    }
    public void playSound(){
        p.getPlayer().playSound(p.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f + ((float) stage / (float)max));
    }

    public int getLastTick() {
        return lastTick;
    }
    public void setLastTick(int lastTick){
        this.lastTick = lastTick;
    }
}
