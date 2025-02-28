package de.lioncraft.lionapi.guimanagement;

import de.lioncraft.lionapi.guimanagement.functions.emptyFunction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

@Deprecated(since = "1.1")
public class button {
    public static HashMap<ItemStack, button> activeButtons;
    emptyFunction function;
    ItemStack button;
    public button(ItemStack button, emptyFunction function){
        this.function = function;
        this.button = button;
        activeButtons.put(button, this);
    }
    public void ClickAction(InventoryClickEvent e){
        function.run(e);
    }
    public ItemStack getButton(){
        return button;
    }
}
