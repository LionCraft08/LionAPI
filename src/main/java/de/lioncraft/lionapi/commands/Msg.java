package de.lioncraft.lionapi.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import de.lioncraft.lionapi.data.Settings;
import de.lioncraft.lionapi.messageHandling.MSG;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.teams.Team;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Msg {
    public static void register(Commands cmd){
        cmd.register(Commands.literal("msg")
                        .requires(commandSourceStack -> (commandSourceStack.getSender() instanceof Player))
                        .executes(cc->{
                            LionChat.sendMSG(null, Component.text("Mit diesem Command kannst du eine private Nachricht an einen Spieler senden."), cc.getSource().getExecutor());
                            return 0;
                        })
                        .then(Commands.argument("player", ArgumentTypes.players()).executes(cc->{
                                    LionChat.sendMSG(null, Component.text("Du musst eine Nachricht eingeben."), cc.getSource().getExecutor());
                                    return 0;
                                })
                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                .executes(cc->{
                                    Component source = cc.getSource().getExecutor().name();
                                    List<Player> list = cc.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(cc.getSource());
                                    if (list.isEmpty()) LionChat.sendMSG(null, MSG.noPlayer.getText(), cc.getSource().getSender());
                                    list.forEach(
                                        member ->{
                                            LionChat.sendMSG(member.name(), Component.text(cc.getArgument("message", String.class)), member.getPlayer());
                                            LionChat.sendLogMessage(Component.text("[MSG] ").append(source).append(Component.text("->"+cc.getInput().replaceFirst(" ", " >> "))));
                                        });
                                    return 0;
                                }))).build() ,
                "Send a Message to a Player",
                List.of("tell", "w", "message"));
    }
}
