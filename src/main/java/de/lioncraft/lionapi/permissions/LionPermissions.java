package de.lioncraft.lionapi.permissions;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class LionPermissions {
    public static final String receiveDebugMessages = create("lionapi.chat.subscribe.debug");

    public static String create(String s){
        return create(s, PermissionDefault.OP);
    }
    public static String create(String s, PermissionDefault def){
        return create(s, def, null);
    }
    public static String create(String s, PermissionDefault def, String description){
        Permission p = new Permission(s);
        p.setDefault(def);
        p.setDescription(description);
        Bukkit.getPluginManager().addPermission(p);
        return s;
    }
}
