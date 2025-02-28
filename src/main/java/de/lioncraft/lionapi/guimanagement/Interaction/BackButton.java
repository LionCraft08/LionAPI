package de.lioncraft.lionapi.guimanagement.Interaction;

import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.functions.emptyFunction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BackButton extends Button{
    private Inventory inv;
    private boolean deleteOnClose;

    /**Creates a {@link Button} opening the given Inventory
     * @param previousInv The inv to open when clicking the Button
     * @param invTitle The Title of the Inventory, because it is not stored in the Inv itself but in the View.
     */
    public BackButton(Inventory previousInv, String invTitle, boolean deleteOnClose) {
        super(Items.getBackButton(invTitle), event -> {
            event.getWhoClicked().openInventory(previousInv);
        return deleteOnClose;});
        inv = previousInv;
        this.deleteOnClose = deleteOnClose;
    }

    public Inventory getInv() {
        return inv;
    }
    public void setInv(Inventory inv) {
        this.inv = inv;
        super.setFunction(event -> {
            event.getWhoClicked().openInventory(getInv());
        return deleteOnClose;});
    }

}
