package de.lioncraft.lionapi.challenge;

import java.util.HashMap;
import java.util.Map;

public class ChallengeEndData {
    private boolean successful;
    private Map<String, Object> args = new HashMap<>();

    public ChallengeEndData(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public ChallengeEndData setArgs(Map<String, Object> args) {
        this.args = args;
        return this;
    }

}
