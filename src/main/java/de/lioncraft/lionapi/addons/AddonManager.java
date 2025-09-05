package de.lioncraft.lionapi.addons;

import de.lioncraft.lionapi.messageHandling.lionchat.ChannelConfiguration;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddonManager {
    private static final List<AbstractAddon> addons = new ArrayList<>();
    public static void registerAddon(AbstractAddon addon){
        AtomicBoolean b = new AtomicBoolean(false);
        addons.forEach(addon1 -> {
            if (addon1.getId().equalsIgnoreCase(addon.getId())){
                LionChat.sendLogMessage("An addon with the id "+addon.getId()+" could not be registered, because the id already exists.");
                b.set(true);
            }
        });
        if (b.get()){
            return;
        }
        addons.add(addon);
        if (addon.isUseOwnChannel()){
            registerAddonChannel(addon);
        }
    }
    public static void registerAddonChannel(AbstractAddon addon){
        if (addon.isUseOwnChannel()){
            LionChat.registerChannel(addon.getId(), new ChannelConfiguration(false, TextColor.color(255, 255, 255),
                    addon.getName(),
                    false));
        }else LionChat.sendDebugMessage(addon.getId() + " tried to register an addon Channel but has UseOwnChannel disabled.");
    }
    public static List<AbstractAddon> getAddons(){
        return addons;
    }

}
