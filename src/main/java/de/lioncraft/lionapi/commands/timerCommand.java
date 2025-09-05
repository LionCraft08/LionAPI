package de.lioncraft.lionapi.commands;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.challenge.ChallengeController;
import de.lioncraft.lionapi.data.ChallengeSettings;
import de.lioncraft.lionapi.messageHandling.DM;
import de.lioncraft.lionapi.messageHandling.MSG;
import de.lioncraft.lionapi.messageHandling.defaultMessages;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.timer.MainTimer;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class timerCommand implements BasicCommand {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        switch (args.length){
                case 0:
                    if(sender instanceof Player p){
                        MainTimer.openUI(p);
                    }else LionChat.sendSystemMessage(MSG.notAPlayer, sender);;
                break;
                case 1:
                    switch (args[0]) {
                        case "start", "resume" -> {
                            LionChat.sendSystemMessage(MainTimer.getTimer().start(), sender);
                        }
                        case "pause" -> {
                            if (MainTimer.getTimer().isActive()) {
                                MainTimer.getTimer().pause();
                                LionChat.sendSystemMessage(Component.text("Successfully paused the Timer"), sender);
                            } else {
                                LionChat.sendSystemMessage(Component.text("The Timer is already paused!"), sender);
                            }
                        }
                        case "reset" -> {
                            if (!MainTimer.isCountUpwards()) {
                                int days = MainTimer.getTimer().getSecondsAtStart() / (3600 * 24);
                                int hours = (MainTimer.getTimer().getSecondsAtStart() / 3600) - (days * 24);
                                int minutes = MainTimer.getTimer().getSecondsAtStart() / (60) - (days * 24 + hours) * 60;
                                int seconds = MainTimer.getTimer().getSecondsAtStart() - ((days * 24 + hours) * 60 + minutes) * 60;
                                MainTimer.getTimer().setDays(days);
                                MainTimer.getTimer().setHours(hours);
                                MainTimer.getTimer().setMinutes(minutes);
                                MainTimer.getTimer().setSeconds(seconds);
                            }else {
                                MainTimer.getTimer().setDays(0);
                                MainTimer.getTimer().setHours(0);
                                MainTimer.getTimer().setMinutes(0);
                                MainTimer.getTimer().setSeconds(0);
                            }

                            LionChat.sendSystemMessage(Component.text("Successfully restarted the Timer"),sender);
                        }
                        case "toggledirection" -> {
                            MainTimer.changeDirection();
                            if(MainTimer.isCountUpwards()) LionChat.sendSystemMessage(Component.text("The timer now counts upwards"),sender);
                            else LionChat.sendSystemMessage(Component.text("The timer now counts down"),sender);

                        }
                        default -> LionChat.sendSystemMessage(MSG.WRONG_ARGS, sender);
                    }
                    break;
                case 5:
                    if(args[0].equals("start")){
                        try {
                            MainTimer.getTimer().setDays(Integer.parseInt(args[1]));
                            MainTimer.getTimer().setHours(Integer.parseInt(args[2]));
                            MainTimer.getTimer().setMinutes(Integer.parseInt(args[3]));
                            MainTimer.getTimer().setSeconds(Integer.parseInt(args[4]));
                            MainTimer.getTimer().updateSecondsAtStart();
                            LionChat.sendSystemMessage(MainTimer.getTimer().start(), sender);
                        }catch (NumberFormatException e){
                            LionChat.sendSystemMessage(Component.text("Make sure you use numbers as parameters 2-5"),sender);
                        }
                    } else if (args[0].equals("set")) {
                        try {
                            MainTimer.getTimer().setDays(Integer.parseInt(args[1]));
                            MainTimer.getTimer().setHours(Integer.parseInt(args[2]));
                            MainTimer.getTimer().setMinutes(Integer.parseInt(args[3]));
                            MainTimer.getTimer().setSeconds(Integer.parseInt(args[4]));
                            if(!MainTimer.getTimer().isActive()){
                                MainTimer.getTimer().updateSecondsAtStart();
                            }
                            LionChat.sendSystemMessage(Component.text("Successfully set the Timer to " + MainTimer.getTimer().getCurrentSeconds() + "s"),sender);
                        }catch (NumberFormatException e){
                            LionChat.sendSystemMessage(Component.text("Make sure you use numbers as parameters 2-5"),sender);
                        }
                    }else{
                        LionChat.sendSystemMessage(MSG.WRONG_ARGS, sender);
                    }
                    break;
                case 3:
                    if(args[0].equals("settings")){
                        switch (args[1]){
                            case "stopbydragon" -> {
                                boolean b = Boolean.parseBoolean(args[2]);
                                if(b){
                                    ChallengeController.getInstance().getSettings().setChallenge(true);
                                }
                                ChallengeController.getInstance().getSettings().setChallengeEndsOnDragonDeath(b);
                                LionChat.sendSystemMessage(Component.text("Challenge-Einstellung geändert: "+b), sender);
                            }
                            case "stopbyplayerdeath" ->{
                                boolean b = Boolean.parseBoolean(args[2]);
                                if(b){
                                    ChallengeController.getInstance().getSettings().setChallenge(true);
                                }
                                ChallengeController.getInstance().getSettings().setChallengeEndsOnPlayerDeath(b);
                                LionChat.sendSystemMessage(Component.text("Challenge-Einstellung geändert: "+b), sender);
                            }
                            default -> LionChat.sendSystemMessage(MSG.WRONG_ARGS, sender);
                        }
                    }else LionChat.sendSystemMessage(MSG.WRONG_ARGS, sender);
                break;
                default:
                    LionChat.sendSystemMessage(MSG.WRONG_ARGS, sender);
            }

        return true;

    }


    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull String[] args) {
        List<String> strings = new ArrayList<>();
        switch (args.length){
            case 0, 1:
                strings.add("start");
                strings.add("set");
                strings.add("pause");
                strings.add("reset");
                strings.add("toggledirection");
                strings.add("resume");
                strings.add("settings");
                break;
            case 2:
                if(args[0].equals("set")||args[0].equals("start")){
                    strings.add("<days>");
                } else if (args[0].equals("settings")) {
                    strings.add("stopbydragon");
                    strings.add("stopbyplayerdeath");
                }
                break;
            case 3:
                if(args[0].equals("set")||args[0].equals("start")){
                    strings.add("<hours>");
                } else if (args[0].equals("settings")) {
                    strings.add("true");
                    strings.add("false");
                }
                break;
            case 4:
                if(args[0].equals("set")||args[0].equals("start")){
                    strings.add("<minutes>");
                }
                break;
            case 5:
                if(args[0].equals("set")||args[0].equals("start")){
                    strings.add("<seconds>");
                }
                break;
            default:
                break;

        }
        return strings;
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] strings) {
        onCommand(commandSourceStack.getSender(), strings );
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        return onTabComplete(commandSourceStack.getSender(), args);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp();
    }
}
