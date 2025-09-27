package de.lioncraft.lionapi.velocity;

import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.velocity.connections.AbstractConnection;
import de.lioncraft.lionapi.velocity.connections.ViaVelocityConnection;
import io.papermc.paper.connection.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class ProxyMessageListeners implements PluginMessageListener {
    private static String channel = "lionapi:backend_communication";

    public static void register(Plugin plugin){
        Messenger m = Bukkit.getServer().getMessenger();
        m.registerIncomingPluginChannel(plugin, channel, new ProxyMessageListeners());
        m.registerOutgoingPluginChannel(plugin, channel);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String s, @NotNull Player player, byte @NotNull [] bytes) {

    }

    @Override
    public void onPluginMessageReceived(@NotNull String s, @NotNull PlayerConnection player, byte @NotNull [] bytes) {
        if (s.equalsIgnoreCase(channel)){
            String message = new String(bytes);
            AbstractConnection a = CommunicationManager.getConnection(message.substring(0, message.indexOf(":")));
            if (a instanceof ViaVelocityConnection){
                //a.sendMessage(message.substring(message.indexOf(":"+1)));
            }
            else if (a != null){
                LionChat.sendDebugMessage("Received invalid Message for "+a.getName()+": This is not a Velocity Connection");
            }
            else{
                LionChat.sendDebugMessage("Received invalid Message for "+message.substring(0, message.indexOf(":"))+": Connection doesn't exist");
            }
        }
    }
}
