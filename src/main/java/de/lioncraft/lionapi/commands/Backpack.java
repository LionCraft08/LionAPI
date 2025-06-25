package de.lioncraft.lionapi.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import de.lioncraft.lionapi.messageHandling.DM;
import de.lioncraft.lionapi.messageHandling.MSG;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.teams.Team;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Backpack implements TabExecutor {
    public static void register(Commands cmd){
        cmd.register(Commands.literal("backpack")
                        .executes(cc -> {
                            if (cc.getSource().getExecutor() instanceof Player p) {
                                if(Team.getTeam(p) != null){
                                    Team t = Team.getTeam(p);
                                    t.getBackpack().openBackpack(p);
                                }else LionChat.sendSystemMessage(MSG.NO_TEAM, p);
                            }else LionChat.sendSystemMessage(MSG.notAPlayer, cc.getSource().getSender());
                            return 0;
                        })
                        .then(Commands.argument("team", StringArgumentType.string())
                                .executes(cc ->{
                                    if (cc.getSource().getExecutor() instanceof Player p) {
                                        if(Team.getTeam(cc.getArgument("team", String.class)) != null){
                                            Team t = Team.getTeam(p);
                                            t.getBackpack().openBackpack(p);
                                        }else LionChat.sendSystemMessage(MSG.NO_TEAM, p);
                                    }else LionChat.sendSystemMessage(MSG.notAPlayer, cc.getSource().getSender());
                                    return 0;
                                } ))
                .build(),
                "Open the Backpack of your team.",
                List.of("bp"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if(Team.getTeam(p) != null){
                Team t = Team.getTeam(p);
                t.getBackpack().openBackpack(p);
            }else sender.sendMessage(DM.messagePrefix.append(Component.text("You are not in a Team!")));
        }else sender.sendMessage(DM.notAPlayer);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
