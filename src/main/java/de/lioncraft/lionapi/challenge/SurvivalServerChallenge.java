package de.lioncraft.lionapi.challenge;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class SurvivalServerChallenge extends ChallengeController{
    public SurvivalServerChallenge() {
        super(false, true);
    }

    public SurvivalServerChallenge(Map<String, Object> args) {
        super(args);
    }

    @Override
    protected void onStart() {

    }

    @Override
    public boolean canBePaused() {
        return false;
    }

    @Override
    protected void onPause() {

    }

    @Override
    protected void onFinish(ChallengeEndData data) {

    }

    @Override
    protected void onResume() {

    }

    @Override
    protected void onLoad() {

    }

    @Override
    protected void onJoin(Player p) {

    }

    @Override
    protected Inventory getConfigInventory(Player user) {
        return null;
    }
}
