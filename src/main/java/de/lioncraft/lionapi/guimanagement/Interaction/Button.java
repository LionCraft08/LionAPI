package de.lioncraft.lionapi.guimanagement.Interaction;

import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.functions.emptyFunction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Button {
    public static HashMap<ItemStack, Button> activeButtons;
    emptyFunction function;
    ItemStack button;
    public Button(ItemStack button, emptyFunction function){
        button = Items.asGUIButton(button.clone());
        this.function = function;
        this.button = button;
        activeButtons.put(button, this);
    }
    public void ClickAction(InventoryClickEvent e){
        boolean b = function.run(e);
        if(b){
            e.getCurrentItem().setAmount(0);
            activeButtons.remove(button);
        }
    }
    public ItemStack getButton(){
        return button;
    }

    protected emptyFunction getFunction() {
        return function;
    }

    protected void setFunction(emptyFunction function) {
        this.function = function;
    }
}
