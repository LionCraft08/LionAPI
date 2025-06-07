package de.lioncraft.lionapi.data;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class ChallengeSettings implements ConfigurationSerializable {
    private boolean challengeEndsOnDragonDeath;
    private boolean challengeEndsOnPlayerDeath;
    private boolean challengeEndsOnTimerExpire, isChallenge, useTimer;


    public ChallengeSettings(){
        challengeEndsOnDragonDeath = false;
        challengeEndsOnTimerExpire = false;
        challengeEndsOnPlayerDeath = false;
        isChallenge = false;
        useTimer = true;
    }

    public boolean isChallengeEndsOnDragonDeath() {
        return challengeEndsOnDragonDeath;
    }

    public void setChallengeEndsOnDragonDeath(boolean challengeEndsOnDragonDeath) {
        this.challengeEndsOnDragonDeath = challengeEndsOnDragonDeath;
    }

    public boolean isChallengeEndsOnPlayerDeath() {
        return challengeEndsOnPlayerDeath;
    }

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

    public @NotNull Map<String, Object> serialize(){
        Map<String, Object> map = new HashMap<>();
        map.put("challenge-ends-on-dragon-death", challengeEndsOnDragonDeath);
        map.put("challenge-ends-on-player-death", challengeEndsOnPlayerDeath);
        map.put("challenge-ends-on-timer-expire", challengeEndsOnTimerExpire);
        map.put("is-challenge", isChallenge);
        map.put("use-timer", useTimer);
        return map;
    }
    public ChallengeSettings(Map<String, Object> map){
        challengeEndsOnDragonDeath= (boolean) map.get("challenge-ends-on-dragon-death");
        challengeEndsOnPlayerDeath=(boolean) map.get("challenge-ends-on-player-death");
        challengeEndsOnTimerExpire=(boolean) map.get("challenge-ends-on-timer-expire");
        isChallenge=(boolean) map.get("is-challenge");
        useTimer = (boolean) map.get("use-timer");
    }
}
