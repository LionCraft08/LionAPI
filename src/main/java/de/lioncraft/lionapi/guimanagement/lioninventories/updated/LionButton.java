package de.lioncraft.lionapi.guimanagement.lioninventories.updated;

import de.lioncraft.lionapi.guimanagement.lioninventories.LionInv;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class LionButton implements LionClickable {
    private Consumer<InventoryClickEvent> onClick;
    private ItemStack item;
    public LionButton(Consumer<InventoryClickEvent> onClick, ItemStack itemStack) {
        this.onClick = onClick;
        item = itemStack;
    }
    @Override
    public void onClick(InventoryClickEvent event) {
        onClick.accept(event);
    }
    @Override
    public ItemStack getItem() {
        return item;
    }
    public void addToInventory(LionInv inventory, int slot) {
        inventory.setButton(slot, this);
    }
}
