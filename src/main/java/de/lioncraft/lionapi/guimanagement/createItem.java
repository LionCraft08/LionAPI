package de.lioncraft.lionapi.guimanagement;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Deprecated(since = "1.1")
public class createItem {
    public static ItemStack getMaterial(EntityType et){
        ItemStack is = new ItemStack(Material.CREEPER_SPAWN_EGG);
        try{
            is.setType(Material.valueOf(et.toString() + "_SPAWN_EGG"));
        }catch (IllegalArgumentException e){
            try{
                is.setType(Material.valueOf(et.toString()));
            }catch (IllegalArgumentException exception){
                ItemMeta im = is.getItemMeta();
                switch (et){
                    case PLAYER -> is.setType(Material.PLAYER_HEAD);
                    case SNOW_GOLEM -> is.setType(Material.SNOW_GOLEM_SPAWN_EGG);
                    case BOAT -> is.setType(Material.BIRCH_BOAT);
                    case FIREBALL, DRAGON_FIREBALL, SMALL_FIREBALL -> is.setType(Material.FIRE_CHARGE);
                    case FIREWORK_ROCKET -> is.setType(Material.FIREWORK_ROCKET);
                    case END_CRYSTAL -> is.setType(Material.END_CRYSTAL);
                    case FALLING_BLOCK -> is.setType(Material.SAND);
                    case FISHING_BOBBER -> is.setType(Material.FISHING_ROD);
                    case GIANT -> is.setType(Material.ZOMBIE_SPAWN_EGG);
                    case ILLUSIONER -> {
                        is.setType(Material.PILLAGER_SPAWN_EGG);
                        im.displayName(Component.translatable(EntityType.ILLUSIONER.translationKey()).append(Component.text("Spawn Egg")));
                    }
                    case CHEST_MINECART -> is.setType(Material.CHEST_MINECART);
                    case COMMAND_BLOCK_MINECART -> is.setType(Material.COMMAND_BLOCK_MINECART);
                    case FURNACE_MINECART -> is.setType(Material.FURNACE_MINECART);
                    case TNT_MINECART -> is.setType(Material.TNT_MINECART);
                    case HOPPER_MINECART -> is.setType(Material.HOPPER_MINECART);
                    case SPAWNER_MINECART -> {
                        is.setType(Material.MINECART);
                        im.displayName(Component.text(et.translationKey()));
                    }
                    case WITHER_SKULL -> is.setType(Material.WITHER_SKELETON_SKULL);
                    case EXPERIENCE_BOTTLE -> is.setType(Material.EXPERIENCE_BOTTLE);
                    case SHULKER_BULLET -> {
                        is.setType(Material.SHULKER_BOX);
                        im.displayName(Component.translatable(et.translationKey()));
                    }
                    case TNT -> is.setType(Material.TNT);
                    case AREA_EFFECT_CLOUD -> {
                        is.setType(Material.POWDER_SNOW_BUCKET);
                        im.displayName(Component.text("AREA EFFECT CLOUD"));
                    }
                    case CHEST_BOAT -> is.setType(Material.OAK_CHEST_BOAT);
                    case LEASH_KNOT -> is.setType(Material.LEAD);
                    default -> im.displayName(Component.text(et.translationKey()));

                }
                is.setItemMeta(im);
            }
        }
        try {
            SpawnEggMeta sm = (SpawnEggMeta) is.getItemMeta();
            sm.setCustomSpawnedType(et);
        }catch (ClassCastException ignored){}
        return is;
    }
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
