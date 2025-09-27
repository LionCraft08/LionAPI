package de.lioncraft.lionapi.listeners;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.velocity.data.TransferrableObject;
import org.bukkit.Bukkit;

public final class VelocityMessageListener {
    public static void onReceive(TransferrableObject to){
        switch (to.getObjectType()){
            case "lionapi_playerdata" -> {

            }
            case "lionapi_shutdown" -> {
                LionAPI.getPlugin().getLogger().info("Attempting to shut down");
                Bukkit.getServer().shutdown();
            }
        }
    }
}
