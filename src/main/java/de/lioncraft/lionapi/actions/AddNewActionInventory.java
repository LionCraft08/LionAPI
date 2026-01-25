package de.lioncraft.lionapi.actions;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.Interaction.LionButtonFactory;
import de.lioncraft.lionapi.guimanagement.Items;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class AddNewActionInventory {
    private AddNewActionInventory() {}

    public static void open(Player p, UUID actionSet, List<LionActions> alreadyExistingActions, List<LionAdvancedActions> alreadyExistingAdvancedActions){
        List<LionActions> actions = new ArrayList<>(Arrays.asList(LionActions.values()));
        actions.removeAll(alreadyExistingActions);
        List<LionAdvancedActions> advancedActions = new ArrayList<>(Arrays.asList(LionAdvancedActions.values()));
        advancedActions.removeAll(alreadyExistingAdvancedActions);
        Inventory inv = createInventory(actions, advancedActions, actionSet);
        p.openInventory(inv);
    }
    private static Inventory createInventory(List<LionActions> actions, List<LionAdvancedActions> advancedActions, UUID actionSet){
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Add new Action"));
        inv.setContents(Items.blockButtons);
        inv.setItem(49, Items.closeButton);
        int i = 0;
        for(LionActions action : actions){
            ItemStack is = action.getConfigPreset().clone();
            inv.setItem(i, createItem(is, actionSet, "action", action.toString()));
            i++;
        }

        for(LionAdvancedActions action : advancedActions){
            ItemStack is = action.getConfigPreset().clone();
            inv.setItem(i, createItem(is, actionSet, "advanced_action",action.toString()));
            i++;
        }

        inv.setItem(45, LionButtonFactory.createButton(Items.getBackButton("Action Manager"), "lionactions.open."+actionSet.toString()));
        return inv;
    }

    private static ItemStack createItem(ItemStack is, UUID actionSet, String actionType, String actionName){
        List<Component> l = new ArrayList<>(Objects.requireNonNullElse(is.lore(), new ArrayList<>()));
        l.addAll(LionAPI.lm().getMessageAsList("inv.actions.add-new.item"));
        is.lore(l);//                                               0          1                     2             3
        return LionButtonFactory.createButton(is, "lionactions.add_item."+actionSet.toString()+"."+actionType+"."+actionName);
    }
}
