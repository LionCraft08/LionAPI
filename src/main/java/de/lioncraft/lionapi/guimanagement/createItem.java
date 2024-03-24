package de.lioncraft.lionapi.guimanagement;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class createItem {
    public static ItemStack get(Component title, Material material, String lores){
        ItemStack tempItem = new ItemStack(material);
        ItemMeta ButtonMeta = tempItem.getItemMeta();
        ButtonMeta.displayName(title);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(lores));
        ButtonMeta.lore(lore);
        tempItem.setItemMeta(ButtonMeta);
        return tempItem;
    }
    public static ItemStack get(String title, Material material, String lore){
        ItemStack tempItem = new ItemStack(material);
        ItemMeta ButtonMeta = tempItem.getItemMeta();
        ButtonMeta.displayName(Component.text(title));
        List<Component> lores = new ArrayList<>();
        lores.add(Component.text(lore));
        ButtonMeta.lore(lores);
        tempItem.setItemMeta(ButtonMeta);
        return tempItem;
    }
    public static ItemStack get(Component title, Material material, String... lores){
        ItemStack tempItem = new ItemStack(material);
        ItemMeta ButtonMeta = tempItem.getItemMeta();
        ButtonMeta.displayName(title);
        List<Component> lore = new ArrayList<>();
        for(String s : lores){
            lore.add(Component.text(s));
        }
        ButtonMeta.addItemFlags();
        ButtonMeta.lore(lore);
        tempItem.setItemMeta(ButtonMeta);
        return tempItem;
    }
    public static ItemStack get(Component title, Material material, TextColor loreColor, String... lores){
        ItemStack tempItem = new ItemStack(material);
        ItemMeta ButtonMeta = tempItem.getItemMeta();
        ButtonMeta.displayName(title);
        List<Component> lore = new ArrayList<>();
        for(String s : lores){
            lore.add(Component.text(s, loreColor));
        }
        ButtonMeta.addItemFlags();
        ButtonMeta.lore(lore);
        tempItem.setItemMeta(ButtonMeta);
        return tempItem;
    }
    public static ItemStack get(Component title, Material material, Component... lores){
        ItemStack tempItem = new ItemStack(material);
        ItemMeta ButtonMeta = tempItem.getItemMeta();
        ButtonMeta.displayName(title);
        List<Component> lore = new ArrayList<>(Arrays.asList(lores));
        ButtonMeta.addItemFlags();
        ButtonMeta.lore(lore);
        tempItem.setItemMeta(ButtonMeta);
        return tempItem;
    }
}
