package de.lioncraft.lionapi.pluginPlusAPI;

import org.bukkit.scheduler.BukkitRunnable;

public class TPDelay extends BukkitRunnable {
    @Override
    public void run() {
        if (step == 0){
            tpUtils.speedEffect();
            step = 1;
        }else{
            tpUtils.teleport();
            this.cancel();
        }
    }
    private TPUtils tpUtils;
    private int step;

    public TPDelay(TPUtils tpUtils, int step) {
        this.tpUtils = tpUtils;
        this.step = step;
    }

}
