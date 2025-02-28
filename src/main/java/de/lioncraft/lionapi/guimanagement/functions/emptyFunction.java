package de.lioncraft.lionapi.guimanagement.functions;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface emptyFunction {
    /**A Function used by various types of Buttons.
     *
     * @param event The Event when clicked on the Button.
     * @return weather the Button should be removed to save RAM or not.
     */
    boolean run(InventoryClickEvent event);
}
