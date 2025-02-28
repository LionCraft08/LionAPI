package de.lioncraft.lionapi.guimanagement;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;

@Deprecated(since = "1.1")
public class buttons {
    public static ItemStack closeButton;
    public static ItemStack blockButton;
    public static ItemStack backButton;
    public static ItemStack forwardButton;
    public static ItemStack[] blockButtons;
    public static ItemStack plusButton;
    public static ItemStack MinusButton;
    public static ItemStack scrollUpButton;
    public static ItemStack scrollDownButton;

    @Deprecated(forRemoval = false)
    public static void setItems(){
        closeButton = Items.get(Component.text("CLOSE", TextColor.color(255, 0, 0)), Material.BARRIER, TextColor.color(255, 0, 0),"Click to close the GUI");
        blockButton = Items.get(Component.text(""), Material.BLACK_STAINED_GLASS_PANE, "");
        backButton =  Items.get(Component.text("Back", TextColor.color(255, 255, 0)), Material.ARROW, TextColor.color(255, 255, 0), "Click to get back");
        forwardButton =  Items.get(Component.text("Next", TextColor.color(255, 255, 0)), Material.SPECTRAL_ARROW, TextColor.color(255, 255, 0), "Click to get to ", "the next page.");
        blockButtons = new ItemStack[53];
        Arrays.fill(blockButtons, blockButton);
        plusButton = PlusButton();
        MinusButton = MinusButton();
        scrollUpButton = Items.get(Component.text("UP", TextColor.color(255, 0,255 )), Material.RED_BANNER, TextColor.color(255, 128, 0), "Click to scroll up / back", "Shift + Click to scroll to the beginning");
        scrollDownButton = Items.get(Component.text("DOWN", TextColor.color(0, 255, 255)), Material.GREEN_BANNER, TextColor.color(255, 128, 0),"Click to scroll down / forward", "Shift + Click to scroll to the end");
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
}
