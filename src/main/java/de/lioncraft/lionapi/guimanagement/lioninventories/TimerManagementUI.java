package de.lioncraft.lionapi.guimanagement.lioninventories;

import de.lioncraft.lionapi.guimanagement.Interaction.*;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.MainMenu;
import de.lioncraft.lionapi.timer.TimerConfig;
import de.lioncraft.lionapi.timer.TimerLike;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public final class TimerManagementUI {
    private static Inventory inv;
    public static void open(HumanEntity player){
        if (inv == null){
            inv = Bukkit.createInventory(null, 54, Component.text("Timer Management"));
            inv.setContents(Items.blockButtons);
            inv.setItem(49, Items.closeButton);
            inv.setItem(45, MainMenu.getToMainMenuButton());
            MultipleStringSelection mss = new MultipleStringSelection(
                    Items.get(Component.text("When Paused"),
                            Material.GOAT_HORN, "<white>When paused, display: "),
                    new ArrayList<>(List.of("[OFF] Display nothing",
                            "[TIMER] the current state of the timer",
                            "[MESSAGE] a Timer Paused message")),
                    (currentState, currentString, event) -> {
                        String s = currentString.substring(currentString.indexOf("[") + 1, currentString.indexOf("]"));
                        TimerConfig.getSettings().setStringValue("when-paused", s);
                    }
            );
            mss.setCurrentString(switch (TimerConfig.getWhenPaused().toUpperCase()){
                case "TIMER" -> 1;
                case "MESSAGE" -> 2;
                default -> 0;
            });
            MultipleStringSelection timerColor = new MultipleStringSelection(
                    Items.get(Component.text("Timer color", TextColor.color(0, 255, 255)),
                            Material.RED_DYE,
                            "<white>Set the timer color: "),
                    TimerLike.getTimerColors(),
                    (currentState, currentString, event) -> {
                        TimerConfig.getSettings().setStringValue("selected-preset", currentString);
                    });
            timerColor.setCurrentString(TimerLike.getTimerColors().indexOf(TimerConfig.getColorTheme()));
            inv.setItem(10, mss.getItem());
            inv.setItem(12, timerColor.getItem());
            Setting playerDeath = new Setting(TimerConfig.getTimerEndsOnPlayerDeath(),
                    Items.get(Component.text("Timer ends on player death"), Material.PLAYER_HEAD, "<white>Whether the timer should be paused",
                            "<white>when a player dies."),
                    TimerConfig::setTimerEndsOnPlayerDeath);
            inv.setItem(14, playerDeath.getTopItem());
            inv.setItem(23, playerDeath.getBottomItem());
            Setting dragonDeath = new Setting(TimerConfig.getTimerEndsOnDragonDeath(),
                    Items.get(Component.text("Timer ends on dragon death"), Material.DRAGON_HEAD, "<white>Whether the timer should be paused",
                            "<white>when the ender dragon dies."),
                    TimerConfig::setTimerEndsOnDragonDeath);
            inv.setItem(16, dragonDeath.getTopItem());
            inv.setItem(25, dragonDeath.getBottomItem());
        }
        player.openInventory(inv);
    }

}
