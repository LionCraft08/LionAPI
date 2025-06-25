package de.lioncraft.lionapi.guimanagement.lioninventories;

import de.lioncraft.lionapi.guimanagement.Interaction.Button;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.MainMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class PluginSettingsMenu {
    private Inventory inv;
    private String previousInventory = null;
    private String name;
    private ItemStack icon;

    public PluginSettingsMenu(String name) {
        this.name = name;
        createInventory();
    }

    public PluginSettingsMenu(String name, String previousInventory) {
        this.previousInventory = previousInventory;
        this.name = name;
        createInventory();
    }
    protected abstract ItemStack getItemFor(int slot);

    private void createInventory(){
        inv = Bukkit.createInventory(null, 54, Component.text("Plugin-Settings "+name));
        inv.setContents(Items.blockButtons);
        inv.setItem(49, Items.closeButton);
        inv.setItem(45, new Button(Items.getBackButton(PluginManagementMenu.getTitle()), event -> {
            if (previousInventory == null) PluginManagementMenu.open(event.getWhoClicked());
            else PluginManagementMenu.openMenu(previousInventory, event.getWhoClicked());
            return false;
        }).getButton());
        for (int i = 10;i<54;i++){

        }
    }

    public String getName() {
        return name;
    }

    public void openMenu(HumanEntity player){
        player.openInventory(inv);
    }

    public ItemStack getIcon() {
        return icon;
    }

    public PluginSettingsMenu setIcon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public String getPreviousInventory() {
        return previousInventory;
    }

    public Inventory getInv() {return inv;}
}
