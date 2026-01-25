package de.lioncraft.lionapi.listeners;

import de.lioncraft.lionapi.actions.*;
import de.lioncraft.lionapi.events.invs.LionButtonClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.UUID;

public class LionActionsGUIListeners implements Listener {

    /* lionactions.open.UUID
    * lionactions.click.UUID.SETTING
     */
    @EventHandler
    public void onClick(LionButtonClickEvent e){
        if (e.getID().equals("lionactions")){
            String[] args = e.getData().split("\\.");
            switch (args[0]) {
                case "open" -> {
                    ActionManager.openInventory(e.getPlayer(), UUID.fromString(args[1]));
                }
                case "click" -> {
                    ActionManager.handleClick(UUID.fromString(args[1]), args[2], e.e().getClick(), e.e().getSlot());
                }
                case "add_item" -> {
                    ActionSet as = ActionManager.getActionSet(UUID.fromString(args[1]));
                    switch (args[2]) {
                        case "action"->{
                            as.addAction(LionActions.valueOf(args[3]), true);
                        }
                        case "advanced_action"->{
                            LionAdvancedActions laa = LionAdvancedActions.valueOf(args[3]);
                            as.addAdvancedAction(laa, laa.getSelectableStrings().get(0));
                        }
                    }
                    as.getInventory().openInventory(e.getPlayer());
                    as.getInventory().updateInventory();
                }
                case "open_add_action_menu" -> {
                    ActionSet as = ActionManager.getActionSet(UUID.fromString(args[1]));
                    AddNewActionInventory.open(
                            e.getPlayer(),
                            as.getUuid(),
                            new ArrayList<>(as.getPresentLionActions()),
                            new ArrayList<>(as.getPresentLionAdvancedActions())
                    );
                }
            }
        }
    }
}
