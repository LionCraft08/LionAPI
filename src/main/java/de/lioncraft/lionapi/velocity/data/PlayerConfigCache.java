package de.lioncraft.lionapi.velocity.data;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.events.velocity.PlayerDataReceiveEvent;
import de.lioncraft.lionapi.events.velocity.PlayerDataRefreshEvent;
import de.lioncraft.lionapi.velocity.connections.ConnectionManager;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PlayerConfigCache implements Listener {
    private static final Map<UUID, PlayerConfiguration> playerConfigCache = new HashMap<>();
    public static void addPlayerConfig(UUID uuid, PlayerConfiguration playerConfiguration) {
        boolean exists = false;
        if (playerConfigCache.containsKey(uuid)) {
            exists = playerConfigCache.get(uuid).timestamp != 0;
        }
        playerConfigCache.put(uuid, playerConfiguration);
        if (!exists) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getPluginManager().callEvent(new PlayerDataReceiveEvent(playerConfiguration));
                }
            }.runTask(LionAPI.getPlugin());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new PlayerDataRefreshEvent(playerConfiguration));
            }
        }.runTask(LionAPI.getPlugin());
    }
    public static void removePlayerConfig(UUID uuid) {
        playerConfigCache.remove(uuid);
    }
    public static PlayerConfiguration getPlayerConfig(UUID uuid) {
        return playerConfigCache.get(uuid);
    }
    public static @NotNull PlayerConfiguration getOrCreatePlayerConfig(UUID uuid) {
        if (!playerConfigCache.containsKey(uuid)) {
            playerConfigCache.put(uuid, new PlayerConfiguration(uuid));
            playerConfigCache.get(uuid).timestamp = 0;
        }
        return playerConfigCache.get(uuid);
    }

    public static void sendAndClearCachedPlayer(UUID uuid) {
        PlayerConfiguration playerConfiguration = playerConfigCache.get(uuid);
        if (playerConfiguration != null) {
            sendCachedPlayer(uuid);
            removePlayerConfig(uuid);
        }
    }

    public static void sendCachedPlayer(UUID uuid) {
        PlayerConfiguration playerConfiguration = playerConfigCache.get(uuid);
        if (playerConfiguration != null) {
            ConnectionManager.getConnectionToVelocity().sendMessage(new TransferrableObject("lionapi_playerdata")
                    .addValue("uuid", uuid.toString())
                    .addValue("data", playerConfiguration.toString()));
        }
    }

    public PlayerConfigCache() {
        st = Bukkit.getAsyncScheduler().runAtFixedRate(LionAPI.getPlugin(),
                scheduledTask -> {
            for (UUID player : playerConfigCache.keySet()) {
                sendCachedPlayer(player);
            }
                },
                1,
                1, TimeUnit.MINUTES);
    }

    private ScheduledTask st;

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        PlayerConfiguration pc = getPlayerConfig(e.getPlayer().getUniqueId());
        if(pc == null) return;
        pc.timestamp = System.currentTimeMillis();
        pc.isOperator = Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId()).isOp();
        sendAndClearCachedPlayer(e.getPlayer().getUniqueId());
    }


}
