package de.lioncraft.lionapi.actions;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.Items;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public enum LionAdvancedActions {
    SET_GAMEMODE(
            Items.get(
                    LionAPI.lm().msg("inv.actions.gamemode.title"),
                    Material.IRON_SWORD,
                    LionAPI.lm().getMessageAsList("inv.actions.gamemode.lore")
            ),
            new ArrayList<>(Arrays.stream(GameMode.values()).map(Enum::toString).toList())
            ,
            s -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setGameMode(GameMode.valueOf(s));
                }
                Bukkit.setDefaultGameMode(GameMode.valueOf(s));
            }
    ),

    ;
    private ItemStack configPreset;
    private Consumer<String> onValueChange;
    private List<String> args;

    LionAdvancedActions(ItemStack configPreset, List<String> args, Consumer<String> onValueChange){
        this.configPreset = configPreset;
        this.onValueChange = onValueChange;
        this.args = args;
    }

    public ItemStack getConfigPreset() {
        return configPreset;
    }

    public List<String> getSelectableStrings() {
        return args;
    }

    public List<String> getStrings() {
        return getSelectableStrings();
    }

    public void onValueChange(String newValue) {
        onValueChange.accept(newValue);
    }

    public Consumer<String> getOnValueChange() {
        return onValueChange;
    }
}
