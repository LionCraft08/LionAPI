package de.lioncraft.lionapi.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.data.Settings;
import de.lioncraft.lionapi.messageHandling.DM;
import de.lioncraft.lionapi.messageHandling.MSG;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.teams.Team;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
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
    public static void register(Commands cmd){
        cmd.register(Commands.literal("teammessage")
                        .requires(commandSourceStack -> Settings.isAllowTeammsg() && (commandSourceStack.getSender() instanceof Player))
                        .executes(cc->{
                            if (!LionAPI.getPlugin().getConfig().getBoolean("settings.msg.allow-team-msg")) {
                                LionChat.sendTeamMSG(null,LionAPI.lm().msg("features.msg.disabled_team"), cc.getSource().getExecutor());
                            } else {
                                LionChat.sendTeamMSG(null, LionAPI.lm().msg("features.msg.explanation_team"), cc.getSource().getExecutor());
                            }

                            return 0;
                        })
                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                .executes(cc->{
                                    if (!LionAPI.getPlugin().getConfig().getBoolean("settings.msg.allow-team-msg")) {
                                        LionChat.sendTeamMSG(null,LionAPI.lm().msg("features.msg.disabled_team"), cc.getSource().getExecutor());
                                        return 1;
                                    }
                                    if (cc.getSource().getExecutor() instanceof Player p){
                                        Team t = Team.getTeam(p);
                                        if (t == null) LionChat.sendSystemMessage(MSG.NO_TEAM, p);
                                        else{
                                            t.getPlayers().forEach(member ->{
                                                if (p.isOnline()){
                                                    LionChat.sendTeamMSG(p.name(), Component.text(cc.getArgument("message", String.class)), member.getPlayer());
                                                }
                                            });

                                        }
                                    }else LionChat.sendSystemMessage(MSG.NOT_A_PLAYER, cc.getSource().getSender());
                                    return 0;
                                })).build() ,
                "Send a Message to your Team",
                List.of("tmsg", "teammsg"));
    }
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
