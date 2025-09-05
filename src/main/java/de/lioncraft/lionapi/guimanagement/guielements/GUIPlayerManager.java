package de.lioncraft.lionapi.guimanagement.guielements;

import org.bukkit.entity.Player;

import java.util.HashMap;

public final class GUIPlayerManager {
    private GUIPlayerManager(){}
    private static final HashMap<Player, ClientRenderWay> renderWay = new HashMap<>();

    public static void setRenderWay(Player p, ClientRenderWay w){
        renderWay.put(p, w);
    }

    public static ClientRenderWay getRenderWay(Player p){
        if (renderWay.containsKey(p)) {
            return renderWay.get(p);
        }
        return ClientRenderWay.VANILLA;
    }

    public enum ClientRenderWay{
        VANILLA,
        LIONDISPLAYS_MOD,
        RESOURCE_PACK
    }
}
