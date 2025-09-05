package de.lioncraft.lionapi.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.StringArgumentType;
import de.lioncraft.lionapi.messageHandling.MSG;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;

public class CodeCommand {
    public static void register(Commands cmd){
        cmd.register(Commands.literal("code")
                .executes(commandContext -> {
                    LionChat.sendSystemMessage(MSG.WRONG_ARGS, commandContext.getSource().getSender());
                    return 0;
                })
                .then(Commands.argument("ExecutionType", StringArgumentType.word())
                        .suggests((commandContext, suggestionsBuilder) -> {
                            suggestionsBuilder.suggest("execute");
                            suggestionsBuilder.suggest("load", MessageComponentSerializer.message().serialize(Component.text("Loads Code from txt files")))
                        }))

                .build());
    }
}
