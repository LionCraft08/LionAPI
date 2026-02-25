package de.lioncraft.lionapi.guimanagement.lioninventories.updated;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface LionClickable {
    void onClick(InventoryClickEvent event);
    ItemStack getItem();
}
