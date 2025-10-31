package de.lioncraft.lionapi.guimanagement.Interaction;

import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.functions.Function;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;


public class Setting {
    public static HashMap<ItemStack, Setting> SettingList = new HashMap<>();
    boolean isEnabled;
    ItemStack topItem, bottomItem;
    Function function;
    public Setting(boolean isEnabled, ItemStack topItem, Function function){
        this.isEnabled = isEnabled;
        this.function = function;
        this.topItem = Items.asGUIButton(topItem);
        changeBottomItem();
    }
    public void ClickAction(){
        isEnabled = !isEnabled;
        changeBottomItem();
        function.run(isEnabled);
    }
    private void changeBottomItem(){
        if(isEnabled){
            bottomItem = Items.get(topItem.displayName(), Material.LIME_DYE, "Currently enabled", "Click to disable");

        }else{
            bottomItem = Items.get(topItem.displayName(), Material.GRAY_DYE, "Currently disabled", "Click to enable");
        }
        bottomItem.getItemMeta().setUnbreakable(true);
        SettingList.put(bottomItem, this);
    }
    public boolean isEnabled() {
        return isEnabled;
    }
    public ItemStack getBottomItem() {return bottomItem;}
    public ItemStack getTopItem() {return topItem;}
}
