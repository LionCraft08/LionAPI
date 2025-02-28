package de.lioncraft.lionapi.timer;

import org.bukkit.scheduler.BukkitRunnable;

class colorTick extends BukkitRunnable {
    @Override
    public void run() {
        if(!timer.isActive()){
            this.cancel();
            return;
        }
        timer.colorTick();
    }
    private TimerLike timer;

    public colorTick(TimerLike t) {
        this.timer = t;
    }
}
