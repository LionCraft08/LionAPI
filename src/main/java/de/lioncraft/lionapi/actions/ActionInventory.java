package de.lioncraft.lionapi.actions;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.Interaction.LionButtonFactory;
import de.lioncraft.lionapi.guimanagement.Interaction.Setting;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.lioninventories.LionInv;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ActionInventory {
    private Inventory inv;
    private ActionSet actionSet;

    private HashMap<Integer, LionActions> actionLocations = new HashMap<>();
    private HashMap<Integer, LionAdvancedActions> advancedActionLocations = new HashMap<>();
    private boolean goBackToChallengeControllerInv;

    public ActionInventory(ActionSet actionSet, boolean goBackToChallengeControllerInv) {
        this.actionSet = actionSet;
        this.goBackToChallengeControllerInv = goBackToChallengeControllerInv;
        initInventory(Component.text("Action Manager"));
    }

    private void initInventory(Component title) {
        this.inv = Bukkit.createInventory(null, 54, title);
        updateInventory();
    }

    public void openInventory(Player p) {
        p.openInventory(inv);
    }

    public void sendClick(int slot, ClickType clickType, String newState) {
        if (actionLocations.containsKey(slot)) {
            switch (clickType) {
                case LEFT -> {
                    actionSet.addAction(actionLocations.get(slot), Boolean.parseBoolean(newState));
                }
                case RIGHT -> {
                    actionSet.removeAction(actionLocations.get(slot));
                }
            }
        } else if (advancedActionLocations.containsKey(slot)) {
            switch (clickType) {
                case LEFT -> {
                    actionSet.addAdvancedAction(advancedActionLocations.get(slot), newState);
                }
                case RIGHT -> {
                    actionSet.removeAdvancedAction(advancedActionLocations.get(slot));
                }
            }
        } else return;
        updateInventory();
    }

    private ItemStack getItem(ItemStack baseItem, boolean state){
        List<Component> l = baseItem.lore();
        l.addAll(LionAPI.lm().getMessageAsList("inv.general.settings.value_toggleable", String.valueOf(state), String.valueOf(!state)));
        baseItem = baseItem.clone();
        baseItem.lore(l);
        return LionButtonFactory.createButton(baseItem, "lionactions.click."+actionSet.getUuid()+"."+!state);
    }

    private ItemStack getItem(ItemStack baseItem, String state, List<String> possible_values){
        List<Component> l = baseItem.lore();
        int i = possible_values.indexOf(state);
        i++;
        String s;
        if(i >= possible_values.size()){
            i=0;
        }
        s = possible_values.get(i);
        l.addAll(LionAPI.lm().getMessageAsList("inv.general.settings.value_toggleable", state, s));
        baseItem = baseItem.clone();
        baseItem.lore(l);
        return LionButtonFactory.createButton(baseItem, "lionactions.click."+actionSet.getUuid()+"."+s);
    }

    public void updateInventory() {
        inv.setContents(Items.blockButtons);
        inv.setItem(49, Items.closeButton);
        if (goBackToChallengeControllerInv)
            inv.setItem(45, LionButtonFactory.createButton(Items.backButton.clone(), "lionapi_open_challenge-controller_menu"));

        int i = 10;
        for (LionActions action : LionActions.values()) {
            if (i % 9 == 0) { i+=1;
            } else if ((i + 1) % 9 == 0) {i+=2;}

            Boolean b = actionSet.getActionValue(action);
            if (b != null) {
                inv.setItem(i, getItem(action.getConfigPreset(), b));
                actionLocations.put(i, action);
                i++;
            }
        }

        for (LionAdvancedActions action : LionAdvancedActions.values()) {
            if (i % 9 == 0) { i+=1;
            } else if ((i + 1) % 9 == 0) {i+=2;}

            String s = actionSet.getActionValue(action);
            if (s != null) {
                inv.setItem(i, getItem(action.getConfigPreset(), s, action.getSelectableStrings()));
                advancedActionLocations.put(i, action);
                i++;
            }
        }
        ItemStack is = Items.plusButton.clone();
        is.editMeta(meta -> {
            meta.displayName(LionAPI.lm().msg("inv.actions.add-new.title"));
            meta.lore(LionAPI.lm().getMessageAsList("inv.actions.add-new.lore"));
        });

        inv.setItem(
            i, LionButtonFactory.createButton(
                is,
                "lionactions.open_add_action_menu."+actionSet.getUuid().toString()
            )
        );
    }

}
