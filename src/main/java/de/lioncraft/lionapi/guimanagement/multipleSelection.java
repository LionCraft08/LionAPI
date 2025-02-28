package de.lioncraft.lionapi.guimanagement;



import de.lioncraft.lionapi.guimanagement.functions.IntegerFunction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

@Deprecated(since = "1.1")
public class multipleSelection {
    public static HashMap<ItemStack, multipleSelection> multipleSelectionMap;
    private List<ItemStack> buttons;
    private ItemStack button;
    private int currentState;
    private IntegerFunction function;

    /** Creates a multiple Selection with many buttons. The Buttons will be
     *
     * @param states: a List with buttons to switch through
     * @param currentState: the Button to start with
     */
    public multipleSelection(List<ItemStack> states, int currentState, IntegerFunction function){
        buttons = states;
        button = buttons.get(currentState);
        multipleSelectionMap.put(button, this);
        this.currentState = currentState;
        this.function = function;
    }
    public ItemStack changeButton(){
        currentState++;
        if(buttons.size()<=currentState){
            currentState = 0;
        }
        button = buttons.get(currentState);
        return button;
    }

    public ItemStack getButton() {
        return buttons.get(getCurrentState());
    }

    public int getCurrentState() {
        return currentState;
    }
    public void ClickAction(InventoryClickEvent e){
        button = changeButton();
        function.run(getCurrentState(), button, e);
    }
}
