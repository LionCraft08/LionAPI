package de.lioncraft.lionapi.velocity;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.net.PortUnreachableException;
import java.util.HashMap;

public class CommunicationSettings {
    private final HashMap<String, Boolean> bool = new HashMap<>();
    private final HashMap<String, Integer> integer = new HashMap<>();
    private final HashMap<String, String> strings = new HashMap<>();
    public CommunicationSettings(FileConfiguration config){
        String s = "settings.velocity-communication.";
        bool.put("disabled", config.getBoolean(s+"disable-communication"));
        integer.put("port", config.getInt(s+"custom-port"));
        if (integer.get("port")>65535){
            LionChat.sendLogMessage(Component.text("The Port provided in config.yml/settings.velocity-communication.custom-port is invalid, changing to default"));
            integer.put("port", -1);
        }
    }
    public Integer getCustomPort(){
        if (integer.get("port")>65535){
            throw new RuntimeException("Port "+integer.get("port") + " is too high");
        }
        if (integer.get("port")<0){
            return null;
        }
        return integer.get("port");
    }

    public boolean usePortCommunication() {
        return getCustomPort() != null;
    }
}
