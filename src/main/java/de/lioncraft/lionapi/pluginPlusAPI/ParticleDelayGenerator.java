package de.lioncraft.lionapi.pluginPlusAPI;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleDelayGenerator extends BukkitRunnable {
    @Override
    public void run() {
        if (repetition <=0) this.cancel();
        Location newl = new Location(l.getWorld(), l.getX()+Math.sin(Math.toRadians(current)), y, l.getZ()+Math.cos(Math.toRadians(current)));
        new ParticleBuilder(Particle.DUST_COLOR_TRANSITION).allPlayers().colorTransition(Color.AQUA, Color.BLUE).location(newl).spawn();
        current+=step;
        y+=ystep;
        repetition--;
    }
    private int step, current;
    private double y, ystep;
    private Location l;
    private int repetition;

    public ParticleDelayGenerator(int step, Player p, double ystep, int repetitions) {
        this.step = step;
        current = 0;
        this.ystep = ystep;
        l = p.getLocation();
        y=l.getY();
        this.repetition = repetitions;
    }
}
