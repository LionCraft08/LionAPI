package de.lioncraft.lionapi.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.lioncraft.lionapi.guimanagement.MainMenu;
import de.lioncraft.lionapi.guimanagement.lionclient.DisplayAttachment;
import de.lioncraft.lionapi.guimanagement.lionclient.DisplayManager;
import de.lioncraft.lionapi.hiddenclicks.ClickCommand;
import de.lioncraft.lionapi.messageHandling.ColorGradient;
import de.lioncraft.lionapi.messageHandling.MSG;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.permissions.LionAPIPermissions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class LionCommands {
    public static void register(Commands cmd){
        cmd.register(
                Commands.literal("lionsystems")
                        .requires(css -> css.getSender().hasPermission(LionAPIPermissions.ExecuteLS.getMcid()))
                        .executes(cc ->{
                            if(cc.getSource().getExecutor() instanceof Player p){
                                p.openInventory(MainMenu.getMainMenu());
                            }else LionChat.sendSystemMessage(MSG.NOT_A_PLAYER, cc.getSource().getSender());
                            return 0;
                        })
                        .then(Commands.argument("arguments", StringArgumentType.greedyString())
                                .executes(cc->{
                                    String s = cc.getArgument("arguments", String.class);
                                    if(s.startsWith("hiddenclickapi ")){
                                        s = s.substring(15);
                                        new ClickCommand().onCommand(cc.getSource().getSender(), s.split(" "));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    if (Objects.equals(cc.getArgument("arguments", String.class), "sendmessage")){
                                        DisplayManager.sendDisplayText((Player) cc.getSource().getSender(), DisplayAttachment.TOP_LEFT, "test",
                                                Component.text("Hallo, es ist Sonntag, "+ Bukkit.getServer().getTPS()[0]+" TPS und gutes Wetter!")
                                                        .append(ColorGradient.getNewGradiant("Ich mag ZÜÜÜÜGEEEE!!!!", TextColor.color(255, 128, 0), TextColor.color(255, 0, 0))));
                                    }
                                    if (Objects.equals(cc.getArgument("arguments", String.class), "senditem")){
                                        DisplayManager.sendDisplayItem((Player) cc.getSource().getSender(), "test", 9, 9, DisplayAttachment.TOP_LEFT,
                                                new ItemStack(Material.ARROW).asQuantity(10));
                                    }
                                    if (Objects.equals(cc.getArgument("arguments", String.class), "sendsquare")){
                                        DisplayManager.sendDisplaySquare((Player) cc.getSource().getSender(), "testsquare", 9, 9, DisplayAttachment.BOTTOM_RIGHT,
                                                0xFFFF00FF, 50, 10);
                                    }
                                    LionChat.sendSystemMessage(MSG.BETA, cc.getSource().getSender());
                                    return 0;
                                }))
                        .build(),
                "The Main functionality of LionSystems",
                List.of("ls")
        );
    }
}
