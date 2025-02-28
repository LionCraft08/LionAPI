package de.lioncraft.lionapi.data;

import de.lioncraft.lionapi.LionAPI;

public final class Settings {
    private Settings(){}
    private static boolean teamsHaveBackpack, displayTeamname;
    private static int backpackSize;

    public static boolean isTeamsHaveBackpack() {
        return teamsHaveBackpack;
    }
    public static void setTeamsHaveBackpack(boolean teamsHaveBackpack) {
        Settings.teamsHaveBackpack = teamsHaveBackpack;
    }
    public static boolean isDisplayTeamname() {
        return displayTeamname;
    }
    public static void setDisplayTeamname(boolean displayTeamname) {
        Settings.displayTeamname = displayTeamname;
    }
    public static int getBackpackSize() {
        return backpackSize;
    }
    public static void setBackpackSize(int backpackSize) {
        Settings.backpackSize = backpackSize;
    }
    public static void init(){
        teamsHaveBackpack = LionAPI.getPlugin().getConfig().getBoolean("settings.teams.has-backpack");
        displayTeamname = LionAPI.getPlugin().getConfig().getBoolean("settings.teams.display-teamname");
        backpackSize = LionAPI.getPlugin().getConfig().getInt("settings.teams.backpack-size");
    }
}
