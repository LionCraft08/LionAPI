package de.lioncraft.lionapi.commands;

import de.lioncraft.lionapi.messageHandling.DM;
import de.lioncraft.lionapi.teams.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Teammsg implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0){
            sender.sendMessage(DM.info("Mit diesem Command kannst du eine Nachricht an dein Team senden."));
        }else{
            if(sender instanceof Player p){
                Team t = Team.getTeam(p);
                if(t != null){
                    Component c = Component.text("[Team] "+p.getName()+" >> ");
                    for(String s : args){
                        c = c.append(Component.text(s + " "));
                    }
                    c = c.color(TextColor.color(130, 130, 130));
                    c = c.hoverEvent(Component.text("/teammsg \n nur sichtbar für dein Team"));
                    t.sendMessage(c);
                }else sender.sendMessage(DM.error("Du bist in keinem Team."));
            }else sender.sendMessage(DM.notAPlayer);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
