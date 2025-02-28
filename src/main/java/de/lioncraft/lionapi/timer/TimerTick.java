package de.lioncraft.lionapi.timer;

import org.bukkit.scheduler.BukkitRunnable;

class TimerTick extends BukkitRunnable {
    @Override
    public void run() {
        if(!timer.isActive()){
            this.cancel();
        }else {
            timer.tick();
        }
    }
    TimerLike timer;

    public TimerTick(TimerLike timer) {
        this.timer = timer;
    }
}
