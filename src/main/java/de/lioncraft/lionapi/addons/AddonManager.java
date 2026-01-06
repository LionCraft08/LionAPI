package de.lioncraft.lionapi.addons;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.messageHandling.lionchat.ChannelConfiguration;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddonManager {
    private static final List<AbstractAddon> addons = new ArrayList<>();
    public static void registerAddon(AbstractAddon addon){
        AtomicBoolean b = new AtomicBoolean(false);
        addons.forEach(addon1 -> {
            if (addon1.getId().equalsIgnoreCase(addon.getId())){
                LionChat.sendLogMessage("An addon with the id "+addon.getId()+" could not be registered, because the id already exists.");
                b.set(true);
            }
        });
        if (b.get()){
            return;
        }
        addons.add(addon);
        if (addon.isUseOwnChannel()){
            registerAddonChannel(addon);
        }
        if (enabledAddons.containsKey(addon.getId())){
            addon.setEnabled(enabledAddons.get(addon.getId()));
        }else{
            enabledAddons.put(addon.getId(), addon.isEnabled());
        }
    }
    public static void registerAddonChannel(AbstractAddon addon){
        if (addon.isUseOwnChannel()){
            LionChat.registerChannel(addon.getId(), new ChannelConfiguration(false, TextColor.color(255, 255, 255),
                    addon.getName(),
                    false));
        }else LionChat.sendDebugMessage(addon.getId() + " tried to register an addon Channel but has UseOwnChannel disabled.");
    }
    public static List<AbstractAddon> getAddons(){
        return addons;
    }


    private static HashMap<String, Boolean> enabledAddons = new HashMap<>();
    private static File addonsData = LionAPI.getPlugin().getDataPath().resolve("addons.yml").toFile();

    public static void save(){
        if (!addonsData.exists()){
            LionAPI.getPlugin().saveResource("addons.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(addonsData);
        for (String addon : enabledAddons.keySet()){
            config.set(addon.toLowerCase(), enabledAddons.get(addon));
        }
        try {
            config.save(addonsData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void load(){
        if (!addonsData.exists()){
            LionAPI.getPlugin().saveResource("addons.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(addonsData);
        for (String s : config.getKeys(true)){
            enabledAddons.put(s.toLowerCase(), config.getBoolean(s));
            for (AbstractAddon addon : addons){
                if (addon.getId().equalsIgnoreCase(s)){
                    addon.setEnabled(config.getBoolean(s));
                }
            }
        }
    }

}
