package de.lioncraft.lionapi.guimanagement;

import de.lioncraft.lionapi.guimanagement.Interaction.Button;
import de.lioncraft.lionapi.timer.MainTimer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class MainMenu {
    private static Inventory mainMenu;
    private static List<Button> buttons;
    private static Button toMainMenuButton;
    public static void Initialize(){
        mainMenu = Bukkit.createInventory(null, 54, Component.text("LionSystems", TextColor.color(0, 255, 255)));
        mainMenu.setContents(Items.blockButtons);
        mainMenu.setItem(49, Items.closeButton);
        buttons = new ArrayList<>();
        toMainMenuButton = new Button(Items.get(Component.text("Back", TextColor.color(0, 255, 255)), Material.SPECTRAL_ARROW, TextColor.color(255, 255, 255), "Back to: LionSystems"), event -> {
            event.getWhoClicked().openInventory(mainMenu);
        return false;});

    }

    /**Registers a new Inventory accessible directly through the Main Menu of LionAPI.
     * THis will create a new mutable Inventory and configure it.
     * @param buttonPreset An Item wich will be used to create the Button in the Main GUI.
     * @return The new created Inventory
     */
    public static Inventory registerNewInventoryPreset(ItemStack buttonPreset, boolean needsOP){
        Inventory inv = Bukkit.createInventory(null, 54, buttonPreset.displayName());
        inv.setContents(Items.blockButtons);
        inv.setItem(49, Items.closeButton);
        inv.setItem(45, toMainMenuButton.getButton());
        Button b = new Button(buttonPreset, event -> {
            if(needsOP && !event.getWhoClicked().isOp()){
                return false;
            }
            event.getWhoClicked().openInventory(inv);
            return false;});
        buttons.add(b);
        return inv;
    }
    public static void registerNewButton(Button b){
        buttons.add(b);
    }

    /** Used to get a Button wich will automatically open the Main Inventory.
     * Can be used as a Back button for new registered Inventories.
     * @return The Back-Button.
     */
    public static Button getToMainMenuButton() {
        return toMainMenuButton;
    }
}
