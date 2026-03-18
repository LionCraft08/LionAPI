package de.lioncraft.lionapi.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import de.lioncraft.lionapi.messageHandling.MSG;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.permissions.LionAPIPermissions;
import de.lioncraft.lionapi.teams.Team;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class Backpack implements TabExecutor {
    public static void register(Commands cmd){
        cmd.register(Commands.literal("backpack")
                        .requires(commandSourceStack ->commandSourceStack.getSender().hasPermission(LionAPIPermissions.OpenOwnBackpack.getMcid()))
                                        .executes(cc -> {
                            if (cc.getSource().getExecutor() instanceof Player p) {
                                if(Team.getTeam(p) != null){
                                    Team t = Team.getTeam(p);
                                    t.getBackpack().openBackpack(p);
                                }else LionChat.sendSystemMessage(MSG.NO_TEAM, p);
                            }else LionChat.sendSystemMessage(MSG.NOT_A_PLAYER, cc.getSource().getSender());
                            return 0;
                        })
                        .then(Commands.argument("team", StringArgumentType.string())
                                .requires(commandSourceStack -> commandSourceStack.getSender().isOp() || commandSourceStack.getSender().hasPermission(LionAPIPermissions.OpenOtherBackpacks.getMcid()))
                                .executes(cc ->{
                                    if (cc.getSource().getExecutor() instanceof Player p) {
                                        Team t = Team.getTeam(cc.getArgument("team", String.class));
                                        if(t != null){
                                            t.getBackpack().openBackpack(p);
                                        }else LionChat.sendSystemMessage(MSG.NO_TEAM, p);
                                    }else LionChat.sendSystemMessage(MSG.NOT_A_PLAYER, cc.getSource().getSender());
                                    return 0;
                                } ))
                .build(),
                "Open the Backpack of your team.",
                List.of("bp"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NonNull [] args) {
        if (sender instanceof Player p) {
            if(Team.getTeam(p) != null){
                Team t = Team.getTeam(p);
                t.getBackpack().openBackpack(p);
            }else LionChat.sendSystemMessage(MSG.NO_TEAM, sender);
        }else LionChat.sendSystemMessage(MSG.NOT_A_PLAYER, sender);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NonNull [] args) {
        return List.of();
    }
}
