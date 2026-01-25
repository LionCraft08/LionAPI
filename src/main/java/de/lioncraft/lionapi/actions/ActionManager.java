package de.lioncraft.lionapi.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.UUID;

public class ActionManager {
    private static HashMap<UUID, ActionSet> actionSets = new HashMap<>();
    public static void openInventory(Player p, UUID actionSet){
        ActionSet as = actionSets.get(actionSet);
        if(as != null){
            as.getInventory().openInventory(p);
        }
    }
    public static void registerActionSet(ActionSet actionSet){
        actionSets.put(actionSet.getUuid(), actionSet);
    }
    public static void handleClick(UUID actionSet, String value, ClickType clickType, int slot){
        ActionSet as = actionSets.get(actionSet);
        if(as != null){
            as.getInventory().sendClick(slot, clickType, value);
        }
    }
    public static ActionSet getActionSet(UUID actionSet){
        return actionSets.get(actionSet);
    }
}
