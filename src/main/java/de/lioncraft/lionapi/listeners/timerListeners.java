package de.lioncraft.lionapi.listeners;

import com.destroystokyo.paper.event.player.IllegalPacketEvent;
import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.data.ChallengeSettings;
import de.lioncraft.lionapi.events.challenge.challengeEndEvent;
import de.lioncraft.lionapi.events.challenge.challengeEndType;
import de.lioncraft.lionapi.events.timerEvents.MainTimerEvents.MainTimerTickEvent;
import de.lioncraft.lionapi.events.timerEvents.TimerTickEvent;
import de.lioncraft.lionapi.events.timerFinishEvent;
import de.lioncraft.lionapi.events.timerTickEvent;
import de.lioncraft.lionapi.timer.MainTimer;
import io.papermc.paper.event.player.PlayerBedFailEnterEvent;
import io.papermc.paper.event.player.PlayerDeepSleepEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.translation.Translatable;
import net.kyori.adventure.translation.Translator;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.BroadcastMessageEvent;

import java.util.Objects;
import java.util.function.Consumer;

public class timerListeners implements Listener {
    private static OfflinePlayer lastDeath;
    @EventHandler
    public void onTimerEnd(timerFinishEvent e){
        if(Objects.equals(e.getTimer(), MainTimer.getTimer())){
            if(ChallengeSettings.challengeEndsOnTimerExpire){
                if(LionAPI.isChallenge) {
                    Bukkit.getServer().getPluginManager().callEvent(new challengeEndEvent(challengeEndType.timerExpired));
                }
            }
        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        if(e.getEntityType().equals(EntityType.ENDER_DRAGON)){
            if(ChallengeSettings.challengeEndsOnDragonDeath){
                if(LionAPI.isChallenge) {
                    Bukkit.getServer().getPluginManager().callEvent(new challengeEndEvent(challengeEndType.dragonDeath));
                }
            }
        } else if (e.getEntityType().equals(EntityType.PLAYER)) {
            lastDeath = (OfflinePlayer) e.getEntity();
            if(ChallengeSettings.challengeEndsOnPlayerDeath){
                if(LionAPI.isChallenge) {
                    Bukkit.getServer().getPluginManager().callEvent(new challengeEndEvent(challengeEndType.playerDeath));
                }
            }
        }else{

        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        MainTimer.getTimer().addViewers(e.getPlayer());
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        MainTimer.getTimer().getViewer().remove(e.getPlayer());
    }
    @EventHandler
    public void onChallengeEnd(challengeEndEvent e){
        switch (e.getChallengeEndType()){
            case dragonDeath -> {
                for(Player p : Bukkit.getOnlinePlayers()){
                    p.sendMessage(Component.text("_______________", TextColor.color(0, 255, 255)));
                    p.sendMessage(Component.text("Der Ender Dragon ist gestorben!", TextColor.color(0, 255, 25)));
                    p.sendMessage(Component.text("Zeit: ", TextColor.color(20, 200, 20)).append(MainTimer.getTimer().getMessage()));
                    p.sendMessage(Component.text("_______________", TextColor.color(0, 255, 255)));
                }
            }
            case playerDeath -> {
                for(Player p : Bukkit.getOnlinePlayers()){
                    p.sendMessage(Component.text("_______________", TextColor.color(0, 255, 255)));
                    p.sendMessage(Component.text(lastDeath.getName() + " died!", TextColor.color(0, 255, 25)));
                    p.sendMessage(Component.text("Zeit: ", TextColor.color(20, 200, 20)).append(MainTimer.getTimer().getMessage()));
                    p.sendMessage(Component.text("_______________", TextColor.color(0, 255, 255)));
                }
            }
        }
    }
    @EventHandler
    public void onHotbarRecieveEvent(PlayerBedFailEnterEvent e){
        if(e.getMessage() != null){
            if(MainTimer.getTimer().isActive()){
                MainTimer.setPlayerSuffix(e.getPlayer(),Component.text(" (").append(e.getMessage()).append(Component.text(")")), 50);
                e.setMessage(null);
            }
        }
    }
    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent e){
        int percentage = e.getPlayer().getWorld().getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
        int amount_of_players = 0;
        int amount_of_sleeping = 0;
        if (!e.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK))return;
        for(Player p : e.getPlayer().getWorld().getPlayers()){
            amount_of_players++;
            if(p==e.getPlayer()) {
                amount_of_sleeping++;
                continue;
            }
            if(p.isSleeping()&&!p.isSleepingIgnored()) amount_of_sleeping++;
        }
        Component c;
        if((amount_of_sleeping/amount_of_players)*100>=percentage){
            c = Component.translatable("sleep.skipping_night");
        }else c = Component.translatable("sleep.players_sleeping").arguments(TranslationArgument.numeric(amount_of_sleeping), TranslationArgument.numeric(amount_of_players));
        for(Player p : e.getPlayer().getWorld().getPlayers()){
            MainTimer.setPlayerSuffix(p, Component.text(" (").append(c).append(Component.text(")")), 50);
        }
    }
}
