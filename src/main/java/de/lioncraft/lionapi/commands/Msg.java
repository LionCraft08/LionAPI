package de.lioncraft.lionapi.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.data.Settings;
import de.lioncraft.lionapi.messageHandling.MSG;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.teams.Team;
import de.lioncraft.lionapi.velocity.connections.ConnectionManager;
import de.lioncraft.lionapi.velocity.data.TransferrableObject;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static de.lioncraft.lionapi.LionAPI.lm;

public class Msg {
    public static void register(Commands cmd){
        cmd.register(Commands.literal("msg")
                        .executes(cc->{
                            LionChat.sendMSG(null, lm().msg("features.msg.explanation"), cc.getSource().getExecutor());
                            return 0;
                        })
                        .then(Commands.argument("player", ArgumentTypes.players()).executes(cc->{
                                    LionChat.sendMSG(null, MSG.WRONG_ARGS.getText(), cc.getSource().getExecutor());
                                    return 0;
                                })
                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                .executes(cc->{
                                    if (!LionAPI.getPlugin().getConfig().getBoolean("settings.msg.allow-msg")) {
                                        LionChat.sendMSG(null,LionAPI.lm().msg("features.msg.disabled"), cc.getSource().getExecutor());
                                        return 1;
                                    }
                                    Component source;
                                    CommandSender sender;
                                    String senderid;
                                    if (cc.getSource().getExecutor() != null){
                                        source = cc.getSource().getExecutor().name().clickEvent(ClickEvent.suggestCommand("/msg " + cc.getSource().getExecutor().getName()));
                                        senderid = cc.getSource().getExecutor().getUniqueId().toString();
                                        sender = cc.getSource().getExecutor();
                                    }else{
                                        source = cc.getSource().getSender().name().clickEvent(ClickEvent.suggestCommand("/msg " + cc.getSource().getSender().getName()));
                                        sender = cc.getSource().getSender();
                                        if (cc.getSource().getSender() instanceof Player p){
                                            senderid = p.getUniqueId().toString();
                                        }else senderid = "UUID";
                                    }

                                    List<Player> list = cc.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(cc.getSource());
                                    if (list.isEmpty()) {
                                        if(ConnectionManager.isConnectedToVelocity()){
                                            ConnectionManager.getConnectionToVelocity().sendMessage(
                                                    new TransferrableObject("lionapi_msg")
                                                            .addValue("source", JSONComponentSerializer.json().serialize(source))
                                                            .addValue("message", JSONComponentSerializer.json().serialize(Component.text(cc.getArgument("message", String.class))))
                                                            .addValue("target", cc.getInput().split(" ")[1])
                                                            .addValue("sourcePlayer", senderid));
                                        }else LionChat.sendMSG(null, MSG.NO_PLAYER.getText(), cc.getSource().getSender());
                                    }
                                    list.forEach(
                                        member -> {
                                            Component target = member.name().clickEvent(ClickEvent.suggestCommand("/msg "+member.getName() + " "));
                                            LionChat.sendMSG(source, Component.text(cc.getArgument("message", String.class)), member);
                                            LionChat.sendMSG(LionAPI.lm().msg("features.msg.you").append(Component.text(" -> ")).append(target), Component.text(cc.getArgument("message", String.class)), sender);
                                            if(LionAPI.getPlugin().getConfig().getBoolean("settings.msg.log-msg"))
                                                LionChat.sendLogMessage(Component.text("[MSG] ").append(source).append(Component.text(" -> "+cc.getInput().replaceFirst("msg ", "").replaceFirst(" ", " >> "))));
                                        });
                                    return 0;
                                }))).build() ,
                "Send a Message to a Player",
                List.of("tell", "w", "message"));
    }
}
