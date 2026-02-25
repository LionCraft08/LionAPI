package de.lioncraft.lionapi.actions;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.Items;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ActionPlaceholder {

    private static final HashMap<String, BooleanConsumer> placeholders = new HashMap<>();

    /**
     * Registers a new Action for a registered Placeholder. Is only used internally and might be changed soon.
     * @param key The String as defined in {@link LionActions}
     * @param placeholder The Action to actually execute.
     */
    public static void registerPlaceholder(String key, BooleanConsumer placeholder) {
        placeholders.put(key, placeholder);
    }

    public static BooleanConsumer getPlaceholder(String key) {
        return placeholders.getOrDefault(key, (b)->{});
    }

}
