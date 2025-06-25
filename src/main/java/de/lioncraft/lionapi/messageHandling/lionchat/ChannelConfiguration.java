package de.lioncraft.lionapi.messageHandling.lionchat;

import de.lioncraft.lionapi.guimanagement.Interaction.Button;
import de.lioncraft.lionapi.guimanagement.Interaction.Setting;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.lioninventories.ChannelSelectionMenu;
import de.lioncraft.lionapi.messageHandling.DM;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChannelConfiguration {
    private boolean opOnly;
    private List<UUID> ignoredBy = new ArrayList<>();
    private TextColor defaultColor = NamedTextColor.WHITE;
    private Component prefix = DM.messagePrefix;
    private boolean configurableByNonOPs;
    private Material icon;

    public ChannelConfiguration(boolean opOnly, TextColor defaultColor, Component prefix, boolean configurableByNonOPs) {
        this.opOnly = opOnly;
        this.defaultColor = defaultColor;
        this.prefix = Component.text("[", NamedTextColor.WHITE)
                .append(prefix)
                .append(Component.text("]", NamedTextColor.WHITE));
        this.configurableByNonOPs = configurableByNonOPs;
    }

    public ChannelConfiguration() {
        opOnly = false;
        configurableByNonOPs = true;
    }

    public boolean isOpOnly() {
        return opOnly;
    }
    public void setOpOnly(boolean opOnly) {
        this.opOnly = opOnly;
    }
    public List<UUID> getIgnoredBy() {
        return ignoredBy;
    }
    public void addIgnoredByPlayer(UUID player){
        if (!ignoredBy.contains(player)) ignoredBy.add(player);
    }
    public void removeIgnoredByPlayer(UUID player){
         ignoredBy.remove(player);
    }
    public TextColor getDefaultColor() {
        return defaultColor;
    }
    public void setDefaultColor(TextColor defaultColor) {
        this.defaultColor = defaultColor;
    }
    public Component getPrefix() {
        return prefix;
    }

    public Material getIcon() {
        return icon;
    }

    public ChannelConfiguration setIcon(Material icon) {
        this.icon = icon;
        return this;
    }

    public void setPrefix(Component prefix) {
        this.prefix = prefix;
    }
    public boolean isConfigurableByNonOPs() {
        return configurableByNonOPs;
    }
    public void setConfigurableByNonOPs(boolean configurableByNonOPs) {
        this.configurableByNonOPs = configurableByNonOPs;
    }
    public boolean canReceive(OfflinePlayer p){
        return ((!(!p.isOp() && isOpOnly())) && !ignoredBy.contains(p.getUniqueId()));
    }
    public List<Player> getPlayersToBroadcastTo(){
        List<Player> list = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()){
            if ((!p.isOp())&&opOnly) continue;
            if (ignoredBy.contains(p.getUniqueId())) continue;
            list.add(p);
        }
        return list;
    }
    public void openInventoryFor(Player p){
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("ChannelConfig ").append(getPrefix()));
        inv.setItem(49, Items.closeButton);
        inv.setItem(45, new Button(Items.getBackButton("LionChat Channels"), event -> {
            ChannelSelectionMenu.open((Player) event.getWhoClicked());
            return true;
        }).getButton());
        if ((!p.isOp() && (!isConfigurableByNonOPs()||isOpOnly()))){
            inv.setItem(22, Items.asGUIButton(Items.get(Component.text("You cannot do that!", TextColor.color(255, 0, 0)), Material.RED_STAINED_GLASS_PANE,
                    TextColor.color(255, 100, 100), "This Channel cannot be disabled.")));
        } else {
            Setting s = new Setting(ignoredBy.contains(p.getUniqueId()),
                    Items.get("See Messages in chat", Material.SPYGLASS, "Toggles the visibility of ", "this channel."), isEnabled -> {
                if (isEnabled){
                    ignoredBy.add(p.getUniqueId());
                    LionChat.sendMessageOnChannel(this, Component.text("You can Messages in this Channel now."), p);
                }else{
                    LionChat.sendMessageOnChannel(this, Component.text("You can Messages in this Channel now."), p);
                    ignoredBy.remove(p.getUniqueId());
                }
            });
            inv.setItem(10, s.getTopItem());
            inv.setItem(19, s.getBottomItem());
            if (p.isOp()) {
                inv.setItem(12, getChangeIconButton(inv));
            }
        }

        p.openInventory(inv);
    }
    private ItemStack getChangeIconButton(Inventory inv){
        return new Button(Items.get("Click to change Icon", icon, "Click to change the icon of this channel."), event -> {
            ItemStack is = event.getView().getCursor();
            if (is.getType().isAir()){
                LionChat.sendMessageOnChannel("system", Component.text("You need an Item on your Cursor to change the Icon."), event.getWhoClicked());
                event.getWhoClicked().playSound(Sound.sound(Key.key("entity.villager.no"), Sound.Source.PLAYER, 1.0f, 1.0f));
            }else{
                icon = is.getType();
                event.getWhoClicked().playSound(Sound.sound(Key.key("block.amethyst.hit"), Sound.Source.PLAYER, 1.0f, 1.0f));
                inv.setItem(12, getChangeIconButton(inv));
                return true;
            }
            return false;
        }).getButton();
    }

    public ItemStack getButton(){
        return new Button(Items.get(getPrefix(), icon, TextColor.color(255, 0, 255), "Click to open Settings for", "this channel"), event -> {
            openInventoryFor((Player) event.getWhoClicked());
            return true;
        }).getButton();
    }
}
