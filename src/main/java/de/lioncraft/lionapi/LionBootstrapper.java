package de.lioncraft.lionapi;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.lioncraft.lionapi.commands.Backpack;
import de.lioncraft.lionapi.commands.LionCommands;
import de.lioncraft.lionapi.commands.Msg;
import de.lioncraft.lionapi.commands.Teammsg;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.CommandSender;

import java.util.List;

public class LionBootstrapper implements PluginBootstrap {
    @Override
    public void bootstrap(BootstrapContext bootstrapContext) {
        bootstrapContext.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, e -> {
            LionCommands.register(e.registrar());
            Backpack.register(e.registrar());
            Teammsg.register(e.registrar());
            Msg.register(e.registrar());


            final LiteralArgumentBuilder<CommandSourceStack> myCommand = Commands.literal("mybootcommand")
                    .requires(source -> source.getSender().hasPermission("myplugin.mybootcommand"))
                    .executes(commandContext -> {
                        CommandSender sender = commandContext.getSource().getSender();
                        sender.sendMessage("This command was registered during bootstrap!");
                        return 1;
                    });

            // Get the registrar from the event and register your command
            e.registrar().register(myCommand.build(), "The Main Functionality of LionSystems",
                    List.of());
        });
    }
}
