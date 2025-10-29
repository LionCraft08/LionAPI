package de.lioncraft.lionapi.guimanagement.functions;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface StringSelectionFunction {
    void run(int currentState, String currentString, InventoryClickEvent event);
}
