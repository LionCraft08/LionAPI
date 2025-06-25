package de.lioncraft.lionapi.hiddenclicks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ClickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
}
