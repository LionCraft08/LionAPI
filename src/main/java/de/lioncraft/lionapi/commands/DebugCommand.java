package de.lioncraft.lionapi.commands;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.pluginPlusAPI.ParticleDelayGenerator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DebugCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p){
            if (args.length>=4){
                new ParticleDelayGenerator(Integer.parseInt(args[2]), p, Double.parseDouble(args[3]), 100).runTaskTimer(LionAPI.getPlugin(), Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
