package de.lioncraft.lionapi.guimanagement.Interaction;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class LionButtonFactory {
    private static NamespacedKey namespacedKey = new NamespacedKey("lionapi", "button_data");
    public static ItemStack createButton(Material m, String id){
        ItemStack is = new ItemStack(m);
        return createButton(is, id);
    }
    public static ItemStack createButton(ItemStack is, String id){
        is.editMeta(itemMeta -> {
            itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, id);
        });
        return is;
    }

    public static NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }
}
