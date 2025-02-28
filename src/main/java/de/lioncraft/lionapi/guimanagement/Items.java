package de.lioncraft.lionapi.guimanagement;

import com.destroystokyo.paper.profile.PlayerProfile;
import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.listeners.invClickListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public final class Items {
    public static ItemStack closeButton;
    public static ItemStack blockButton;
    public static ItemStack backButton;
    public static ItemStack forwardButton;
    public static ItemStack[] blockButtons;
    public static ItemStack plusButton;
    public static ItemStack MinusButton;
    public static ItemStack scrollUpButton;
    public static ItemStack scrollDownButton;

    public static void setItems(){
        closeButton = Items.get(Component.text("CLOSE", TextColor.color(255, 0, 0)), Material.BARRIER, TextColor.color(255, 0, 0),"Click to close the GUI");
        blockButton = Items.get(Component.text(""), Material.BLACK_STAINED_GLASS_PANE, "");
        backButton =  Items.get(Component.text("Back", TextColor.color(255, 255, 0)), Material.ARROW, TextColor.color(255, 255, 0), "Click to get back");
        forwardButton =  Items.get(Component.text("Next", TextColor.color(255, 255, 0)), Material.SPECTRAL_ARROW, TextColor.color(255, 255, 0), "Click to get to ", "the next page.");
        blockButtons = new ItemStack[54];
        Arrays.fill(blockButtons, blockButton);
        plusButton = PlusButton();
        MinusButton = MinusButton();
        scrollUpButton = Items.get(Component.text("UP", TextColor.color(255, 0,255 )), Material.RED_BANNER, TextColor.color(255, 128, 0), "Click to scroll up / back", "Shift + Click to scroll to the beginning");
        scrollDownButton = Items.get(Component.text("DOWN", TextColor.color(0, 255, 255)), Material.GREEN_BANNER, TextColor.color(255, 128, 0),"Click to scroll down / forward", "Shift + Click to scroll to the end");
        LionAPI.getPlugin().getComponentLogger().info(Component.text("Init Step 2 Complete"));
    }
    public static ItemStack getPlayerHead(OfflinePlayer player){
        ItemStack is = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) is.getItemMeta();
        sm.setOwningPlayer(player);
        try {
            sm.displayName(Component.text(Objects.requireNonNull(player.getName())));
        }catch (NullPointerException r){
            return null;
        }
        is.setItemMeta(sm);
        return is;
    }
    private static ItemStack PlusButton(){
        ItemStack button = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta buttonmeta = (SkullMeta) button.getItemMeta();
        buttonmeta.setPlayerProfile(Bukkit.createProfile("LionK08"));
        PlayerProfile pp = Bukkit.createProfile("LionK08");
        PlayerTextures pt = Objects.requireNonNull(buttonmeta.getPlayerProfile()).getTextures();
        try {
            pt.setSkin(new URI("https://textures.minecraft.net/texture/5d8604b9e195367f85a23d03d9dd503638fcfb05b0032535bc43734422483bde").toURL());
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        pp.setTextures(pt);
        buttonmeta.setPlayerProfile(pp);
        buttonmeta.displayName(Component.text("+"));
        button.setItemMeta(buttonmeta);
        return button;
    }
    private static ItemStack MinusButton(){
        ItemStack button = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta buttonmeta = (SkullMeta) button.getItemMeta();
        buttonmeta.setPlayerProfile(Bukkit.createProfile("LionK08"));
        PlayerProfile pp = Bukkit.createProfile("LionK08");
        PlayerTextures pt = null;
        try {
            pt = Objects.requireNonNull(buttonmeta.getPlayerProfile()).getTextures();
        }catch (NullPointerException ignored){}
        try {
            if (pt != null) {
                pt.setSkin(new URI("https://textures.minecraft.net/texture/482c23992a02725d9ed1bcd90fd0307c8262d87e80ce6fac8078387de18d0851").toURL());
            }
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        pp.setTextures(pt);
        buttonmeta.setPlayerProfile(pp);
        buttonmeta.displayName(Component.text("-"));
        button.setItemMeta(buttonmeta);
        return button;
    }
    public static ItemStack getMaterial(EntityType et){
        Material m = Material.ALLIUM;
        Component c = null;
        try{
            m = Material.valueOf(et.toString() + "_SPAWN_EGG");
        }catch (IllegalArgumentException e){
            try{
                m = Material.valueOf(et.toString());
            }catch (IllegalArgumentException exception){
                switch (et){
                    case PLAYER -> m = Material.PLAYER_HEAD;
                    case SNOW_GOLEM -> m = Material.SNOW_GOLEM_SPAWN_EGG;
                    case BOAT -> m = Material.BIRCH_BOAT;
                    case FIREBALL, DRAGON_FIREBALL, SMALL_FIREBALL -> m = Material.FIRE_CHARGE;
                    case FIREWORK_ROCKET -> m = Material.FIREWORK_ROCKET;
                    case END_CRYSTAL -> m = Material.END_CRYSTAL;
                    case FALLING_BLOCK -> m = Material.SAND;
                    case FISHING_BOBBER -> m = Material.FISHING_ROD;
                    case GIANT -> m = Material.ZOMBIE_SPAWN_EGG;
                    case ILLUSIONER -> {
                        m = Material.PILLAGER_SPAWN_EGG;
                        c = Component.translatable(EntityType.ILLUSIONER.translationKey()).append(Component.text("Spawn Egg"));
                    }
                    case CHEST_MINECART -> m = Material.CHEST_MINECART;
                    case COMMAND_BLOCK_MINECART -> m = Material.COMMAND_BLOCK_MINECART;
                    case FURNACE_MINECART -> m = Material.FURNACE_MINECART;
                    case TNT_MINECART -> m = Material.TNT_MINECART;
                    case HOPPER_MINECART -> m = Material.HOPPER_MINECART;
                    case SPAWNER_MINECART -> {
                        m = Material.MINECART;
                        c = Component.text(et.translationKey());
                    }
                    case WITHER_SKULL -> m = Material.WITHER_SKELETON_SKULL;
                    case EXPERIENCE_BOTTLE -> m = Material.EXPERIENCE_BOTTLE;
                    case SHULKER_BULLET -> {
                        m = Material.SHULKER_BOX;
                        c = Component.translatable(et.translationKey());
                    }
                    case TNT -> m = Material.TNT;
                    case AREA_EFFECT_CLOUD -> {
                        m = Material.POWDER_SNOW_BUCKET;
                        c = Component.text("AREA EFFECT CLOUD");
                    }
                    case CHEST_BOAT -> m = Material.OAK_CHEST_BOAT;
                    case LEASH_KNOT -> m = Material.LEAD;
                    default -> c = Component.text(et.translationKey());

                }
            }
        }
        ItemStack is  = new ItemStack(m);
        if(c != null){
            Component finalC = c;
            is.editMeta(itemMeta -> {
                itemMeta.displayName(finalC);
            });
        }
        try {
            SpawnEggMeta sm = (SpawnEggMeta) is.getItemMeta();
            sm.setCustomSpawnedType(et);
        }catch (ClassCastException ignored){}
        return is;
    }
    public static ItemStack get(String title, Material material, TextColor loreColor, String... lore){
        return get(Component.text(title), material, loreColor,lore);
    }
    public static ItemStack get(String title, Material material, String... lore){
        return get(Component.text(title), material, lore);
    }
    public static ItemStack get(Component title, Material material, String... lores){
        return get(title, material, null, lores);
    }
    public static ItemStack get(Component title, Material material, TextColor loreColor, String... lores){
        Component[] lore = new Component[lores.length];
        int i = 0;
        for(String s : lores){
            lore[i] = (Component.text(s, loreColor));
            i++;
        }
        return get(title, material, lore);
    }
    public static ItemStack get(Component title, Material material, Component... lores){
        ItemStack tempItem = new ItemStack(material);
        ItemMeta ButtonMeta = tempItem.getItemMeta();
        if(ButtonMeta == null){
            System.out.println("Error: "+material);
        }
        ButtonMeta.displayName(title);
        List<Component> lore = new ArrayList<>(Arrays.asList(lores));
        ButtonMeta.lore(lore);
        tempItem.setItemMeta(ButtonMeta);
        return tempItem;
    }
    public static ItemStack getBackButton(String InvTitle){
        return getBackButton(Component.text(InvTitle, TextColor.color(0, 255, 255)));
    }
    public static ItemStack getBackButton(Component InvTitle){
        ItemStack is = backButton.clone();
        ItemMeta im = is.getItemMeta();
        im.lore(List.of(Component.text("Go back to ", TextColor.color(255, 128, 0)).append(InvTitle)));
        is.setItemMeta(im);
        return is;
    }

    /**Creates a Button wich can be used as a Button in a GUI,
     * wich means you can't take it out of the Inventory.
     * @param is the {@link ItemStack} to make a Button
     * @return the ItemStack itself, transferred to a Button.
     */
    public static ItemStack asGUIButton(ItemStack is){
        ItemMeta im = is.getItemMeta();
        im.getPersistentDataContainer().set(invClickListener.getDisabledClick(), PersistentDataType.STRING, UUID.randomUUID().toString());
        is.setItemMeta(im);
        return is;
    }
}
