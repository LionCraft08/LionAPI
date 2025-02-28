package de.lioncraft.lionapi.events.team;

import de.lioncraft.lionapi.teams.Team;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TeamRemovePlayerEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public TeamRemovePlayerEvent(Team team, OfflinePlayer p) {
        this.team = team;
        this.p = p;
    }

    private Team team;
    private OfflinePlayer p;
    private boolean isCancelled = false;
    private Component cancelMessage;


    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    /**It is recommended to set {@link TeamRemovePlayerEvent#setCancelMessage(Component)} as well.
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        isCancelled =cancel;
    }

    public Component getCancelMessage() {
        return cancelMessage;
    }

    public void setCancelMessage(Component cancelMessage) {
        this.cancelMessage = cancelMessage;
    }

    public Team getTeam() {
        return team;
    }

    public OfflinePlayer getP() {
        return p;
    }
}
