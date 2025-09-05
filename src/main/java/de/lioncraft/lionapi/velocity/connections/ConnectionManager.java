package de.lioncraft.lionapi.velocity.connections;

import de.lioncraft.lionapi.LionAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public final class ConnectionManager {
    private ConnectionManager(){}
    private static AbstractConnection connectionToVelocity;
    private static final HashMap<String, AbstractConnection> connections = new HashMap<>();
    public static void initialize(FileConfiguration fc){
        String s = "server-connection.";
        connectionToVelocity = createConnection(fc, "velocity");
    }

    private static AbstractConnection createConnection(FileConfiguration fc, String servername){
        String s = "server-connection."+servername+".";
        String type = fc.getString("type");
        int port = fc.getInt("port");
        String ip = fc.getString("ip");
        boolean asServer = fc.getBoolean("server");
        AbstractConnection ac = null;
        switch (type){
            case "blocked" -> {
                return null;
            }
            case "direct" -> {
                ac = createDirectConnection(ip, servername, port, asServer);
            }
            case "player" ->{
                ac = createProxyConnection(servername);
            }
            case "auto" -> {
                if (isValidPort(port)&&isValidIPv4(ip)){
                    ac = createDirectConnection(ip, servername, port, asServer);
                }else ac = createProxyConnection(servername);
            }
        }
        AbstractConnection finalAc = ac;
        Bukkit.getScheduler().runTaskAsynchronously(LionAPI.getPlugin(), () -> {
            finalAc.enableConnection();
        });
        return ac;
    }
    private static final Pattern IPV4_PATTERN = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$");
    public static boolean isValidIPv4(String ip) {
        return IPV4_PATTERN.matcher(ip).matches();
    }
    private static boolean isValidPort(int port) {
        // Valid port range: 0 to 65535
        return port >= 0 && port <= 65535;
    }
    public static ViaVelocityConnection createProxyConnection(String name){
        //TODO!!!
        return new ViaVelocityConnection(name);
    }
    private static DirectConnection createDirectConnection(String ip, String servername, int port, boolean server){
        return new DirectConnection(ip, port, servername, server);
    }
}
