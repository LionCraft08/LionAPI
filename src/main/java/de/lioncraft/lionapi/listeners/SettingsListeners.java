package de.lioncraft.lionapi.listeners;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.addons.AddonManager;
import de.lioncraft.lionapi.events.saveDataEvent;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.playerSettings.PlayerSettings;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsListeners implements Listener {
    @EventHandler
    public void onChat(AsyncChatEvent e){
        if (!PlayerSettings.getSettings(e.getPlayer()).canChat()) {
            e.setCancelled(true);
            LionChat.sendSystemMessage(LionAPI.lm().msg("features.chat.disabled"), e.getPlayer());
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if (!PlayerSettings.getSettings(e.getPlayer()).canMove()) {
            if (e.hasChangedPosition()) {
                e.setTo(e.getFrom().setRotation(e.getTo().getYaw(), e.getTo().getPitch()));
                //e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if (!PlayerSettings.getSettings(e.getPlayer()).canMove()) {
            if (e.hasItem()){
                if (e.getItem().getType().equals(Material.CHORUS_FRUIT)||e.getItem().getType().equals(Material.ENDER_PEARL))
                    e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMove(BlockBreakEvent e){
        if (!PlayerSettings.getSettings(e.getPlayer()).canMineBlocks()) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onHit(PrePlayerAttackEntityEvent e){
        if (!PlayerSettings.getSettings(e.getPlayer()).canHitEntities()) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        PlayerSettings.getSettings(e.getPlayer()).update();
    }
    @EventHandler
    public void onInvClose(InventoryCloseEvent e){
        playerAddToSettingsInv.remove(e.getInventory());
    }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){PlayerSettings.getSettings(e.getPlayer()).update();}
    @EventHandler
    public void onGameMode(PlayerGameModeChangeEvent e){
        if (e.getNewGameMode().equals(GameMode.ADVENTURE)||e.getNewGameMode().equals(GameMode.SURVIVAL)) {
            PlayerSettings.getSettings(e.getPlayer()).update();
        }
    }
    @EventHandler
    public void onSave(saveDataEvent e){
        AddonManager.save();
        try {
            PlayerSettings.serializeAll();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
    private static final List<Inventory> playerAddToSettingsInv = new ArrayList<>();
    public static void addPlayerAddToSettingsInv(Inventory inventory){
        playerAddToSettingsInv.add(inventory);
    }

}
