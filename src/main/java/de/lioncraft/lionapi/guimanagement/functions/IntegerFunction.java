package de.lioncraft.lionapi.guimanagement.functions;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface IntegerFunction {
    void run(int currentState, ItemStack currentButton, InventoryClickEvent event);
}
