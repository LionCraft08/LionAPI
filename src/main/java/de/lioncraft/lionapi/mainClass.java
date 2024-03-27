package de.lioncraft.lionapi;

import de.lioncraft.lionapi.events.saveDataEvent;
import de.lioncraft.lionapi.guimanagement.Function;
import de.lioncraft.lionapi.guimanagement.Setting;
import de.lioncraft.lionapi.guimanagement.buttons;
import de.lioncraft.lionapi.listeners.invClickListener;
import de.lioncraft.lionapi.listeners.listeners;
import de.lioncraft.lionapi.messageHandling.defaultMessages;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class mainClass extends JavaPlugin {

    @Override
    public void onEnable() {
        defaultMessages.setValues();
        buttons.setItems();
        plugin = this;
        Bukkit.getPluginManager().registerEvents(new invClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new listeners(), this);
        randomizer.InitializeEntityList();
        randomizer.disabledItemList = new ArrayList<>();
        Setting.SettingList = new HashMap<>();
        for(String s : getConfig().getStringList("disabled-items")){
            Material m = Material.getMaterial(s);
            if(m == null){
                Bukkit.getConsoleSender().sendMessage(defaultMessages.messagePrefix.append(Component.text("Could not find Item " + s)));
            }else{
                randomizer.disabledItemList.add(m);
            }
        }
        Bukkit.getLogger().info("<LionSystems> Successfully enabled LionAPI.");
    }

    @Override
    public void onDisable() {
        Bukkit.getPluginManager().callEvent(new saveDataEvent());
        Bukkit.getLogger().info("<LionSystems> Successfully disabled LionAPI.");
    }
    static Plugin plugin;
    public static Plugin getPlugin(){
        return plugin;
    }
}
