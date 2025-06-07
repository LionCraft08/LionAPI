package de.lioncraft.lionapi.pluginPlusAPI;

import de.lioncraft.lionapi.LionAPI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TPUtils {
    private Player player;
    private Location targetedLocation;
    private BukkitTask t;

    public TPUtils(Player player, Location targetedLocation) {
        this.player = player;
        this.targetedLocation = targetedLocation;
    }
    public void teleport(){
        player.teleport(targetedLocation);
    }
    public void start(){
        t = new TPDelay(this, 0).runTaskTimer(LionAPI.getPlugin(), 0, 4);
    }
    public void speedEffect(){
        player.addPotionEffect(PotionEffectType.SPEED.createEffect(8,2));
        player.playSound(player, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, SoundCategory.MASTER, 1.0f, 1.0f);
    }
}
