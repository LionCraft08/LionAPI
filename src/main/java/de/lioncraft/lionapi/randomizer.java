package de.lioncraft.lionapi;

import de.lioncraft.lionapi.messageHandling.DM;
import de.lioncraft.lionapi.messageHandling.defaultMessages;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Deprecated (since = "1.2")
public class randomizer {
    public static Random random = new Random();
    public static List<EntityType> disabledEntityList;
    public static List<Material> disabledItemList;
    public static List<Biome> disabledBiomeList;
    public static ItemStack getRandomItem(){
        return new ItemStack(getRandomMaterial());
    }

    /**This will return a Random Item from the org.bukkit.Material enum.
     * Make sure you use getRandomAllowedMaterial() if you want the User to disable Items.
     * @return a random Item, not Material, meaning it's not legacy and .isItem() returns true
     */
    public static Material getRandomMaterial(){
        Material m;
        int i = 0;
        do{
            i++;
            m = Material.values()[random.nextInt(Material.values().length)];
            if(i>2000){
                Bukkit.getLogger().warning("[LionAPI] There might be any Items disabled in the config.yml, make sure you fix this. Until then, take some Cobblestone!");
                return Material.COBBLESTONE;

            }
        }while (!m.isItem()||m.isLegacy());
        return m;
    }
    /**This will return a Random Item from the org.bukkit.Material enum the user
     * has not disabled in the config.yml.disabled-items
     * @return a random, not disabled Item, not Material, meaning it's not legacy and .isItem() returns true
     */
    public static Material getRandomAllowedMaterial(){
        Material m;
        int i = 0;
        do {
            i++;
            m = getRandomMaterial();
            if(i>2000){
                Bukkit.getLogger().warning("[LionAPI] There might be any Items disabled in the config.yml, make sure you fix this. Until then, take some Cobblestone!");
                return Material.COBBLESTONE;
            }
        }while (disabledItemList.contains(m));
        return m;
    }
    /**This will return a Random Biome from the org.bukkit.Block.Biome enum.
     * Make sure you use getRandomAllowedBiome() if you want the User to disable Biomes.
     * @return a random Biome
     */
    public static Biome getRandomBiome(){
        return Biome.values()[random.nextInt(Biome.values().length)];
    }
    /**This will return a Random Biome from the org.bukkit.Block.Biome enum.
     * The user can disable Biomes in config.yml.disabled-biomes
     * @return a random Biome the User has not disabled
     */
    public static Biome getRandomAllowedBiome(){
        Biome b;
        int i = 0;
        do {
            i++;
            if(i>200){
                Bukkit.getLogger().warning("[LionAPI] There might be any Biomes disabled in the config.yml, make sure you fix this. Until then, take a Crimson Forest!");
                return Biome.CRIMSON_FOREST;
            }
            b = getRandomBiome();
        }while (disabledBiomeList.contains(b));
        return b;
    }
    /**This will return a Random EntityType from the org.bukkit.entity.EntityType enum.
     * @return a random Entity
     */
    public static @NotNull EntityType getRandomEntity(){
        EntityType et;
        do{
            et = EntityType.values()[random.nextInt(EntityType.values().length)];
        }while (et==null);
        return et;
    }
    /**This will return a Random EntityType from the org.bukkit.entity.EntityType enum.
     * The user can disable Biomes in config.yml.disabled-biomes
     * @return a random Entity the User didn't disable
     */
    public static @NotNull EntityType getRandomAllowedEntity(){
        EntityType et;
        int i = 0;
        do{
            i++;
            if(i>250){
                Bukkit.getLogger().warning("[LionAPI] There might be any Entities disabled in the config.yml, make sure you fix this. Until then, take a Zombie!");
                return EntityType.ZOMBIE;
            }
            et = EntityType.values()[random.nextInt(EntityType.values().length)];
        }while (disabledEntityList.contains(et));
        return et;
    }
    /**get a random Achievement
     * @return an Achievement
     */
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

    /**get a random Achievement a Player hasn't done yet
     * @param p the Player to check for it's Achievement
     * @return an Achievement the Player hasn't achieved jet
     */
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

    /**a Method to restore persistent data, do NOT use!!!!
     */
    public static void InitializeAllowedLists(){
        Bukkit.getLogger().info("Lists Initialized!");
        disabledEntityList = new ArrayList<>();
        disabledItemList = new ArrayList<>();
        disabledBiomeList = new ArrayList<>();
        for(String s : LionAPI.getPlugin().getConfig().getStringList("disabled-entities")){
            if(s != null){
                try {
                    disabledEntityList.add(EntityType.valueOf(s));
                }catch (IllegalArgumentException e){
                    Bukkit.getLogger().warning("[LionAPI] Could not find Entity " + s);
                }
            }
        }
        for(String s : LionAPI.getPlugin().getConfig().getStringList("disabled-items")){
            Material m = Material.getMaterial(s);
            if(m == null){
                Bukkit.getConsoleSender().sendMessage(DM.messagePrefix.append(Component.text("[LionAPI] Could not find Item " + s)));
            }else{
                randomizer.disabledItemList.add(m);
            }
        }
        for(String s : LionAPI.getPlugin().getConfig().getStringList("disabled-biomes")){
            if(s != null){
                try {
                    disabledBiomeList.add(Biome.valueOf(s));
                }catch (IllegalArgumentException e){
                    Bukkit.getLogger().warning("[LionAPI] Could not find Biome " + s);
                }
            }
        }
    }

}
