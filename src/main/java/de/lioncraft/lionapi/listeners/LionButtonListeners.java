package de.lioncraft.lionapi.listeners;

import de.lioncraft.lionapi.addons.AddonManager;
import de.lioncraft.lionapi.events.invs.LionButtonClickEvent;
import de.lioncraft.lionapi.guimanagement.Interaction.MultipleStringSelection;
import de.lioncraft.lionapi.guimanagement.MainMenu;
import de.lioncraft.lionapi.guimanagement.lioninventories.AddonManageMenu;
import de.lioncraft.lionapi.guimanagement.lioninventories.ChannelSelectionMenu;
import de.lioncraft.lionapi.messageHandling.lionchat.ChannelConfiguration;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.timer.MainTimer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class LionButtonListeners implements Listener {
    @EventHandler
    public void onClick(LionButtonClickEvent e){
        switch (e.getID()){
            case "lionapi_open_main_menu" ->
                e.getPlayer().openInventory(MainMenu.getMainMenu());
            case "lionapi_open_channel_menu" ->{
                if (e.getData().isBlank()){
                    ChannelSelectionMenu.open(e.getPlayer());
                }else{
                    LionChat.getChannels().get(e.getData()).openInventoryFor(e.getPlayer());
                }
            }
            case "lionapi_channel_change_icon" ->{
                ItemStack is = e.e().getView().getCursor();
                if (is.getType().isAir()){
                    LionChat.sendMessageOnChannel("system", Component.text("You need an Item on your Cursor to change the Icon."), e.getPlayer());
                    e.getPlayer().playSound(Sound.sound(Key.key("entity.villager.no"), Sound.Source.PLAYER, 1.0f, 1.0f));
                }else{
                    ChannelConfiguration c = LionChat.getChannels().get(e.getData());
                    c.setIcon(is.getType());
                    e.getPlayer().playSound(Sound.sound(Key.key("block.amethyst.hit"), Sound.Source.PLAYER, 1.0f, 1.0f));
                    e.e().setCurrentItem(c.getChangeIconButton());
                }
            }
            case "lionapi_open_timer_menu"-> {
                LionChat.sendMessageOnChannel("timer", Component.text("Dieses Menu ist nur in Beta-Environments verfügbar. Allgemeine Einstellungen sind über den AddonManager zu erreichen."));
            }
            case "lionapi_open_addon_manager" -> AddonManageMenu.open(e.getPlayer());
            case "lionapi_multiplestringselection" -> MultipleStringSelection.click(e.getData(), e.e());
        }
    }
}
