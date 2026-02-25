package de.lioncraft.lionapi.data;

import de.lioncraft.lionapi.timer.MainTimer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ChallengeSettings implements ConfigurationSerializable {
    private boolean challengeEndsOnDragonDeath;
    private boolean challengeEndsOnPlayerDeath;
    private boolean challengeEndsOnTimerExpire, isChallenge, useTimer, timerCountsUpwards, hasBeenLoaded;


    public ChallengeSettings(){
        challengeEndsOnDragonDeath = true;
        challengeEndsOnTimerExpire = false;
        challengeEndsOnPlayerDeath = true;
        isChallenge = true;
        useTimer = true;
        hasBeenLoaded = false;
    }

    @Deprecated(since = "1.9.10", forRemoval = true)
    public boolean isChallengeEndsOnDragonDeath() {
        return challengeEndsOnDragonDeath;
    }

    @Deprecated(since = "1.9.10", forRemoval = true)
    public void setChallengeEndsOnDragonDeath(boolean challengeEndsOnDragonDeath) {
        this.challengeEndsOnDragonDeath = challengeEndsOnDragonDeath;
    }

    @Deprecated(since = "1.9.10", forRemoval = true)
    public boolean isChallengeEndsOnPlayerDeath() {
        return challengeEndsOnPlayerDeath;
    }

    @Deprecated(since = "1.9.10", forRemoval = true)
    public void setChallengeEndsOnPlayerDeath(boolean challengeEndsOnPlayerDeath) {
        this.challengeEndsOnPlayerDeath = challengeEndsOnPlayerDeath;
    }

    public boolean isChallengeEndsOnTimerExpire() {
        return challengeEndsOnTimerExpire;
    }

    public void setChallengeEndsOnTimerExpire(boolean challengeEndsOnTimerExpire) {
        this.challengeEndsOnTimerExpire = challengeEndsOnTimerExpire;
    }

    public boolean isChallenge() {
        return isChallenge;
    }

    public void setChallenge(boolean challenge) {
        isChallenge = challenge;
    }

    public boolean isUseTimer() {
        return useTimer;
    }

    public void setUseTimer(boolean useTimer) {
        this.useTimer = useTimer;
    }

    public boolean isTimerCountsUpwards() {
        return timerCountsUpwards;
    }

    public void setTimerCountsUpwards(boolean timerCountsUpwards) {
        this.timerCountsUpwards = timerCountsUpwards;
    }

    public boolean isHasBeenLoaded() {
        return hasBeenLoaded;
    }

    public void setHasBeenLoaded(boolean hasBeenLoaded) {
        this.hasBeenLoaded = hasBeenLoaded;
    }

    public void applyTimerSettings(){
        if (useTimer){
            if (timerCountsUpwards != MainTimer.isCountUpwards()) MainTimer.changeDirection();
        }
    }

    public @NotNull Map<String, Object> serialize(){
        Map<String, Object> map = new HashMap<>();
        map.put("challenge-ends-on-timer-expire", challengeEndsOnTimerExpire);
        map.put("is-challenge", isChallenge);
        map.put("use-timer", useTimer);
        map.put("timer-counts-upwards", timerCountsUpwards);
        map.put("has-been-loaded", hasBeenLoaded);
        return map;
    }
    public ChallengeSettings(Map<String, Object> map){
        challengeEndsOnTimerExpire=(boolean) map.get("challenge-ends-on-timer-expire");
        isChallenge=(boolean) map.get("is-challenge");
        useTimer = (boolean) map.get("use-timer");
        timerCountsUpwards = (boolean) Objects.requireNonNullElse(map.get("timer-counts-upwards"), true);
        hasBeenLoaded = (boolean) Objects.requireNonNullElse(map.get("has-been-loaded"), false);
    }
}
