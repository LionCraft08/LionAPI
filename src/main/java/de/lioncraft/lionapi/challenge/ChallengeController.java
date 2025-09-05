package de.lioncraft.lionapi.challenge;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.data.ChallengeSettings;
import de.lioncraft.lionapi.events.challenge.challengeEndEvent;
import de.lioncraft.lionapi.events.challenge.challengeEndType;
import de.lioncraft.lionapi.timer.MainTimer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class ChallengeController implements ConfigurationSerializable {
    private static Class<? extends ChallengeController> clazz;

    public static void setClazz(Class<? extends ChallengeController> clazz) {
        ChallengeController.clazz = clazz;
    }

    private static ChallengeController instance;

    public static void setInstance(ChallengeController instance) {
        ChallengeController.instance = instance;
    }

    public static ChallengeController getInstance() {
        if (instance == null) if (clazz != null){
            try {
                instance = clazz.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }else{
            instance = new SimpleSpeedrunChallenge();
        }
        return instance;
    }

    protected abstract void onStart();
    public abstract boolean canBePaused();
    protected abstract void onPause();
    protected abstract void onFinish(ChallengeEndData data);
    protected abstract void onResume();
    protected abstract void onLoad();
    protected abstract void onJoin(Player p);
    protected abstract Inventory getConfigInventory(Player user);

    public void sendFinish(ChallengeEndData data){
        isActive = false;
        if (settings.isUseTimer()) MainTimer.getTimer().pause();
        onFinish(data);
    }

    public void sendJoin(Player p){
        if (!isActive &&(settings.isUseTimer())&&!MainTimer.getTimer().isHasEverBeenActive()){
            onLoad();
        }
        onJoin(p);
    }

    public void sendPause(){
        if (!canBePaused()) return;
        isActive = false;
        if (settings.isUseTimer()) MainTimer.getTimer().pause();
        onPause();
    }
    public void sendStart(){
        isActive = true;
        if (settings.isUseTimer()) MainTimer.getTimer().start();
        onStart();
    }
    public void sendResume(){
        isActive = true;
        if (settings.isUseTimer()) MainTimer.getTimer().start();
        onResume();
    }

    private ChallengeSettings settings;
    private boolean isActive;

    public ChallengeController(boolean useTimer, boolean timerCountsUpwards){
        settings = new ChallengeSettings();
        settings.setChallenge(true);
        settings.setUseTimer(useTimer);
        if (useTimer){
            if (timerCountsUpwards &&!MainTimer.isCountUpwards()) MainTimer.changeDirection();
        }
    }
    public ChallengeController(Map<String, Object> args){
        settings = (ChallengeSettings) args.get("settings");
    }

    public ChallengeSettings getSettings() {
        return settings;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of("settings", settings);
    }

    public boolean isActive() {
        return isActive;
    }
}
