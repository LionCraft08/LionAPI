package de.lioncraft.lionapi.guimanagement.lioninventories;

import de.lioncraft.lionapi.addons.AbstractAddon;
import de.lioncraft.lionapi.addons.AddonManager;
import de.lioncraft.lionapi.guimanagement.Interaction.Button;
import de.lioncraft.lionapi.guimanagement.Interaction.LionButtonFactory;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.MainMenu;
import de.lioncraft.lionapi.guimanagement.ScrollableInterface;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.sound.Sounds;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class AddonManageMenu {
    private static ScrollableInterface inv;
    public static void open(HumanEntity player){
        if (inv == null){
            List<ItemStack> list = new ArrayList<>();
            AddonManager.getAddons().forEach(addon -> {
                ItemStack is = getItem(addon);
                if (is == null) return;
                var ref = new Object() {
                    Button b = null;
                };
                ref.b = new Button(is, event -> {
                    boolean successful;
                    if (addon.isEnabled()) successful = addon.unload();
                    else successful = addon.load();
                    if (successful){
                        ItemStack newButton = getItem(addon);
                        event.setCurrentItem(newButton);
                        ref.b.updateButton(newButton);
                        event.getWhoClicked().playSound(Sounds.Click.getSound());
                    }else{
                        event.getWhoClicked().playSound(Sounds.Error.getSound());
                        LionChat.sendError("Ein Fehler ist aufgetreten.", event.getWhoClicked());
                    }
                    return false;
                }
                );
                list.add(is);
            });
            inv = new ScrollableInterface(list, Component.text("Addon Manager"), null, true, null);
            inv.setBackButton(MainMenu.getToMainMenuButton());
        }
        if (player.isOp()){
            player.openInventory(inv.getInventory());
        }else player.playSound(Sounds.getSound(Sounds.Error));
    }
    public static ItemStack getItem(AbstractAddon addon){
        ItemStack is = addon.getSettingsIcon();
        if (is==null) return null;
        is.editMeta(itemMeta -> {
            List<Component> lore = itemMeta.lore();
            if (lore == null) lore = new ArrayList<>();
            if (addon.isEnabled()){
                lore.add(Component.text("Leftclick to open config, Rightclick to disable", NamedTextColor.DARK_GRAY));
            }else{
                lore.add(Component.text("Leftclick to open config, Rightclick to enable", NamedTextColor.DARK_GRAY));
            }
        });
        return is;
    }

    public static ItemStack getItem(){
        return LionButtonFactory.createButton(Items.get(Component.text("Manage Addons", NamedTextColor.DARK_AQUA),
                Material.AMETHYST_SHARD, "Click to open the addon Manager"),
                "lionapi_open_addon_manager");
    }
}
