package de.lioncraft.lionapi.guimanagement.lioninventories;

import de.lioncraft.lionapi.guimanagement.Interaction.Button;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.ScrollableInterface;
import de.lioncraft.lionapi.messageHandling.lionchat.ChannelConfiguration;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ChannelSelectionMenu {
    private static Button button;
    public static void open(Player p){
        List<ItemStack> items = new ArrayList<>();
        LionChat.getChannels().forEach((s, channelConfiguration) -> {
            items.add(channelConfiguration.getButton());
        });
        ScrollableInterface inv = new ScrollableInterface(items, Component.text("LionChat Channels", TextColor.color(0, 255, 255)), null, true, null);
        p.openInventory(inv.getInventory());

    }
    public static Button getButton(){
        if (button == null)
            button = new Button(Items.get("LionChat Settings", Material.GOAT_HORN, "Opens the configuration for the different Channels", "with the option to mute them."), event -> {
                open((Player) event.getWhoClicked());
                return false;
            });
        return button;
    }


}
