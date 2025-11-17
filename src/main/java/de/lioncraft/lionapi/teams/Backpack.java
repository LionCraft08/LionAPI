package de.lioncraft.lionapi.teams;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.data.Settings;
import de.lioncraft.lionapi.messageHandling.DM;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Backpack implements ConfigurationSerializable{
    public static HashMap<Team, Backpack> backpacks;
    public Backpack(Map<String, Object> map){
        title = Component.text("Backpack");
        inv = Bukkit.createInventory(null, (Integer) map.get("size"), title);
        List<ItemStack> list = (List<ItemStack>) map.get("content");
        int i = 0;
        for(ItemStack is : list){
            inv.setItem(i, is);
            i++;
        }
    }
    private Inventory inv;
    private Component title;
    public Backpack(int size, @Nullable Component title, @Nullable List<ItemStack> content){
        if(size%9!=0){
            LionAPI.getPlugin().getComponentLogger().warn(Component.text("Invalid Backpack Size: " + size));
            size = 54;
        }
        if(title == null){
            title = this.title = LionAPI.lm().msg("features.backpack");
        }
        inv = Bukkit.createInventory(null, size, title);
        if(content != null){
            for(int i = 0; i < 54; i++) {
                inv.setItem(i, content.get(i));
            }
        }
    }

    public boolean addItem(ItemStack is){
        return inv.addItem(is).isEmpty();
    }
    public void openBackpack(Player p){
        if(!Settings.isTeamsHaveBackpack()){
            LionChat.sendSystemMessage(LionAPI.lm().msg("features.backpack.disabled"), p);
            return;
        }
        p.openInventory(inv);
        p.playSound(p, Sound.BLOCK_AMETHYST_BLOCK_STEP, 1.0f, 1.0f);
    }
    public Inventory getInv() {
        return inv;
    }
    public Component getTitle() {
        return title;
    }
    public void setEmpty(){
        inv = Bukkit.createInventory(inv.getHolder(), inv.getSize(), title);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("content", inv.getContents());
        map.put("size", inv.getSize());
        return map;
    }
}
