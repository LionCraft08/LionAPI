package de.lioncraft.lionapi.events.challenge;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class challengeEndEvent extends Event {
    public de.lioncraft.lionapi.events.challenge.challengeEndType getChallengeEndType() {
        return challengeEndType;
    }

    private challengeEndType challengeEndType;
    public challengeEndEvent(challengeEndType challengeEndType){
        this.challengeEndType = challengeEndType;
    }
    private static final HandlerList handlers = new HandlerList();

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
