package de.lioncraft.lionapi.pluginPlusAPI;

import de.lioncraft.lionapi.LionAPI;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TPUtils {
    private static HashMap<Player, TPUtils> queuedTPs = new HashMap<>();
    public static void teleport(Player p, int fadeOutDuration, Location targetedLocation){
        p.spawnParticle(Particle.COMPOSTER, p.getLocation(), 0);
    }
    private Player player;
    private Location targetedLocation;
    private int duration;
    private BukkitTask t;

    public TPUtils(Player player, Location targetedLocation, int duration) {
        this.player = player;
        this.targetedLocation = targetedLocation;
        this.duration = duration;
        t = new ParticleDelayGenerator(1, player, 1, 100).runTaskTimerAsynchronously(LionAPI.getPlugin(), 0, 2);
        player.playSound(player, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, SoundCategory.MASTER, 1.0f, 1.0f);
    }
}
