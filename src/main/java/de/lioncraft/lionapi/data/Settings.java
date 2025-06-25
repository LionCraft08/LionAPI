package de.lioncraft.lionapi.data;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.teams.Team;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

import static de.lioncraft.lionapi.LionAPI.*;

public final class Settings {
    private Settings(){}
    private static BasicSettings settings;

    public static boolean isTeamsHaveBackpack() {
        return settings.getBoolValue("teams.has-backpack");
    }

    public static boolean isDisplayTeamname() {
        return settings.getBoolValue("teams.display-teamname");
    }
    public static int getBackpackSize() {
        return settings.getIntValue("teams.backpack-size");
    }
    public static boolean isAllowTeammsg() {
        return settings.getBoolValue("teams.allow-teammsg");
    }
    public static void init(){
        FileConfiguration c = getPlugin().getConfig();
        List<Setting> list = new ArrayList<>();
        list.add(new Setting<>("teams.has-backpack", List.of(
                Component.text("Sets whether Teams can access a"),
                Component.text("Backpack. Changing this mid-game can"),
                Component.text("make Items unaccessible.")))
                .setName("Backpacks")
                .setValue(true)
                .setOnChange((old, newValue) -> {
                    if (!(boolean)newValue){
                        for (Team t : Team.getTeams()){
                            t.getBackpack().getInv().close();
                        }
                    }
                    return true;
                }));
        list.add(new Setting<>("teams.display-teamname", List.of(
                Component.text("Sets whether the name of a Team"),
                Component.text("is diplayed ingame.")))
                .setName("Display teamnames")
                .setValue(true));
        list.add(new Setting<>("teams.allow-teammsg", List.of(
                Component.text("Sets whether Teams can use their own"),
                Component.text("Chat by using /tmsg")))
                .setName("Allow Teamchats")
                .setValue(true));
        list.add(new Setting<>("teams.backpack-size", List.of(
                Component.text("Sets the Backpack-Size."),
                Component.text("Can destroy Items if used mid-game")))
                .setName("Allow Teamchats")
                .setValue(true)
                .setOnChange((oldValue, newValue) -> {
                    int old = (int) oldValue;
                    int newV = (int) newValue;
                    if (newV > old){
                        newV = old+9;
                    } else if (old > newV) {
                        newV = old-9;
                    }
                    if (newV%9==0&&newV>0&&newV<=54)
                        return newV;
                    return oldValue;
                }));
        settings = new BasicSettings(c, list);
    }
}
