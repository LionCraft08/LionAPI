package de.lioncraft.lionapi.teams;

import com.google.errorprone.annotations.ForOverride;
import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.commands.Teams;
import de.lioncraft.lionapi.data.Settings;
import de.lioncraft.lionapi.events.team.TeamRegisterPlayerEvent;
import de.lioncraft.lionapi.events.team.TeamRemovePlayerEvent;
import de.lioncraft.lionapi.messageHandling.DM;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.*;

/**Creating a subclass of this one requires the following:
 * {@link Team#setMainClass(Class)} to set the new Class
 * <ul>
 * <li>extending this class</li>
 * <li>adding a Constructor that accepts {@link Map}<{@link String},{@link Object}></li>
 * <li>Override {@link Team#getSerializationValues()} for serialization.</li>
 * <li>Annotate the class with {@link org.bukkit.configuration.serialization.DelegateDeserialization(Team)}</li>
 * </ul>
 */
@SerializableAs("LionTeam")
public class Team implements ConfigurationSerializable, Iterable<OfflinePlayer> {
    private static HashMap<OfflinePlayer, Team> playerTeamHashMap = new HashMap<>();
    private static List<Team> teams = new ArrayList<>();
    private static Class<? extends Team> mainClass;
    public static void saveAll(){
        File f = new File(LionAPI.getPlugin().getDataPath().toFile(), "teams.yml");
        try {
            Files.deleteIfExists(f.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("teams", teams);
        try {
            yml.save(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void resetAll(){
        File f = new File(LionAPI.getPlugin().getDataPath().toFile(), "teams.yml");
        f.delete();
        for(Team t : teams) t.clear();
        teams.clear();
        playerTeamHashMap.clear();
    }
    public static void loadAll(){
        File f = new File(LionAPI.getPlugin().getDataPath().toFile(), "teams.yml");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
    }
    /**for more Info see {@link Teams#registerNewCommandPart(String, TeamClassFunction)}
     */
    public static boolean registerNewCommandPart(String args, TeamClassFunction codeToRun){
        return Teams.registerNewCommandPart(args, codeToRun);}

    /**
     * @param name The Teamname
     * @return The Team or null
     */
    public static @Nullable Team getTeam(String name){
        for(Team t : teams){
            if(t.getName().equals(name)){
                return t;
            }
        }
        return null;
    }
    public static List<Team> getTeams() {
        return teams;
    }
    public static Team registerNewTeam(String name){
        Team team;
        if(mainClass != null){
            try {
                team = mainClass.getConstructor(String.class).newInstance(name);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }else{
            team = new Team(name);
        }
        teams.add(team);
        return team;
    }
    public static void unregisterTeam(Team team){
        team.clear();
        teams.remove(team);
        team.name = null;
    }
    public static Team getTeam(OfflinePlayer p){
        return playerTeamHashMap.get(p);
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Class<? extends Team> getMainClass() {
        return mainClass;
    }
    public static void setMainClass(Class<? extends Team> mainClass) {
        Team.mainClass = mainClass;
    }

    private List<OfflinePlayer> players = new ArrayList<>();
    private String name;
    private Backpack backpack;

    @NotNull
    @Override
    public Iterator<OfflinePlayer> iterator() {
        return players.iterator();
    }
    public void updateTabList(Player p){
        if(Settings.isDisplayTeamname()){
            p.playerListName(Component.text("[", TextColor.color(200, 200, 200)).append(Component.text(getName(), TextColor.color(255, 0, 255))).append(Component.text("] ", TextColor.color(200, 200, 200))).append(p.displayName()));
        }else{
            p.playerListName(p.displayName());
        }
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = getSerializationValues();
        map.put("name", name);
        map.put("bp", backpack);
        List<String> list = new ArrayList<>();
        for(OfflinePlayer id : players){
            list.add(id.getUniqueId().toString());
        }
        map.put("players", list);
        return map;
    }
    protected Team(Map<String, Object> map){
        name = (String) map.get("name");
        backpack = (Backpack) map.get("bp");
        List<String> uuids = (List<String>) map.get("players");
        for(String s : uuids){
            players.add(Bukkit.getOfflinePlayer(UUID.fromString(s)));
        }
        teams.add(this);
        for(OfflinePlayer p : players){
            playerTeamHashMap.put(p, this);
        }
    }
    protected Team(String name){
        this.name = name;
        backpack = new Backpack(Settings.getBackpackSize(), Component.text("BP Team "+name), null);
    }

    public static Team valueOf(Map<String, Object> map){
        Team t = null;
        if(mainClass != null){
            try {
                t = mainClass.getConstructor(Map.class).newInstance(map);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                LionAPI.getPlugin().getComponentLogger().warn(Component.text("A Plugin has registered an invalid Class as Child of Team"));
                throw new RuntimeException(e);
            }
            return t;
        }
        t = new Team(map);
        return t;
    }
    public static Team deserialize(Map<String, Object> map){
        return valueOf(map);
    }
    @ForOverride
    protected Map<String, Object> getSerializationValues(){
        return new HashMap<>();
    }

    public Component clear(){
        if(players.isEmpty()){
            return Component.text("This Team is already empty.", TextColor.color(255, 255, 0));
        }else{
            int i = 0;
            List<OfflinePlayer> list = new ArrayList<>(players);
            for(OfflinePlayer p : list){
                removePlayer(p);
                i++;
            }
            return Component.text("Removed "+i+" Players from this Team");
        }
    }

    public List<OfflinePlayer> getPlayers() {
        return new ArrayList<>(players);
    }

    public String getName() {
        return name;
    }
    public Component addPlayer(OfflinePlayer p){
        TeamRegisterPlayerEvent event = new TeamRegisterPlayerEvent(this, p);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()){
            if(event.getCancelMessage() == null) return DM.error("Der Spieler konnte nicht registriert werden.");
            else return DM.error(event.getCancelMessage());
        }
        if(!players.contains(p)){
            players.add(p);
            playerTeamHashMap.put(p, this);
        }else return DM.error(p.getName() + " ist bereits in diesem Team");
        return Component.text(p.getName() + " ist jetzt in Team " + getName());
    }

    public Component removePlayer(OfflinePlayer p){
        TeamRemovePlayerEvent event = new TeamRemovePlayerEvent(this, p);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()){
            if(event.getCancelMessage() == null) return DM.error("Der Spieler konnte nicht entfernt werden.");
            else return DM.error(event.getCancelMessage());
        }
        if(players.contains(p)){
            players.remove(p);
            playerTeamHashMap.remove(p);
        }else return DM.error(p.getName() + " ist nicht in diesem Team");
        return Component.text(p.getName() + " wurde aus Team " + getName() + " entfernt");
    }

    public List<Player> getOnlinePlayers(){
        List<Player> list = new ArrayList<>();
        players.forEach(offlinePlayer -> {
            Player p = Bukkit.getPlayer(offlinePlayer.getUniqueId());
            if (p != null) list.add(p);
        });
        return list;
    }

    public Backpack getBackpack() {
        return backpack;
    }
    public void sendMessage(Component c){
        for(Player p : getOnlinePlayers()){
            p.sendMessage(c);
        }
    }
    public void playSound(Sound sound){
        for(Player p : getOnlinePlayers()){
            p.playSound(sound);
        }
    }

    /**Checks if this Team has been unregistered
     * @return the Status
     */
    public boolean isValid(){
        return name != null;
    }

}
