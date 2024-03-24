package de.lioncraft.lionapi.messageHandling;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public final class defaultMessages {
    public static Component messagePrefix;
    public static Component wrongArgs;
    public static Component noPlayer;
    public static Component commandError;
    public static Component noPermission;
    public static Component wait;
    public static Component notAPlayer;

    /**Sets the Messages in the Class
     *Do not use!
     **/
    public static void setValues(){
        messagePrefix = Component.text("<").append(Component.text("Lion", TextColor.color(255, 0, 255))).append(Component.text("Systems", TextColor.color(0, 255, 255))).append(Component.text("> "));
        wrongArgs = messagePrefix.append(Component.text("Wrong usage of args.", TextColor.color(255, 0, 0)));
        noPlayer = messagePrefix.append(Component.text("Could not find the given Player.", TextColor.color(255, 0, 0)));
        noPermission = messagePrefix.append(Component.text("You do not have the permission to do this.", TextColor.color(255, 0, 0)));
        commandError = messagePrefix.append(Component.text("An error occurred when trying to execute this command.", TextColor.color(255, 0, 0)));
        wait = messagePrefix.append(Component.text("Please wait a bit.", TextColor.color(255, 0, 0)));
        notAPlayer = messagePrefix.append(Component.text("This Command can only be executed as a Player!", TextColor.color(255, 0, 0)));

    }
}
