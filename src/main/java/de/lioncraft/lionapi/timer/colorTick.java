package de.lioncraft.lionapi.timer;

import de.lioncraft.lionapi.data.BasicSettings;
import org.bukkit.scheduler.BukkitRunnable;

class colorTick extends BukkitRunnable {
    private int tickCounter = 0;
    @Override
    public void run() {
        if(!timer.isActive() && TimerConfig.getWhenPaused().equalsIgnoreCase("OFF")){
            this.cancel();
            return;
        }
        tickCounter++;
        if(tickCounter >= TimerConfig.getDelay()){
            timer.colorTick();
            tickCounter = 0;
        }
    }
    private TimerLike timer;

    public colorTick(TimerLike t) {
        this.timer = t;
    }
}
