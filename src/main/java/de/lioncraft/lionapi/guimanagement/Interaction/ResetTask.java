package de.lioncraft.lionapi.guimanagement.Interaction;

import org.bukkit.scheduler.BukkitRunnable;

class ResetTask extends BukkitRunnable {
    @Override
    public void run() {
        if(i.getStage()-i.getLastTick() >= 3){
            i.countDown();
            i.setLastTick(i.getLastTick()-2);
        }else{
            i.countUp();
        }
        if(tick%2 == 0){
            i.playSound();
        }
        tick++;
    }
    private Interactor i;
    private int tick;

    public ResetTask(Interactor i) {
        this.i = i;
        tick = i.getStage();
    }
}
