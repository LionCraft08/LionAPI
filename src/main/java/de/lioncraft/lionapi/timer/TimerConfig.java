package de.lioncraft.lionapi.timer;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.data.BasicSettings;
import de.lioncraft.lionapi.data.Setting;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TimerConfig {
    private static BasicSettings config;
    private static Path configPath = LionAPI.getPlugin().getDataPath().resolve("timer-color-presets.yml");
    private static YamlConfiguration configFile;

    public static YamlConfiguration getConfigFile() {
        if (configFile == null) init();
        return configFile;
    }

    public static void save(){
        config.saveTo(configFile);
        try {
            configFile.save(configPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init(){
        configFile = YamlConfiguration.loadConfiguration(configPath.toFile());
        List<Setting> settings = new ArrayList<>();
        settings.add(new Setting<>("speed",
                List.of(
                        Component.text("Sets the speed of the Timer.", TextColor.color(255, 255, 255)),
                        Component.text("1 = lowest, 20 = updates once per second", TextColor.color(255, 255, 255))
                ))
                .setName("Timer Speed")
                .setValue(1)
                );
        settings.add(new Setting<>("selected-preset",
                List.of(
                        Component.text("Sets the color of the timer", TextColor.color(255, 255, 255)),
                        Component.text("1 = lowest, 20 = updates once per second", TextColor.color(255, 255, 255))
                ))
                .setName("Color Theme")
                .setValue("default")
                .setOnChange((oldValue, newValue) -> {
                            TimerLike.setTimerColor((String)newValue);
                            return newValue;
                        })
                );
        settings.add(new Setting<String>("when-paused",
                List.of(
                        Component.text("What to do when the Timer is paused", TextColor.color(255, 255, 255)),
                        Component.text("Can be OFF, MESSAGE or TIMER", TextColor.color(255, 255, 255))
                ))
                .setName("When Paused")
                .setValue("OFF")
                );
        settings.add(new Setting<Boolean>("pause-when-dragon-dies",
                List.of(
                        Component.text("If the timer should be stopped when a ", TextColor.color(255, 255, 255)),
                        Component.text("player kills the dragon", TextColor.color(255, 255, 255))
                ))
                .setName("Pause on dragon death")
                .setValue(false)
                );
        settings.add(new Setting<Boolean>("pause-when-player-dies",
                List.of(
                        Component.text("If the timer should be stopped when a", TextColor.color(255, 255, 255)),
                        Component.text("player dies.", TextColor.color(255, 255, 255))
                ))
                .setName("Pause on Player death")
                .setValue(false)
                );

        config = new BasicSettings(configFile, settings);

    }
    public static BasicSettings getSettings(){
        return config;
    }
    public static int getDelay(){
        return getSettings().getIntValue("speed");
    }
    public static String getWhenPaused(){
        return getSettings().getStringValue("when-paused");
    }
    public static String getColorTheme(){
        return getSettings().getStringValue("selected-preset");
    }
    public static boolean getTimerEndsOnPlayerDeath(){
        return getSettings().getBoolValue("pause-when-player-dies");
    }
    public static boolean getTimerEndsOnDragonDeath(){
        return getSettings().getBoolValue("pause-when-dragon-dies");
    }
    public static void setTimerEndsOnPlayerDeath(boolean value){
        getSettings().setBoolValue("pause-when-player-dies", value);
    }
    public static void setTimerEndsOnDragonDeath(boolean value){
        getSettings().setBoolValue("pause-when-dragon-dies", value);
    }
}
