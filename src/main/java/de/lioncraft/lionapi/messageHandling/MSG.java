package de.lioncraft.lionapi.messageHandling;

import de.lioncraft.lionapi.LionAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public enum MSG{
    BETA(LionAPI.lm().msg("general.error.no_beta_environment")),
    WRONG_ARGS(LionAPI.lm().msg("command.error.wrong_args")),
    @Deprecated(since = "1.10.4", forRemoval = true) noPlayer (LionAPI.lm().msg("command.error.no_player")),
    NO_PLAYER (LionAPI.lm().msg("command.error.no_player")),
    @Deprecated(since = "1.10.4", forRemoval = true) commandError (LionAPI.lm().msg("command.error.error")),
    COMMAND_ERROR (LionAPI.lm().msg("command.error.error")),
    @Deprecated(since = "1.10.4", forRemoval = true) noPermission (LionAPI.lm().msg("command.error.no_permission")),
    NO_PERMISSION (LionAPI.lm().msg("command.error.no_permission")),
    @Deprecated(since = "1.10.4", forRemoval = true) wait (LionAPI.lm().msg("command.error.wait")),
    WAIT (LionAPI.lm().msg("command.error.wait")),
    NO_TEAM(LionAPI.lm().msg("command.error.no_team")),
    @Deprecated(since = "1.10.4", forRemoval = true) notAPlayer (LionAPI.lm().msg("command.error.not_a_player")),
    NOT_A_PLAYER (LionAPI.lm().msg("command.error.not_a_player"));


    private Component text;
    MSG(@NotNull Component text) {
        this.text = text;
    }

    public Component getText() {
        return text;
    }
}
