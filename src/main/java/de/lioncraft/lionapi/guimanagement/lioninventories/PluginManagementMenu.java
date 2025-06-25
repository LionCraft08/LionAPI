package de.lioncraft.lionapi.guimanagement.lioninventories;

import de.lioncraft.lionapi.guimanagement.Interaction.Button;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.MainMenu;
import de.lioncraft.lionapi.guimanagement.ScrollableInterface;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PluginManagementMenu {
    private static HashMap<String, PluginSettingsMenu> pluginInventories;
    public static void openMenu(String plugin, HumanEntity user){
        if (pluginInventories.containsKey(plugin)){
            user.openInventory(pluginInventories.get(plugin).getInv());
        }else LionChat.sendMessageOnChannel("system", Component.text("This Plugin seems to be not loaded correctly. Check the console for more Details"), user);
    }
    public static void registerNewPluginSettingsPage(String name, PluginSettingsMenu menu){
        if (pluginInventories.containsKey(name)) LionChat.sendLogMessage("A Plugin Settings Page called "+name+" was registered twice, so the first one is being overwritten.");
        pluginInventories.put(name, menu);
    }
    private static final Component title = Component.text("LionPlugin Management");
    public static void open(HumanEntity user){
        List<ItemStack> list = new ArrayList<>();
        pluginInventories.forEach((s, inventory) -> {
            Button b = new Button(inventory.getIcon(), event -> {
                inventory.openMenu(event.getWhoClicked());
                return true;
            });
            list.add(b.getButton());
        });
        ScrollableInterface sc = new ScrollableInterface(list, title, null, true,
                Items.get("LionPlugins", Material.REDSTONE, "Allows you to manage every loaded LionSystems Plugin"));
        user.openInventory(sc.getInventory());
    }

    public static Component getTitle() {
        return title;
    }

}
