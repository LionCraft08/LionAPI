package de.lioncraft.lionapi.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.lioncraft.lionapi.guimanagement.MainMenu;
import de.lioncraft.lionapi.messageHandling.MSG;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Predicate;

public class LionCommands {
    public static void register(Commands cmd){
        cmd.register(
                Commands.literal("lionsystems")
                        .executes(cc ->{
                            if(cc.getSource().getExecutor() instanceof Player p){
                                p.openInventory(MainMenu.getMainMenu());
                            }else LionChat.sendSystemMessage(MSG.notAPlayer, cc.getSource().getSender());
                            return 0;
                        })
                        .then(Commands.argument("arguments", StringArgumentType.word())
                                .executes(cc->{
                                    LionChat.sendSystemMessage(MSG.BETA, cc.getSource().getSender());
                                    return 0;
                                }))
                        .build(),
                "The Main functionality of LionSystems",
                List.of("ls")
        );
    }
}
