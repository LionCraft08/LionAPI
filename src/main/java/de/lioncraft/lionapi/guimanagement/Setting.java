package de.lioncraft.lionapi.guimanagement;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;


public class Setting {
    public static HashMap<ItemStack, Setting> SettingList;
    boolean isEnabled;
    ItemStack topItem, bottomItem;
    Function function;
    public Setting(boolean isEnabled, ItemStack topItem, Function function){
        this.isEnabled = isEnabled;
        this.function = function;
        this.topItem = topItem;
        changeBottomItem();
    }
    public void ClickAction(){
        isEnabled = !isEnabled;
        changeBottomItem();
        function.run(isEnabled);
    }
    private void changeBottomItem(){
        if(isEnabled){
            bottomItem = createItem.get(topItem.displayName(), Material.LIME_DYE, "Currently enabled", "Click to disable");

        }else{
            bottomItem = createItem.get(topItem.displayName(), Material.GRAY_DYE, "Currently disabled", "Click to enable");
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
