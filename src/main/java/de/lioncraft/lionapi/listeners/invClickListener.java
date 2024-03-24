package de.lioncraft.lionapi.listeners;

import de.lioncraft.lionapi.guimanagement.Setting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class invClickListener implements Listener {
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            if (Setting.SettingList.get(e.getCurrentItem()) != null) {
                e.setCancelled(true);
                Setting s = Setting.SettingList.get(e.getCurrentItem());
                s.ClickAction();
                Objects.requireNonNull(e.getClickedInventory()).setItem(e.getSlot(), s.getBottomItem());
            }
        }
    }
}
