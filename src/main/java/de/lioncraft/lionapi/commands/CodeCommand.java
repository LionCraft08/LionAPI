package de.lioncraft.lionapi.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.StringArgumentType;
import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.features.customcode.CodeExecutor;
import de.lioncraft.lionapi.messageHandling.MSG;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.permissions.LionAPIPermissions;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;

public class CodeCommand {
    public static void register(Commands cmd){
        cmd.register(Commands.literal("code")
                .requires(commandSourceStack -> commandSourceStack.getSender().hasPermission(
                        LionAPIPermissions.ExecuteCode.getMcid()
                ))
                .executes(commandContext -> {
                    LionChat.sendSystemMessage(MSG.WRONG_ARGS, commandContext.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("execute")
                        .then(Commands.argument("code", StringArgumentType.string())
                                .executes(context -> {
                                    if (!LionAPI.getPlugin().getConfig().getBoolean("settings.allow-code-execution")){
                                        LionChat.sendSystemMessage(LionAPI.lm().msg("features.code.error.disabled"), context.getSource().getSender());
                                        return 0;
                                    }
                                    String code = context.getArgument("code", String.class);
                                    try {
                                        CodeExecutor.compileAndExecute(null,
                                                CodeExecutor.prepareCodeString(
                                                code, CodeExecutor.CodePrepareOptions.ADD_FUNCTION
                                        ));
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                        .then(Commands.literal("executeAsync")
                                .then(Commands.argument("code", StringArgumentType.string())
                                        .executes(context -> {
                                            if (!LionAPI.getPlugin().getConfig().getBoolean("settings.allow-code-execution")){
                                                LionChat.sendSystemMessage(LionAPI.lm().msg("features.code.error.disabled"), context.getSource().getSender());
                                                return 0;
                                            }
                                            String code = context.getArgument("code", String.class);
                                            try {
                                                CodeExecutor.compileAndExecuteAsync(code);
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            return 1;
                                        }))
                        )
                        .then(Commands.literal("load"))


                .build());
    }
}
