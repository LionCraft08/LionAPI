package de.lioncraft.lionapi.sound;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.intellij.lang.annotations.Subst;

public enum Sounds {
    Error("entity.silverfish.death"),
    Click("ui.button.click"),

    ;

    private final String key;
    Sounds(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
    public Sound getSound(){
        return Sound.sound(Key.key(this.getKey()), Sound.Source.UI, 1.0f, 1.0f);
    }

    public static Sound getSound(Sounds sound){
        return Sound.sound(Key.key(sound.getKey()), Sound.Source.UI, 1.0f, 1.0f);
    }
}
