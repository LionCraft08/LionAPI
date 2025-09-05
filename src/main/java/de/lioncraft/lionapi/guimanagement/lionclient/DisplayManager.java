package de.lioncraft.lionapi.guimanagement.lionclient;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.guielements.GUIPlayerManager;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.velocity.ProxyMessageListeners;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.potion.PotionType;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

public class DisplayManager implements PluginMessageListener {
    //command:name:type:offsx:offsy:attachment:data
    public static void sendDisplayCheck(Player p){
        p.sendPluginMessage(LionAPI.getPlugin(), channel, "check_existing".getBytes());
    }

    public static void sendDisplayReset(Player p, String id){
        p.sendPluginMessage(LionAPI.getPlugin(), channel, ("delete_display:"+id).getBytes());
    }

    public static void sendDisplayText(Player p, String id, Integer offsetX, Integer offsetY, DisplayAttachment attachment, Component text, int maxWidth){
        String s = "update_display:"+id+":text:"+offsetX+":"+offsetY+":"+attachment.toString()+":"+maxWidth+":"+
                GsonComponentSerializer.gson().serialize(text);
        p.sendPluginMessage(LionAPI.getPlugin(), channel, s.getBytes());
    }
    public static void sendDisplayText(Player p, DisplayAttachment attachment, String id, Component text){
        sendDisplayText(p, id, null, null, attachment, text, 200);
    }
    public static void sendDisplaySquare(Player p, String id, Integer offsetX, Integer offsetY, DisplayAttachment attachment, int color, int width, int height){
        String s = "update_display:"+id+":square:"+offsetX+":"+offsetY+":"+attachment.toString()+":"+color+":"+width+":"+height;
        p.sendPluginMessage(LionAPI.getPlugin(), channel, s.getBytes());
    }
    public static void sendDisplayCompass(Player p, String id, Integer offsetX, Integer offsetY, DisplayAttachment attachment, int posX, Integer posY, int posZ){
        String s = "update_display:"+id+":compass:"+offsetX+":"+offsetY+":"+attachment.toString()+":"+posX+":"+posY+":"+posZ;
        p.sendPluginMessage(LionAPI.getPlugin(), channel, s.getBytes());
    }
    public static void sendDisplayCompass(Player p, String id, int posX, Integer posY, int posZ){
        sendDisplayCompass(p, id, null, null, DisplayAttachment.TOP_LEFT, posX, posY, posZ);
    }
    public static void sendDisplayFrame(Player p, String id, Integer offsetX, Integer offsetY, DisplayAttachment attachment, int color, int width, int height){
        String s = "update_display:"+id+":frame:"+offsetX+":"+offsetY+":"+attachment.toString()+":"+color+":"+width+":"+height;
        p.sendPluginMessage(LionAPI.getPlugin(), channel, s.getBytes());
    }
    public static void sendDisplayItem(Player p, String id, Integer offsetX, Integer offsetY, DisplayAttachment attachment, ItemStack is){
        String serializedItemStack = is.getType()+":"+
                is.getAmount()+":"+
                (is.getItemMeta().hasEnchantmentGlintOverride()||is.getItemMeta().hasEnchants())+":"+
                getCustomData(is)+":"+getCustomModelString(is);
        String s = "update_display:"+id+":item:"+offsetX+":"+offsetY+":"+attachment.toString()+":"+serializedItemStack;
        p.sendPluginMessage(LionAPI.getPlugin(), channel, s.getBytes());
    }
    private static String getCustomModelString(ItemStack is){
        if (is.getItemMeta().hasCustomModelDataComponent()) {
            final String[] s = {""};
            is.getItemMeta().getCustomModelDataComponent().getStrings().forEach(s1 -> {
                s[0] = s[0] +"@"+s1;
            });
            return s[0];
        }
        return null;
    }
    private static String getCustomData(ItemStack is){
        if (is.getItemMeta() instanceof PotionMeta pm){
            return Objects.requireNonNullElse(pm.getBasePotionType(), PotionType.WATER).toString();
        }
        if (is.getItemMeta() instanceof ArmorMeta am){
            if (am.hasTrim()){
                return am.getTrim().getPattern()+"@"+am.getTrim().getMaterial();
            }
        }
        if (is.getItemMeta() instanceof BannerMeta bm){
            if (!bm.getPatterns().isEmpty()){
                StringBuilder s = new StringBuilder();
                for (Pattern p : bm.getPatterns()){
                    s.append(p.getPattern().toString()).append(p.getColor().toString());
                    s.append("@");
                }
                return s.toString();
            }
        }
        return null;
    }
    public static void sendMessage(Player p, String message){
        p.sendPluginMessage(LionAPI.getPlugin(), channel, message.getBytes());
    }
    public static void sendCompassData(Player p, double xz, double y){
        p.sendPluginMessage(LionAPI.getPlugin(), channel, ("compass_data:"+xz+":"+y).getBytes());
    }

    @Override
    public void onPluginMessageReceived(@NotNull String s, @NotNull Player player, byte @NotNull [] bytes) {
        if (s.equalsIgnoreCase(channel)){
            String data = new String(bytes);
            if (data.contains(":")){
                String cmd = data.substring(0, data.indexOf(":"));
                switch (cmd){
                    case "check_existing"->{
                        if(getStringAtIndex(1, data).equalsIgnoreCase("true")){
                            LionChat.sendSystemMessage(Component.text("Dein Client wurde erfolgreich registriert."), player);
                            GUIPlayerManager.setRenderWay(player, GUIPlayerManager.ClientRenderWay.LIONDISPLAYS_MOD);
                        }

                    }
                    case "send_version" -> {

                    }
                }
            }

        }
    }
    private String getStringAtIndex(int index, String message){
        if (!message.contains(":")) return message;
        else {
            var array = message.split(":");
            if (array.length>index){
                return array[index];
            }
        }
        return "null";
    }

    private static String channel = "lionapi:display_communication";
    public static void register(Plugin plugin){
        Messenger m = Bukkit.getServer().getMessenger();
        m.registerIncomingPluginChannel(plugin, channel, new DisplayManager());
        m.registerOutgoingPluginChannel(plugin, channel);
    }
}
