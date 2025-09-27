package de.lioncraft.lionapi.velocity;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.velocity.connections.AbstractConnection;
import de.lioncraft.lionapi.velocity.connections.DirectConnection;

import java.util.HashMap;

public class CommunicationManager {
    private static HashMap<String, AbstractConnection> queuedConnections = new HashMap<>();
    private static HashMap<String, AbstractConnection> connections = new HashMap<>();

    private static CommunicationSettings settings = new CommunicationSettings(LionAPI.getPlugin().getConfig());
    public static void registerConnection(DirectConnection c){
        LionChat.sendDebugMessage("Enabling Connection to "+c.getName()+" ("+c.getHost()+":"+c.getPort()+")");

    }

    public static CommunicationSettings getSettings() {
        return settings;
    }

    static AbstractConnection getConnection(String name){
        return connections.get(name);
    }

}
