package de.lioncraft.lionapi.hiddenclicks;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ClickCommand implements BasicCommand {
    public boolean onCommand(@NotNull CommandSender sender,  @NotNull String[] args) {
        if (args.length < 1) return true;
        else
        if(!args[0].isEmpty()){
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(String s : args){
                if(!first){
                    result.append(" ");
                }
                result.append(s);
                first = false;
            }
            if(HiddenKlick.registeredKlicks.get(result.toString()) != null){
                HiddenKlick.registeredKlicks.get(result.toString()).onClick(new HiddenKlick(args, sender));
            }
        }
        return true;
    }
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] strings) {
        onCommand(commandSourceStack.getSender(), strings );
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        return List.of();
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return true;
    }
}
