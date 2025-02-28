package de.lioncraft.lionapi.teams;

import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface TeamClassFunction {
    void onRun(CommandSender sender, String[] args, Team team);
}
