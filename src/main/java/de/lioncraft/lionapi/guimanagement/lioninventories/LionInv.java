package de.lioncraft.lionapi.guimanagement.lioninventories;

import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.lioninventories.updated.LionClickable;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class LionInv implements InventoryHolder {
    private Inventory inv;
    private HashMap<Integer, LionClickable> items = new HashMap<>();

    public LionInv(Material background, Component title) {
        init(background, title, 54);
    }
    public LionInv(Material background, Component title, int size) {
        init(background, title, size);
    }

    public LionClickable getButton(int slot){
        return items.get(slot);
    }

    public void setButton(int slot, LionClickable item){
        items.put(slot, item);
        inv.setItem(slot, item.getItem());
    }

    public void addButton(LionClickable item, boolean keepBorder){
        int slot = items.keySet().stream().max(Integer::compareTo).orElse(0);
        if(keepBorder){
            slot = getSlotWithBorder(slot, !(inv.getSize()<=18));
            if(slot >= inv.getSize()-9){throw new IndexOutOfBoundsException("Cannot add a new item, Inventory is full or the last usable slot is occupied");}
        }
        else if(slot>= inv.getSize()) {throw new IndexOutOfBoundsException("Cannot add a new item, Inventory is full or the last usable slot is occupied");}
        setButton(slot, item);
    }
    private int getSlotWithBorder(int targetedSlot, boolean keepTopBorder){
        if(keepTopBorder && targetedSlot < 10) return 10;
        if(targetedSlot%9==0)return targetedSlot+1;
        if((targetedSlot+1)%9==0) return targetedSlot+2;
        return targetedSlot;
    }



    private void init(Material background, Component title, int size) {
        inv = Bukkit.createInventory(this, size, title);
        if(background != null){
            inv.setContents(Items.createBlockButtonArray(background, size));
        }
        inv.setItem(49, Items.closeButton);
    }



    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
