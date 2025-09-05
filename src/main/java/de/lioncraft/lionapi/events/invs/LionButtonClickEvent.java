package de.lioncraft.lionapi.events.invs;

import de.lioncraft.lionapi.guimanagement.Interaction.LionButtonFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class LionButtonClickEvent extends Event {
    private InventoryClickEvent e;
    private String id;
    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public LionButtonClickEvent(InventoryClickEvent e) {
        this.e = e;
        id = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(LionButtonFactory.getNamespacedKey(), PersistentDataType.STRING);

    }

    public InventoryClickEvent e() {
        return e;
    }
    public InventoryClickEvent getEvent(){
        return e;
    }
    public String getID(){
        if (id.contains("."))
            return id.substring(0, id.indexOf("."));
        return id;
    }
    public String getData(){
        if (id.contains(".")){
            return id.substring(id.indexOf(".")+1);
        }
        return "";
    }
    public ItemStack getItem(){
        return e.getCurrentItem();
    }
    public Player getPlayer(){
        return (Player) e.getWhoClicked();
    }
}
