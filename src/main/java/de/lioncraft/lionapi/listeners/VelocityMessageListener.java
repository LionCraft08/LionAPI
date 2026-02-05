package de.lioncraft.lionapi.listeners;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.velocity.data.*;
import org.bukkit.Bukkit;

import java.util.UUID;

public final class VelocityMessageListener {
    public static void onReceive(TransferrableObject to){
        switch (to.getObjectType()){
            case "lionapi_playerdata" -> {
                PlayerConfiguration pc = PlayerConfiguration.fromJson(to.getString("data"));
                PlayerConfigCache.addPlayerConfig(pc.uuid, pc);
            }
            case "lionapi_playerdata_update" -> {
                PlayerConfiguration pc = PlayerConfigCache.getPlayerConfig(UUID.fromString(to.getString("player")));
                if (pc == null) return;
                switch (to.getString("key")){
                    case "isOperator" -> {
                        boolean b = to.getBool("data");
                        pc.isOperator = b;
                        Bukkit.getOfflinePlayer(pc.uuid).setOp(b);
                    }
                }
            }
            case "lionapi_shutdown" -> {
                LionAPI.getPlugin().getLogger().info("Attempting to shut down");
                Bukkit.getServer().shutdown();
            }
        }
    }
}
