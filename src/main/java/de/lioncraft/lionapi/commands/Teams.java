package de.lioncraft.lionapi.commands;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.messageHandling.DM;
import de.lioncraft.lionapi.messageHandling.MSG;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.teams.Team;
import de.lioncraft.lionapi.teams.TeamClassFunction;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Teams implements BasicCommand {
    private static HashMap<String, TeamClassFunction> registeredArgs = new HashMap<>();

    /**Will register a new part to the /team Command.<br>
     * The user has to enter the params like the following: /team get {teamname} argument
     * <br>Supports Tab Completing
     * @param argument a String wich has to match the args used in the command.
     *                 Can contain whitespaces to divide the String into multiple arguments.
     * @param codeToRun the code that will be executed if some1 uses the Command
     * @return true if successful
     */
    public static boolean registerNewCommandPart(@NotNull String argument, @NotNull TeamClassFunction codeToRun){
        if(registeredArgs.containsKey(argument)){
            return false;
        }else{
            registeredArgs.put(argument, codeToRun);
            return true;
        }
    }
    public void onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if(args.length < 1){
            LionChat.sendSystemMessage(MSG.WRONG_ARGS, sender);
        }
        if(args.length == 1){
            switch (args[0]){
                case "shuffle":
                    shuffleTeams(sender);
                    break;
                case "list":
                    listTeams(sender);
                    break;
                default:LionChat.sendSystemMessage(MSG.WRONG_ARGS, sender);
            }
        }
        if(args.length >=2){
            Team t = Team.getTeam(args[1]);
            switch (args[0]){
                case "register":
                    if(t!=null){
                        LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.error.already_existing"), sender);
                    }else{
                        t = Team.registerNewTeam(args[1]);
                        LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.created", t.getName()), sender);
                    }
                    break;
                case "delete":
                    if(t==null){
                        LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.error.not_existing"), sender);
                    }else{
                        Team.unregisterTeam(t);
                        LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.removed", args[1]), sender);
                    }
                    break;
                case "shuffle":
                    shuffleTeams(sender);
                    break;
                case "list":
                    listTeams(sender);
                    break;
                case "get":
                    if(t==null){
                        LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.error.not_existing"), sender);
                    }else{
                        if(args.length >= 3){
                            switch (args[2]){
                                case "add":
                                    if(args.length >= 4){
                                        Team ct = Team.getTeam(Bukkit.getOfflinePlayer(args[3]));
                                        if(ct != null){
                                            LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.error.player_already_in_team", args[3], ct.getName()), sender);
                                        }else{
                                            LionChat.sendSystemMessage(t.addPlayer(Bukkit.getOfflinePlayer(args[3])), sender);
                                        }
                                    }else LionChat.sendSystemMessage(MSG.WRONG_ARGS, sender);
                                    break;
                                case "remove":
                                    if(args.length >= 4){
                                        OfflinePlayer p = Bukkit.getOfflinePlayer(args[3]);
                                        if (t.getPlayers().contains(p)) {
                                            LionChat.sendSystemMessage(t.removePlayer(p), sender);
                                        }else LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.error.player_not_in_team", args[3], t.getName()), sender);
                                    }else LionChat.sendSystemMessage(MSG.WRONG_ARGS, sender);
                                    break;
                                case "clear":
                                    LionChat.sendSystemMessage(t.clear(), sender);
                                    break;
                                case "fill":
                                    int amount = 0;
                                    Component c = Component.text("Added players:");
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        if (Team.getTeam(p) == null){
                                            t.addPlayer(p);
                                            c = c.appendNewline().append(p.displayName());
                                            amount++;
                                        }
                                    }
                                    if (amount>1){
                                        c = Component.text(amount).hoverEvent(c.asHoverEvent());
                                        LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.player_added_amount", c, Component.text(t.getName())), sender);
                                    } else if (amount==1) LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.player_added_amount_single", Component.text(t.getName())), sender);
                                    else LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.error.full"), sender);
                                    break;
                                case "changename":
                                    if (args.length >= 4){
                                        if (Team.getTeam(args[3])==null){
                                            String s = t.getName();
                                            t.setName(args[3]);
                                            LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.renamed", s, t.getName()), sender);
                                        }else LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.error.already_existing"), sender);
                                    }else LionChat.sendSystemMessage(MSG.WRONG_ARGS, sender);
                                    break;
                                default:
                                    List<String> list = new ArrayList<>(List.of(args));
                                    list.remove(0);
                                    list.remove(0);
                                    StringBuilder complete = new StringBuilder();
                                    for(String s : list){
                                        complete.append(s).append(" ");
                                    }
                                    complete.deleteCharAt(complete.length() - 1);
                                    boolean b = true;
                                    for(String s : registeredArgs.keySet())
                                        if (s.equalsIgnoreCase(complete.toString())||complete.toString().toLowerCase().startsWith(s.toLowerCase())){
                                            registeredArgs.get(s).onRun(sender, list.toArray(new String[0]), t);
                                            b = false;
                                        }
                                    if (b) LionChat.sendSystemMessage(MSG.WRONG_ARGS, sender);
                                    
                                    break;
                            }
                        }else LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.error.not_existing"), sender);
                    }
                    break;
                default: LionChat.sendSystemMessage(MSG.WRONG_ARGS, sender);
            }
        }
    }

    private void listTeams(CommandSender sender) {
        if(!Team.getTeams().isEmpty()){
            LionChat.sendSystemMessage("Teams: " + Team.getTeams().size(), sender);
            for(Team t2 : Team.getTeams()){
                Component c = Component.text(" > " + t2.getName());
                Component h = Component.text("");
                for(OfflinePlayer p : t2){
                    h = h.append(Component.text(p.getName())).appendNewline();
                }
                c = c.hoverEvent(h.asHoverEvent());
                sender.sendMessage(c);
            }
        }else LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.error.no_team"), sender);
    }

    private void shuffleTeams(CommandSender sender){
        List<Player> players = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()) if (Team.getTeam(p) == null) players.add(p);
        if(!players.isEmpty()){
            if(Team.getTeams().size() >= 2){
                Random r = new Random();
                while (!players.isEmpty()){
                    Player p = players.get(r.nextInt(players.size()));
                    Team lowest = Team.getTeams().get(0);
                    for(Team t1 : Team.getTeams()){
                        if(t1.getPlayers().size() < lowest.getPlayers().size()){
                            lowest = t1;
                        }
                    }
                    lowest.addPlayer(p);
                    players.remove(p);
                }
                LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.shuffle"), sender);
            }else LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.error.shuffle", "2"), sender);
        }else LionChat.sendSystemMessage(LionAPI.lm().msg("features.teams.error.full"), sender);
    }

    /**\team [register|delete|get|shuffle|list] {teamname} [add|remove|clear]
     *
     * @param sender Source of the command.  For players tab-completing a
     *     command inside a command block, this will be the player, not
     *     the command block.
     * @param args The arguments passed to the command, including final
     *     partial argument to be completed
     * @return the List
     */
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        switch (args.length){
            case 0, 1: return List.of("register", "delete", "get", "shuffle","list");
            case 2:
                for(Team t : Team.getTeams()){
                    list.add(t.getName());
                }
                break;
            case 3:
                if(args[0].equals("get")){
                    list.addAll(List.of("add", "remove", "clear", "fill", "changename"));
                    for(String s : registeredArgs.keySet()){
                        list.add(s.split(" ")[0]);
                    }
                }
                break;
            case 4:
                Team t = Team.getTeam(args[1]);
                if(t == null){
                    break;
                }
                if (args[0].equals("get")) {
                    if(args[2].equals("add")){
                        for(Player p : Bukkit.getOnlinePlayers()) {
                            if (!t.getPlayers().contains(p)) {
                                list.add(p.getName());
                            }
                        }
                        break;
                    } else if (args[2].equals("remove")) {
                        for(Player p : Bukkit.getOnlinePlayers()){
                            if(t.getPlayers().contains(p)){
                                list.add(p.getName());
                            }
                        }
                        break;
                    } else if (args[2].equals("clear")||args[2].equals("fill")) {
                        break;
                    } else if (args[2].equals("changename")) {
                        list.add("<name>");
                        break;
                    }
                }
            default:
                for(String s : registeredArgs.keySet()){
                    String[] strings = s.split(" ");
                    boolean b = true;
                    for (int i = 0; i< args.length-2;i++){
                        try {
                            if (!strings[i].equalsIgnoreCase(args[i+2])){
                                b=false;
                                break;
                            }
                        }catch (ArrayIndexOutOfBoundsException e){break;}

                    }
                    if(b){
                        list.add(strings[args.length-3]);
                    }
                }
        }
        return list;
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
