package de.lioncraft.lionapi.guimanagement.lioninventories;

import de.lioncraft.lionapi.guimanagement.Interaction.Setting;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.MainMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public final class TimerManagementUI {
    private static Inventory inv;
    public static void open(HumanEntity player){
        if (inv == null){
            inv = Bukkit.createInventory(null, 54, Component.text("Timer Management"));
            inv.setContents(Items.blockButtons);
            inv.setItem(49, Items.closeButton);
            inv.setItem(45, MainMenu.getToMainMenuButton());

        }
        player.openInventory(inv);
    }

}
