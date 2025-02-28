package de.lioncraft.lionapi.commands;

import de.lioncraft.lionapi.messageHandling.DM;
import de.lioncraft.lionapi.teams.Team;
import de.lioncraft.lionapi.teams.TeamClassFunction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Teams implements TabExecutor {
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
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 1){
            sender.sendMessage(DM.wrongArgs);
        }
        if(args.length == 1){
            switch (args[0]){
                case "shuffle":
                    shuffleTeams(sender);
                    break;
                case "list":
                    listTeams(sender);
                    break;
                default:sender.sendMessage(DM.wrongArgs);
            }
        }
        if(args.length >=2){
            Team t = Team.getTeam(args[1]);
            switch (args[0]){
                case "register":
                    if(t!=null){
                        sender.sendMessage(DM.messagePrefix.append(Component.text("This Team already exists!", TextColor.color(255, 128, 0))));
                    }else{
                        t = Team.registerNewTeam(args[1]);
                        sender.sendMessage(DM.messagePrefix.append(Component.text("Created new Team called \""+t.getName()+"\"")));
                    }
                    break;
                case "delete":
                    if(t==null){
                        sender.sendMessage(DM.messagePrefix.append(Component.text("This Team does not exists!", TextColor.color(255, 128, 0))));
                    }else{
                        Team.unregisterTeam(t);
                        sender.sendMessage(DM.messagePrefix.append(Component.text("Removed Team " + args[1])));
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
                        sender.sendMessage(DM.messagePrefix.append(Component.text("This Team does not exists!", TextColor.color(255, 128, 0))));
                    }else{
                        if(args.length >= 3){
                            switch (args[2]){
                                case "add":
                                    if(args.length >= 4){
                                        Team ct = Team.getTeam(Bukkit.getOfflinePlayer(args[3]));
                                        if(ct != null){
                                            sender.sendMessage(DM.error(Component.text("Dieser Spieler ist schon in Team " + ct.getName())));
                                        }else{
                                            sender.sendMessage(t.addPlayer(Bukkit.getOfflinePlayer(args[3])));
                                        }
                                    }else sender.sendMessage(DM.wrongArgs);
                                    break;
                                case "remove":
                                    if(args.length >= 4){
                                        OfflinePlayer p = Bukkit.getOfflinePlayer(args[3]);
                                        if (t.getPlayers().contains(p)) {
                                            sender.sendMessage(t.removePlayer(p));
                                        }else sender.sendMessage(DM.messagePrefix.append(Component.text("The given Player is not in this Team.")));
                                    }else sender.sendMessage(DM.wrongArgs);
                                    break;
                                case "clear":
                                    sender.sendMessage(DM.messagePrefix.append(t.clear()));
                                    break;
                                case "fill":
                                    int amount = 0;
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        if (Team.getTeam(p) == null){
                                            t.addPlayer(p);
                                            amount++;
                                        }
                                    }
                                    if (amount>1){
                                        sender.sendMessage(DM.info("Es wurden "+amount+" Spieler zu Team "+t.getName()+" hinzugefügt." ));
                                    } else if (amount==1) sender.sendMessage(DM.info("Es wurde "+amount+" Spieler zu Team "+t.getName()+" hinzugefügt." ));
                                    else sender.sendMessage(DM.info("Alle Spieler sind bereits in Teams"));
                                    break;
                                case "changename":
                                    if (args.length >= 4){
                                        if (Team.getTeam(args[3])==null){
                                            String s = t.getName();
                                            t.setName(args[3]);
                                            sender.sendMessage(DM.info("Das Team "+s+" wurde umbenannt in "+t.getName()));
                                        }else sender.sendMessage(DM.fatalError("Dieses Team existiert bereits!"));
                                    }else sender.sendMessage(DM.wrongArgs);
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
                                    if (b) sender.sendMessage(DM.wrongArgs);
                                    break;
                            }
                        }else sender.sendMessage(DM.messagePrefix.append(Component.text("This Team does not exists!", TextColor.color(255, 128, 0))));
                    }
                    break;
                default: sender.sendMessage(DM.wrongArgs);
            }
        }
        return true;
    }

    private void listTeams(CommandSender sender) {
        if(!Team.getTeams().isEmpty()){
            sender.sendMessage(DM.info("Teams: " + Team.getTeams().size()));
            for(Team t2 : Team.getTeams()){
                Component c = Component.text(" > " + t2.getName());
                Component h = Component.text("");
                for(OfflinePlayer p : t2){
                    h = h.append(Component.text(p.getName())).appendNewline();
                }
                c = c.hoverEvent(h.asHoverEvent());
                sender.sendMessage(c);
            }
        }else sender.sendMessage(DM.error("No Teams existing"));
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
                sender.sendMessage(DM.info("Alle Spieler wurden zufällig verteilt"));
            }else sender.sendMessage(DM.error("Es werden hierfür mindestens 2 Teams benötigt"));
        }else sender.sendMessage(DM.error("Alle Spieler sind bereits in Teams"));
    }

    /**\team [register|delete|get|shuffle|list] {teamname} [add|remove|clear]
     *
     * @param sender Source of the command.  For players tab-completing a
     *     command inside a command block, this will be the player, not
     *     the command block.
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args The arguments passed to the command, including final
     *     partial argument to be completed
     * @return the List
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        switch (args.length){
            case 1: return List.of("register", "delete", "get", "shuffle","list");
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
}
