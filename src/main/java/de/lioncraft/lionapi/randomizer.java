package de.lioncraft.lionapi;

import io.papermc.paper.advancement.AdvancementDisplay;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class randomizer {
    public static Random random = new Random();
    public static List<EntityType> disabledEntityList;
    public static List<Material> disabledItemList;
    public static ItemStack getRandomItem(){
        return new ItemStack(getRandomMaterial());
    }
    public static Material getRandomMaterial(){
        Material m;
        do{
            m = Material.values()[random.nextInt(Material.values().length)];
        }while (!m.isItem()||m.isLegacy());
        return m;
    }
    public static Material getRandomAllowedMaterial(){
        Material m;
        do {
            m = getRandomMaterial();
        }while (disabledItemList.contains(m));
        return m;
    }
    public static Biome getRandomBiome(){
        Biome b;
        do {
            b = Biome.values()[random.nextInt(Biome.values().length)];
        }while (b!=Biome.THE_VOID);
        return b;
    }
    public static EntityType getRandomEntity(){
        EntityType et;
        do{
            et = EntityType.values()[random.nextInt(EntityType.values().length)];
        }while (disabledEntityList.contains(et));
        return et;
    }
    public static Advancement getRandomAdvancement(){
        Advancement a;
        int i = 0;
        List<Advancement> list = new ArrayList<>();
        Iterator<Advancement> id = Bukkit.advancementIterator();
        while (id.hasNext()) {
            id.next();
            list.add(id.next());
            i++;
        }
        int j = new Random().nextInt(i);
        return list.get(j);
    }
    public static Advancement getRandomNotGainedAdvancement(Player p){
        Advancement a;
        int i = 0;
        List<Advancement> list = new ArrayList<>();
        Iterator<Advancement> id = Bukkit.advancementIterator();
        while (id.hasNext()) {
            id.next();
            list.add(id.next());
            i++;
        }
        do {
            int j = new Random().nextInt(i);
            a = list.get(j);
        }while (p.getAdvancementProgress(a).isDone());
        return a;
    }
    public static void InitializeEntityList(){
        disabledEntityList = new ArrayList<>();
        disabledEntityList.add(EntityType.AREA_EFFECT_CLOUD);
        disabledEntityList.add(EntityType.ARROW);
        disabledEntityList.add(EntityType.BLOCK_DISPLAY);
        disabledEntityList.add(EntityType.DRAGON_FIREBALL);
        disabledEntityList.add(EntityType.DROPPED_ITEM);
        disabledEntityList.add(EntityType.EGG);
        disabledEntityList.add(EntityType.ENDER_CRYSTAL);
        disabledEntityList.add(EntityType.ENDER_PEARL);
        disabledEntityList.add(EntityType.ENDER_SIGNAL);
        disabledEntityList.add(EntityType.EVOKER_FANGS);
        disabledEntityList.add(EntityType.EXPERIENCE_ORB);
        disabledEntityList.add(EntityType.FALLING_BLOCK);
        disabledEntityList.add(EntityType.FIREBALL);
        disabledEntityList.add(EntityType.FIREWORK);
        disabledEntityList.add(EntityType.FISHING_HOOK);
        disabledEntityList.add(EntityType.GIANT);
        disabledEntityList.add(EntityType.GLOW_ITEM_FRAME);
        disabledEntityList.add(EntityType.ILLUSIONER);
        disabledEntityList.add(EntityType.INTERACTION);
        disabledEntityList.add(EntityType.ITEM_DISPLAY);
        disabledEntityList.add(EntityType.ITEM_FRAME);
        disabledEntityList.add(EntityType.LEASH_HITCH);
        disabledEntityList.add(EntityType.LLAMA_SPIT);
        disabledEntityList.add(EntityType.LIGHTNING);
        disabledEntityList.add(EntityType.MARKER);
        disabledEntityList.add(EntityType.MINECART);
        disabledEntityList.add(EntityType.MINECART_CHEST);
        disabledEntityList.add(EntityType.MINECART_COMMAND);
        disabledEntityList.add(EntityType.MINECART_FURNACE);
        disabledEntityList.add(EntityType.MINECART_HOPPER);
        disabledEntityList.add(EntityType.MINECART_MOB_SPAWNER);
        disabledEntityList.add(EntityType.MINECART_TNT);
        disabledEntityList.add(EntityType.PAINTING);
        disabledEntityList.add(EntityType.PLAYER);
        disabledEntityList.add(EntityType.PRIMED_TNT);
        disabledEntityList.add(EntityType.SHULKER_BULLET);
        disabledEntityList.add(EntityType.SMALL_FIREBALL);
        disabledEntityList.add(EntityType.SNOWBALL);
        disabledEntityList.add(EntityType.SPECTRAL_ARROW);
        disabledEntityList.add(EntityType.SPLASH_POTION);
        disabledEntityList.add(EntityType.TEXT_DISPLAY);
        disabledEntityList.add(EntityType.THROWN_EXP_BOTTLE);
        disabledEntityList.add(EntityType.UNKNOWN);
        disabledEntityList.add(EntityType.ZOMBIE_HORSE);
        disabledEntityList.add(EntityType.WITHER_SKULL);
        disabledEntityList.add(EntityType.TRIDENT);
        disabledEntityList.add(EntityType.BOAT);
        disabledEntityList.add(EntityType.CHEST_BOAT);
        disabledEntityList.add(EntityType.ARMOR_STAND);
    }
}
