package de.lioncraft.lionapi.hiddenclicks;

import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class HiddenKlick {
    static Map<String, KlickFunction> registeredKlicks;
    private String[] args;
    private CommandSender sender;

    /**Registers a new Hidden Click wich will run the Function-Parameter everytime the ClickEvent
     * in the returned {@link ClickEvent}
     * @param args a unique name, by whitespaces separated, will be present in the Function Parameter
     * @param onKlick the Function to be executed
     * @return a ClickEvent wich can be used in ChatMessages, Books etc.
     */
    public static @NotNull ClickEvent registerHiddenKlick(String args, KlickFunction onKlick){
        if (registeredKlicks == null) registeredKlicks = new HashMap<>();
        registeredKlicks.put(args, onKlick);
        return ClickEvent.runCommand("/lionsystems hiddenclickapi " + args);
    }

    HiddenKlick(String[] args, CommandSender sender) {
        this.args = args;
        this.sender = sender;
    }

    public String[] getArgs() {
        return args;
    }

    public CommandSender getSender() {
        return sender;
    }
}
