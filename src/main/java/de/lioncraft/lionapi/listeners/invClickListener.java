package de.lioncraft.lionapi.listeners;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.*;
import de.lioncraft.lionapi.guimanagement.Interaction.Button;
import de.lioncraft.lionapi.guimanagement.Interaction.Interactor;
import de.lioncraft.lionapi.guimanagement.Interaction.MultipleSelection;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class invClickListener implements Listener {
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            if(e.getCurrentItem().hasItemMeta()){
                if(e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(disabledClick)){
                    e.setCancelled(true);
                }
            }
            if (Setting.SettingList.get(e.getCurrentItem()) != null) {
                e.setCancelled(true);
                Setting s = Setting.SettingList.get(e.getCurrentItem());
                s.ClickAction();
                Objects.requireNonNull(e.getClickedInventory()).setItem(e.getSlot(), s.getBottomItem());
            }else if (de.lioncraft.lionapi.guimanagement.Interaction.Setting.SettingList.get(e.getCurrentItem()) != null) {
                e.setCancelled(true);
                de.lioncraft.lionapi.guimanagement.Interaction.Setting s = de.lioncraft.lionapi.guimanagement.Interaction.Setting.SettingList.get(e.getCurrentItem());
                s.ClickAction();
                Objects.requireNonNull(e.getClickedInventory()).setItem(e.getSlot(), s.getBottomItem());
            } else if (button.activeButtons.containsKey(e.getCurrentItem())) {
                e.setCancelled(true);
                button b = button.activeButtons.get(e.getCurrentItem());
                b.ClickAction(e);
            } else if (Button.activeButtons.containsKey(e.getCurrentItem())) {
                e.setCancelled(true);
                Button b = Button.activeButtons.get(e.getCurrentItem());
                b.ClickAction(e);
            } else if (multipleSelection.multipleSelectionMap.containsKey(e.getCurrentItem())) {
                e.setCancelled(true);
                multipleSelection ms = multipleSelection.multipleSelectionMap.get(e.getCurrentItem());
                ms.ClickAction(e);
                Objects.requireNonNull(e.getClickedInventory()).setItem(e.getSlot(), ms.getButton());
            } else if (MultipleSelection.multipleSelectionMap.containsKey(e.getCurrentItem())) {
                e.setCancelled(true);
                MultipleSelection ms = MultipleSelection.multipleSelectionMap.get(e.getCurrentItem());
                ms.ClickAction(e);
                Objects.requireNonNull(e.getClickedInventory()).setItem(e.getSlot(), ms.getButton());
            }else if(ScrollableInterface.activeInterfaces.containsKey(e.getClickedInventory())){
                ScrollableInterface sc = ScrollableInterface.activeInterfaces.get(e.getClickedInventory());
                e.setCancelled(true);
                if(e.getCurrentItem().equals(Items.scrollDownButton)){
                    sc.scroll(true, e.isShiftClick());
                    e.getWhoClicked().playSound(Sound.sound().type(Key.key("item.book.page_turn")).source(Sound.Source.NEUTRAL).pitch(1.0f).volume(0.7f).build());
                }else if(e.getCurrentItem().equals(Items.scrollUpButton)){
                    sc.scroll(false, e.isShiftClick());
                    e.getWhoClicked().playSound(Sound.sound().type(Key.key("item.book.page_turn")).source(Sound.Source.NEUTRAL).pitch(1.0f).volume(0.7f).build());
                }
            }
            if(e.getCurrentItem() != null){
                if(e.getCurrentItem().equals(Items.closeButton)){
                    e.getWhoClicked().closeInventory();
                }else if (e.getCurrentItem().isSimilar(Items.blockButton)){
                    e.setCancelled(true);
                }
            }
        }
    }
    private static List<ItemStack> interactions = new ArrayList<>();
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.hasItem()){
            if(e.getAction().isRightClick()) {
                for (ItemStack is : interactions) {
                    if (is.isSimilar(e.getItem())) {
                        if(Interactor.sendInteraction(e.getPlayer(), e.getItem().asQuantity(1))){
                            e.getItem().setAmount(e.getItem().getAmount() - 1);
                        }
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
    public static void addInteractionItem(ItemStack is){
        interactions.add(is);
    }
    private static final NamespacedKey disabledClick = new NamespacedKey(LionAPI.getPlugin(), "disabled-click-id");
    public static NamespacedKey getDisabledClick(){
        return disabledClick;
    }
}
