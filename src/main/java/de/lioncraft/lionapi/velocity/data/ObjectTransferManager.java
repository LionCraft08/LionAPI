package de.lioncraft.lionapi.velocity.data;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public final class ObjectTransferManager implements PluginMessageListener {
    private static String channel = "lionapi:objecttransfer";

    private ObjectTransferManager(){}

    public static void register(Plugin p){
        Messenger m = p.getServer().getMessenger();
        m.registerIncomingPluginChannel(p, channel, new ObjectTransferManager());
        m.registerOutgoingPluginChannel(p, channel);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String s, @NotNull Player player, byte @NotNull [] bytes) {

    }
}
