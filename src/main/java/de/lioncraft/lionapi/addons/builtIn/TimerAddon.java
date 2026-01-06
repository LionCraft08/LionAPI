package de.lioncraft.lionapi.addons.builtIn;

import de.lioncraft.lionapi.addons.AbstractAddon;
import de.lioncraft.lionapi.guimanagement.Interaction.Button;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.lioninventories.TimerManagementUI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TimerAddon extends AbstractAddon {
    public TimerAddon() {
        super("timer", MiniMessage.miniMessage().deserialize("<#FF8C00>Timer"));
        super.setUseOwnChannel(true);
    }

    @Override
    protected void onLoad() {

    }

    @Override
    protected void onUnload() {

    }

    private ItemStack is = Items.get(getName(), Material.CLOCK, "Open Settings for "+getId());
    @Override
    public ItemStack getSettingsIcon(){
        return is;
    }

    @Override
    public void openConfigMenu(Player p){
        TimerManagementUI.open(p);
    }
}
