package de.lioncraft.lionapi.data;

import de.lioncraft.lionapi.guimanagement.functions.Function;
import net.kyori.adventure.text.Component;

import javax.lang.model.type.PrimitiveType;
import java.util.ArrayList;
import java.util.List;

public class Setting<T> {
    private List<Component> description = new ArrayList<>();
    private T value;
    private String key;
    private String name;
    private SettingChangeEvent<T> onChange;

    public Setting(String key, List<Component> description) {
        this.description = description;
        this.key = key;
    }

    public Setting(String key) {
        this.key = key;
    }
    public Setting(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public List<Component> getDescription() {
        return description;
    }

    public Setting<T> setDescription(List<Component> description) {
        this.description = description;
        return this;
    }

    public T getValue() {
        return value;
    }

    public boolean setValueAndTrackChanges(T value){
        T old = this.value;
        setValue(value);
        return old != this.value;
    }
    public Setting<T> setValue(T value) {
        if (onChange != null)
            this.value = onChange.onChange(this.value, value);
        else this.value = value;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Setting<T> setKey(String key) {
        this.key = key;
        return this;
    }

    public String getName() {
        if (name != null) return name;
        return key;
    }

    public Setting<T> setName(String name) {
        this.name = name;
        return this;
    }

    public SettingChangeEvent<T> getOnChange() {
        return onChange;
    }

    public Setting<T> setOnChange(SettingChangeEvent<T> onChange) {
        this.onChange = onChange;
        return this;
    }
}
