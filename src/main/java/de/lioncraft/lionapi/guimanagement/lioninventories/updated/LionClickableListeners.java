package de.lioncraft.lionapi.guimanagement.lioninventories.updated;

import de.lioncraft.lionapi.guimanagement.lioninventories.LionInv;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LionClickableListeners implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof LionInv li){
            li.getButton(e.getSlot()).onClick(e);
            e.setCancelled(true);
        }
    }
}
