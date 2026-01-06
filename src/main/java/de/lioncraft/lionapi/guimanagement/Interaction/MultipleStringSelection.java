package de.lioncraft.lionapi.guimanagement.Interaction;

import de.lioncraft.lionapi.guimanagement.functions.StringSelectionFunction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MultipleStringSelection {
    private static final HashMap<String, MultipleStringSelection> mappings = new HashMap<>();

    /** See more at {@link MultipleStringSelection#MultipleStringSelection(ItemStack, List, StringSelectionFunction)}
     *
     * @param base the base ItemStack
     * @param strings the different lore components to switch between
     * @param f The code to execute when the Item is clicked.
     * @return the ItemStack to place in the Inventory
     */
    public static ItemStack createMultipleStringSelection(ItemStack base, List<String> strings, StringSelectionFunction f){
        return new MultipleStringSelection(base, strings, f).getItem();
    }
    private final List<String> lore;
    private final ItemStack base;
    private final StringSelectionFunction f;
    private int currentItem = 0;
    private final String uuid;
    /** Creates a MultipleStringSelection<br>
     * The Item will display the content of "strings" in its lore, with the selected one marked green.
     * The content of "strings" will be deserialized by MiniMessage, however, they should not contain any color.
     * @param base the base ItemStack
     * @param strings the different lore components to switch between
     * @param f The code to execute when the Item is clicked
     */
    public MultipleStringSelection(ItemStack base, List<String> strings, StringSelectionFunction f){
        lore = strings;
        this.base = base;
        this.f = f;
        uuid = UUID.randomUUID().toString();
        mappings.put(uuid, this);
    }

    public int getCurrentString() {
        return currentItem;
    }

    public void setCurrentString(int currentItem) {
        this.currentItem = currentItem;
    }

    public ItemStack getItem(){
        ItemStack is = base.clone();
        List<Component> lores = new ArrayList<>();
        for (String s : lore){
            if (lore.indexOf(s) == currentItem) lores.add(MiniMessage.miniMessage().deserialize("<#00AA00>"+s));
            else lores.add(MiniMessage.miniMessage().deserialize("<#DDDDDD>"+s));
        }
        is.lore(lores);
        return LionButtonFactory.createButton(is, "lionapi_multiplestringselection."+uuid);
    }

    private void onClick(InventoryClickEvent e){
        currentItem++;
        if (currentItem >= lore.size()) currentItem = 0;
        e.setCancelled(true);
        f.run(currentItem, lore.get(currentItem), e);
        e.setCurrentItem(getItem());
    }

    /**
     * Internal Method, should not be used unless you are the developer of LionAPI xd
     */
    @ApiStatus.Internal
    public static void click(String id, InventoryClickEvent e){
        MultipleStringSelection ms = mappings.get(id);
        if(ms != null){
            ms.onClick(e);
        }
    }
}
