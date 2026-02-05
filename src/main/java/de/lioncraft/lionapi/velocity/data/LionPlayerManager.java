package de.lioncraft.lionapi.velocity.data;

import de.lioncraft.lionapi.events.velocity.PlayerDataRefreshEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.UUID;

@Deprecated(forRemoval = true)
public class LionPlayerManager {
    private static HashMap<UUID, PlayerData> map = new HashMap<>();

    @ApiStatus.Internal
    public static void addPlayerData(PlayerData playerData){
        map.put(playerData.getUuid(), playerData);
        OfflinePlayer p = Bukkit.getOfflinePlayer(playerData.getUuid());
        p.setOp(playerData.isOP());
    }

    public static PlayerData getPlayerData(UUID uuid){
        return map.get(uuid);
    }
}
