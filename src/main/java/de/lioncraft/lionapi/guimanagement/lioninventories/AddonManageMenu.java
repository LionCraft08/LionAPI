package de.lioncraft.lionapi.guimanagement.lioninventories;

import de.lioncraft.lionapi.LionAPI;
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
    private AddonManageMenu(){}
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
                    if (event.isLeftClick()) {addon.openConfigMenu((Player) event.getWhoClicked());}
                    if(!event.isRightClick()) return false;

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
                        LionChat.sendSystemMessage(LionAPI.lm().msg("inv.addons.error_while_enabling"), event.getWhoClicked());
                    }
                    return false;
                }
                );
                list.add(ref.b.getButton());
            });
            inv = new ScrollableInterface(list, LionAPI.lm().msg("inv.addons.manager.title"), null, true, null);
            inv.setBackButton(MainMenu.getToMainMenuButton());
        }
        if (player.isOp()){
            player.openInventory(inv.getInventory());
        }else player.playSound(Sounds.getSound(Sounds.Error));
    }
    public static ItemStack getItem(AbstractAddon addon){
        ItemStack is = addon.getSettingsIcon();
        if (is==null) return null;

        is = is.clone();
        List<Component> lore = is.lore();
        if (lore == null) lore = new ArrayList<>();
        if (addon.isEnabled()){
            lore.addAll(LionAPI.lm().getMessageAsList("inv.addons.description_enabled"));
        }else{
            lore.addAll(LionAPI.lm().getMessageAsList("inv.addons.description_disabled"));
        }
        is.lore(lore);

        return is;
    }

    public static ItemStack getItem(){
        return LionButtonFactory.createButton(Items.get(LionAPI.lm().msg("inv.addons.manager.title"),
                Material.AMETHYST_SHARD, LionAPI.lm().getMessageAsList("inv.addons.manager.lore")),
                "lionapi_open_addon_manager");
    }
}
