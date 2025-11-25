package de.lioncraft.lionapi.permissions;

import org.bukkit.permissions.PermissionDefault;

import static org.bukkit.permissions.PermissionDefault.*;

public enum LionAPIPermissions {
    ExecuteCode("lionapi.command.code.execute", OP, "Allows you to use the /code command. Enabling the command in config.yml is still required"),
    ExecuteTimer("lionapi.command.timer.execute", OP, "Allows you to use the /timer command"),
    ExecuteLS("lionapi.command.ls.execute", TRUE, "Some useful things..."),
    ExecuteTeam("lionapi.command.teams.execute", OP, "Allows you to use the /teams command")
    ;

    private final String mcid;
    private final PermissionDefault pmd;
    private final String description;
    LionAPIPermissions(String id, PermissionDefault pmdefault, String description){
        mcid = id;
        pmd = pmdefault;
        this.description = description;
    }

    public PermissionDefault getPmd() {
        return pmd;
    }

    public String getMcid() {
        return mcid;
    }

    public String getDescription() {
        return description;
    }
}
