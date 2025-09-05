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
import io.papermc.paper.registry.event.RegistryEvents;
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
            e.registrar().register(Commands.literal("timer")
                    .requires(s -> s.getSender().isOp())
                    .build(),
                    "Timer & Challenge Management");
            e.registrar().register(Commands.literal("teams")
                            .requires(s -> s.getSender().isOp())
                            .build(),
                    "Manage the Teams for the Challenge, does not affect the vanilla Teams.");
            e.registrar().register(Commands.literal("hiddenclickapi")
                            .build(),
                    "Internal use only, do NOT use");
        });
    }
}
