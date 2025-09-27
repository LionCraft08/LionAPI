package de.lioncraft.lionapi.data;

import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.type.PrimitiveType;
import java.lang.reflect.Array;
import java.util.*;

public class BasicSettings implements ConfigurationSerializable {
    private List<Setting> list = new ArrayList<>();

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> values = new HashMap<>();
        list.forEach((s) -> {
            values.put(s.getKey(), s.getValue());
        });
        return values;
    }

    public BasicSettings(Map<String, Object> map){
        map.forEach((key, value)->list.add(new Setting<>(key, value)));
    }

    public BasicSettings(FileConfiguration config, List<Setting> settings){
        list = settings;
        list.forEach(setting -> {
            if (config.contains("settings."+setting.getKey())){
                setting.setValue(config.get("settings."+setting.getKey()));
            }
        });
    }
    public Object getValue(String key){
        for (Setting s : list){
            if (s.getKey().equals(key)) return s.getValue();
        }
        return null;
    }

    public boolean getBoolValue(String key){
        return getValue(key, boolean.class);
    }
    public int getIntValue(String key){
        return getValue(key, int.class);
    }
    public String getStringValue(String key){
        return getValue(key, String.class);
    }
    public <T> T getValue(String key, Class<T> clazz){
        for (Setting s : list){
            if (s.getKey().equals(key)) return (T) s.getValue();
        }
        return getDefaultValueUsingArray(clazz);
    }
    private static <T> T getDefaultValueUsingArray(Class<T> clazz) {
        if (!clazz.isPrimitive()) {
            return null;
        }
        Object array = Array.newInstance(clazz, 1);
        return (T) Array.get(array, 0);
    }

    public void saveTo(YamlConfiguration config){
        list.forEach((setting)->config.set("settings."+setting.getKey(), setting.getValue()));
    }

    public BasicSettings(List<Setting> settings){
        list = settings;
    }
}
