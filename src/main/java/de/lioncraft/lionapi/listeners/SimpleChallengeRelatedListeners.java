package de.lioncraft.lionapi.listeners;

import de.lioncraft.lionapi.challenge.ChallengeController;
import de.lioncraft.lionapi.challenge.ChallengeEndData;
import de.lioncraft.lionapi.events.timerEvents.MainTimerEvents.MainTimerFinishEvent;
import de.lioncraft.lionapi.events.timerEvents.MainTimerEvents.MainTimerPauseEvent;
import de.lioncraft.lionapi.events.timerEvents.MainTimerEvents.MainTimerResumeEvent;
import de.lioncraft.lionapi.timer.MainTimer;
import de.lioncraft.lionapi.timer.TimerConfig;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SimpleChallengeRelatedListeners implements Listener {
    public static String PlayerThatDied;
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        if (!ChallengeController.getInstance().getSettings().isChallenge()) return;
        if (!ChallengeController.getInstance().isActive()) return;
        if (TimerConfig.getTimerEndsOnPlayerDeath()){
            ChallengeController.getInstance().sendFinish(new ChallengeEndData(false).setArgs(Map.of("type", "playerDeath", "player", Objects.requireNonNullElse(PlayerThatDied, e.getPlayer().getName()))));
            PlayerThatDied = null;
        }
    }
    @EventHandler
    public void onPlayerDeath(EntityDeathEvent e){
        if (!ChallengeController.getInstance().getSettings().isChallenge()) return;
        if (!ChallengeController.getInstance().isActive()) return;
        if (TimerConfig.getTimerEndsOnDragonDeath()){
            if (e.getEntityType().equals(EntityType.ENDER_DRAGON)) ChallengeController.getInstance().sendFinish(new ChallengeEndData(true).setArgs(Map.of("type", "dragonDeath")));
        }
    }

    @EventHandler
    public void onPause(MainTimerPauseEvent e){
        if (!ChallengeController.getInstance().getSettings().isChallenge()) return;
        if (!ChallengeController.getInstance().isActive()) return;
        if (ChallengeController.getInstance().canBePaused()){
            ChallengeController.getInstance().sendPause();
        }
    }
    @EventHandler
    public void onPause(MainTimerResumeEvent e){
        if (!ChallengeController.getInstance().getSettings().isChallenge()) return;
        if (ChallengeController.getInstance().isActive()) return;
        if(MainTimer.getTimer().isHasEverBeenActive()){
            ChallengeController.getInstance().sendResume();
        }else ChallengeController.getInstance().sendStart();
    }

    @EventHandler
    public void onEnd(MainTimerFinishEvent e){
        if (!ChallengeController.getInstance().getSettings().isChallenge()) return;
        if (!ChallengeController.getInstance().isActive()) return;
        if (ChallengeController.getInstance().getSettings().isChallengeEndsOnTimerExpire()){
            ChallengeController.getInstance().sendFinish(new ChallengeEndData(false).setArgs(Map.of("type", "timerExpired")));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if (!ChallengeController.getInstance().getSettings().isChallenge()) return;
        ChallengeController.getInstance().sendJoin(e.getPlayer());
    }

}
