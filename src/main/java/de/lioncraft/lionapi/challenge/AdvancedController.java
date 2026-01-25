package de.lioncraft.lionapi.challenge;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.actions.ActionSet;
import de.lioncraft.lionapi.actions.LionActions;
import de.lioncraft.lionapi.actions.LionAdvancedActions;
import de.lioncraft.lionapi.data.BasicSettings;
import de.lioncraft.lionapi.data.Setting;
import de.lioncraft.lionapi.guimanagement.Interaction.Button;
import de.lioncraft.lionapi.guimanagement.Interaction.LionButtonFactory;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.MainMenu;
import de.lioncraft.lionapi.permissions.LionAPIPermissions;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**An advanced version of {@link ChallengeController}. <br>
 * Provides Settings for every event of the timer to be configured by the user, such as
 * onStart, onFinish, onPause,...
 * If you override one of these methods ({@link ChallengeController#onLoad()},...),
 * make sure to call the method in this class as well (e.g. super.onLoad())
 */
public abstract class AdvancedController extends ChallengeController {
    private HashMap<String, ActionSet> actionSets = new HashMap<>();

    public AdvancedController(boolean useTimer, boolean timerCountsUpwards) {
        super(useTimer, timerCountsUpwards);
        actionSets = getDefaultConfig();
    }


    private static HashMap<String, ActionSet> getDefaultConfig() {
        HashMap<String, ActionSet> actionSets = new HashMap<>();
        actionSets.put("load",  new ActionSet(Map.of(
                LionActions.FREEZE_WORLD, true,
                LionActions.FREEZE_PLAYERS, false,
                LionActions.INVULNERABLE_PLAYERS, true
        ), Map.of(), true));
        actionSets.put("pause",  new ActionSet(Map.of(
                LionActions.FREEZE_WORLD, true,
                LionActions.FREEZE_PLAYERS, true
        ), Map.of(), true));
        actionSets.put("start",  new ActionSet(Map.of(
                LionActions.RESET_PLAYER_HEALTH, true
        ), Map.of(
        ), true));
        actionSets.put("resume",  new ActionSet(Map.of(
                LionActions.FREEZE_WORLD, false,
                LionActions.FREEZE_PLAYERS, false,
                LionActions.ALLOW_PVP, true,
                LionActions.ALLOW_FLYING, false,
                LionActions.BLOCK_WORLD_INTERACTIONS, false,
                LionActions.INVULNERABLE_PLAYERS, false
        ), Map.of(
                LionAdvancedActions.SET_GAMEMODE, "SURVIVAL"
        ), true));
        actionSets.put("finish",  new ActionSet(Map.of(
                LionActions.ALLOW_FLYING, true,
                LionActions.BLOCK_WORLD_INTERACTIONS, true,
                LionActions.INVULNERABLE_PLAYERS, true
        ), Map.of(), true));
        return actionSets;
    }

    public AdvancedController(Map<String, Object> args) {
        super(args);
        HashMap<String, Object> actions = (HashMap<String, Object>) args.get("action-settings");
        if (actions != null)
            actions.forEach((key, value) -> actionSets.put(key, (ActionSet) value));
    }

    @Override
    protected void onStart(){
        actionSets.get("start").executeActions();
        onResume();
    }
    @Override
    protected void onPause(){
        actionSets.get("pause").executeActions();
    }
    @Override
    protected void onFinish(ChallengeEndData data){
        actionSets.get("finish").executeActions();
    }
    @Override
    protected void onResume(){
        actionSets.get("resume").executeActions();
    }

    @Override
    protected void onLoad(){
        actionSets.get("load").executeActions();
    }


    @Override
    public @NonNull Map<String, Object> serialize() {
        Map<String, Object> serialized = super.serialize();
        serialized.put("action-settings", actionSets);
        return serialized;
    }




    @Override
    protected Inventory getConfigInventory(Player user){
        if (inv == null) {
            inv = Bukkit.createInventory(null, 54, Component.text("Advanced Challenge Controller"));
            inv.setContents(Items.blockButtons);
            inv.setItem(49, Items.closeButton);
            Button openDefaultConfig = new Button(Items.get(
                    LionAPI.lm().msg("inv.challenge-controller.open-default-inv.title"),
                    Material.COMMAND_BLOCK,
                    LionAPI.lm().getMessageAsList("inv.challenge-controller.open-default-inv.lore")),
                    event -> {
                        Inventory inv = getConfigInventory();
                        if (inv != null) {
                            event.getWhoClicked().openInventory(inv);
                        }
                        return false;
                    });
            int i = 10;
            for (Map.Entry<String, ActionSet> entry : actionSets.entrySet()) {
                if (i % 9 == 0) {
                    i+=1;
                } else if ((i + 1) % 9 == 0) {
                    i+=2;
                }
                inv.setItem(i,
                        LionButtonFactory.createButton(
                                getActionItem(entry.getKey()),
                                "lionactions.open."+entry.getValue().getUuid()));
                i++;
            }
            inv.setItem(53, openDefaultConfig.getButton());
            inv.setItem(45, MainMenu.getToMainMenuButton());
        }
        return inv;

    }

    private ItemStack getActionItem(String action) {
        return Items.get(
                LionAPI.lm().msg("inv.challenge-controller.config."+action.toLowerCase()+".title"),
                Material.CLOCK,
                LionAPI.lm().getMessageAsList("inv.challenge-controller.config."+action.toLowerCase()+".lore")
        );
    }

    private Inventory inv;

    protected abstract Inventory getConfigInventory();
}
