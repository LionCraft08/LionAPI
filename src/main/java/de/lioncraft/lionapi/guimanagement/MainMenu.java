package de.lioncraft.lionapi.guimanagement;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.Interaction.Button;
import de.lioncraft.lionapi.guimanagement.Interaction.LionButtonFactory;
import de.lioncraft.lionapi.guimanagement.lioninventories.AddonManageMenu;
import de.lioncraft.lionapi.guimanagement.lioninventories.ChannelSelectionMenu;
import de.lioncraft.lionapi.timer.MainTimer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static de.lioncraft.lionapi.timer.MainTimer.openUI;

public final class MainMenu {
    private MainMenu(){}
    private static Inventory mainMenu = Initialize();
    private static ItemStack toMainMenuButton = LionButtonFactory.createButton(Items.get(
            Component.text("Back", TextColor.color(0, 255, 255)),
            Material.SPECTRAL_ARROW,
            TextColor.color(255, 255, 255),
            "Back to: LionSystems"),
            "lionapi_open_main_menu");

    public static Inventory Initialize(){
        Inventory mainMenu = Bukkit.createInventory(null, 54, Component.text("LionSystems", TextColor.color(0, 255, 255)));
        mainMenu.setContents(Items.blockButtons);
        mainMenu.setItem(49, Items.closeButton);
        mainMenu.setItem(10, LionButtonFactory.createButton(Items.get(Component.text("Timer", TextColor.color(255, 128, 0)), Material.CLOCK, TextColor.color(255, 255, 255), "Configure the Timer"),
                "lionapi_open_timer_menu"));
        mainMenu.setItem(12, ChannelSelectionMenu.getButton());
        mainMenu.setItem(37, AddonManageMenu.getItem());
        mainMenu.setItem(39, LionButtonFactory.createButton(
                Items.get(Component.text("Challenge Controller", TextColor.color(255, 128, 0)),
                        Material.DIAMOND_SWORD,
                        LionAPI.lm().msg("inv.challenge-controller.open_gui")),
                "lionapi_open_challenge-controller_menu"));
        return mainMenu;
    }

    /**Registers a new Inventory accessible directly through the Main Menu of LionAPI.
     * This will create a new mutable Inventory and configure it.
     * @ An Item which will be used to create the Button in the Main GUI.
     * @return The newly created Inventory
     */
//    public static Inventory registerNewInventoryPreset(ItemStack buttonPreset, boolean needsOP){
//        Inventory inv = Bukkit.createInventory(null, 54, buttonPreset.displayName());
//        inv.setContents(Items.blockButtons);
//        inv.setItem(49, Items.closeButton);
//        inv.setItem(45, toMainMenuButton.getButton());
//        Button b = new Button(buttonPreset, event -> {
//            if(needsOP && !event.getWhoClicked().isOp()){
//                return false;
//            }
//            event.getWhoClicked().openInventory(inv);
//            return false;});
//        buttons.add(b);
//        return inv;
//    }
    public static void setButton(int slot, ItemStack b){
        mainMenu.setItem(slot, b);
    }

    /** Used to get a Button wich will automatically open the Main Inventory.
     * Can be used as a Back button for new registered Inventories.
     * @return The Back-Button.
     */
    public static ItemStack getToMainMenuButton() {
        return toMainMenuButton;
    }

    public static Inventory getMainMenu() {
        return mainMenu;
    }
}
