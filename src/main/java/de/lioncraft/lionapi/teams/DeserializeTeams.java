package de.lioncraft.lionapi.teams;

import de.lioncraft.lionapi.LionAPI;
import org.bukkit.scheduler.BukkitRunnable;

public class DeserializeTeams extends BukkitRunnable {
    @Override
    public void run() {
        Team.loadAll();
    }
}
