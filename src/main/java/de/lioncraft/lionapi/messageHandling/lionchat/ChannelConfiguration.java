package de.lioncraft.lionapi.messageHandling.lionchat;

import de.lioncraft.lionapi.messageHandling.DM;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChannelConfiguration {
    private boolean opOnly;
    private List<UUID> ignoredBy = new ArrayList<>();
    private TextColor defaultColor = NamedTextColor.WHITE;
    private Component prefix = DM.messagePrefix;
    private boolean configurableByNonOPs;

    public ChannelConfiguration(boolean opOnly, TextColor defaultColor, Component prefix, boolean configurableByNonOPs) {
        this.opOnly = opOnly;
        this.defaultColor = defaultColor;
        this.prefix = prefix;
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
    public void setPrefix(Component prefix) {
        this.prefix = prefix;
    }
    public boolean isConfigurableByNonOPs() {
        return configurableByNonOPs;
    }
    public void setConfigurableByNonOPs(boolean configurableByNonOPs) {
        this.configurableByNonOPs = configurableByNonOPs;
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
}
