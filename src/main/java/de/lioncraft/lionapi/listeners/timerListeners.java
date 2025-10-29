package de.lioncraft.lionapi.listeners;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.timer.MainTimer;
import io.papermc.paper.event.player.PlayerBedFailEnterEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.GenericGameEvent;

public class timerListeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        MainTimer.getTimer().addViewers(e.getPlayer());
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        MainTimer.getTimer().getViewer().remove(e.getPlayer());
    }

    @EventHandler
    public void onHotbarReceiveEvent(PlayerBedFailEnterEvent e){
        if(e.getMessage() != null){
            if(MainTimer.getTimer().isActive()){
                if (e.getMessage() instanceof TextComponent c){
                    if (c.content().isBlank()) return;
                }
                MainTimer.setPlayerSuffix(e.getPlayer(), e.getMessage(), 50);
                e.setMessage(null);
            }
        }
    }
    @EventHandler
    public void onLeaveWhileSleeping(PlayerQuitEvent e){
        World w = e.getPlayer().getWorld();
        LionAPI.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(LionAPI.getPlugin(), () -> {
           calculatePlayers(null, w);
        }, 1);
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent e){
        if (!e.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)) return;
        calculatePlayers(e.getPlayer(), e.getPlayer().getWorld());
    }

    private static void calculatePlayers(Player p, World w){
        int percentage = w.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
        int amount_of_players = 0;
        int amount_of_sleeping = 0;
        for(Player player : w.getPlayers()){
            if (!player.isSleepingIgnored()) amount_of_players++;
            if(player == p) {
                amount_of_sleeping++;
                continue;
            }
            if(player.isSleeping()&&!player.isSleepingIgnored()) amount_of_sleeping++;
        }
        if (amount_of_sleeping <= 0) return;
        if (amount_of_players <= 0) return;
        Component c;
        if((amount_of_sleeping/amount_of_players)*100>=percentage){
            c = Component.translatable("sleep.skipping_night");
        }else c = Component.translatable("sleep.players_sleeping").arguments(TranslationArgument.numeric(amount_of_sleeping), TranslationArgument.numeric(amount_of_players));
        for(Player player : w.getPlayers()){
            MainTimer.setPlayerSuffix(player,c, 50);
        }
    }

    @EventHandler
    public void onLimit(BlockCanBuildEvent e){
        if (e.isBuildable()) return;
        if (e.getBlock().getLocation().getBlockY() == e.getBlock().getLocation().getWorld().getMaxHeight()){
            setHotbarMessage(Component.translatable("build.tooHigh", TextColor.color(170, 0, 0)).arguments(Component.text(e.getBlock().getWorld().getMaxHeight())), e.getPlayer());
        }
    }

    @EventHandler
    public void onChestFail(GenericGameEvent e){
        if (e.getEvent() == GameEvent.JUKEBOX_PLAY){
            if (e.getLocation().getBlock() instanceof Jukebox jb){
                for (Player p : e.getLocation().getNearbyPlayers(e.getRadius())){
                    MainTimer.setPlayerSuffix(p,
                            Component.translatable("record.nowPlaying").arguments(jb.getRecord().lore().get(0)), 50);
                }
            }

        }
    }
    public static void setHotbarMessage(Component message, Player p){
        MainTimer.setPlayerSuffix(p, message, 60);
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDisk(EntityMountEvent e){
        if (!e.isCancelled()){
            if (e.getEntity() instanceof Player p){
                setHotbarMessage(Component.translatable("mount.onboard").arguments(Component.keybind("key.sneak")), p);
            }
        }

    }
}
