package de.lioncraft.lionapi.commands;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.data.ChallengeSettings;
import de.lioncraft.lionapi.messageHandling.DM;
import de.lioncraft.lionapi.messageHandling.defaultMessages;
import de.lioncraft.lionapi.timer.MainTimer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class timerCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        switch (args.length){
                case 0:
                    if(sender instanceof Player p){
                        MainTimer.openUI(p);
                    }else sender.sendMessage(DM.notAPlayer);
                break;
                case 1:
                    switch (args[0]) {
                        case "start", "resume" -> {
                            sender.sendMessage(MainTimer.getTimer().start());
                        }
                        case "pause" -> {
                            if (MainTimer.getTimer().isActive()) {
                                MainTimer.getTimer().pause();
                                sender.sendMessage(DM.messagePrefix.append(Component.text("Successfully paused the Timer")));
                            } else {
                                sender.sendMessage(DM.messagePrefix.append(Component.text("The Timer is already paused!")));
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
                            sender.sendMessage(DM.messagePrefix.append(Component.text("Successfully restarted the Timer")));
                        }
                        case "toggledirection" -> {
                            MainTimer.changeDirection();
                            if(MainTimer.isCountUpwards()) sender.sendMessage(DM.messagePrefix.append(Component.text("The timer now counts upwards")));
                            else sender.sendMessage(DM.messagePrefix.append(Component.text("The timer now counts down")));

                        }
                        default -> sender.sendMessage(DM.wrongArgs);
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
                            sender.sendMessage(MainTimer.getTimer().start());
                        }catch (NumberFormatException e){
                            sender.sendMessage(DM.messagePrefix.append(Component.text("Make sure you use numbers as parameters 2-5")));
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
                            sender.sendMessage(DM.messagePrefix.append(Component.text("Successfully set the Timer to " + MainTimer.getTimer().getCurrentSeconds() + "s")));
                        }catch (NumberFormatException e){
                            sender.sendMessage(DM.messagePrefix.append(Component.text("Make sure you use numbers as parameters 2-5")));
                        }
                    }else{
                        sender.sendMessage(DM.wrongArgs);
                    }
                    break;
                case 3:
                    if(args[0].equals("settings")){
                        switch (args[1]){
                            case "stopbydragon" -> {
                                boolean b = Boolean.getBoolean(args[2]);
                                if(b){
                                    LionAPI.isChallenge = true;
                                }
                                ChallengeSettings.challengeEndsOnDragonDeath = b;
                            }
                            case "stopbyplayerdeath" ->{
                                boolean b = Boolean.getBoolean(args[2]);
                                if(b){
                                    LionAPI.isChallenge = true;
                                }
                                ChallengeSettings.challengeEndsOnPlayerDeath = b;
                            }
                            default -> sender.sendMessage(DM.wrongArgs);
                        }
                    }else sender.sendMessage(DM.wrongArgs);
                break;
                default:
                    sender.sendMessage(DM.wrongArgs);
            }

        return true;

    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> strings = new ArrayList<>();
        switch (args.length){
            case 1:
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
}
