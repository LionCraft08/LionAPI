package de.lioncraft.lionapi.guimanagement.lionclient;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.guielements.GUIPlayerManager;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DisplayManager implements PluginMessageListener {
    private static Runtime.Version version = Runtime.Version.parse("1.0.2");
    //command:name:type:offsx:offsy:attachment:data
    public static void sendDisplayCheck(Player p){
        p.sendPluginMessage(LionAPI.getPlugin(), channel, ("check_existing:"+ version.toString()).getBytes());
    }

    public static void sendDisplayData(Player p, LionDisplayData ldd){
        p.sendPluginMessage(LionAPI.getPlugin(), channel, ("update_display:"+ldd.toString()).getBytes());
    }

    public static void sendDisplayReset(Player p, String id){
        sendDisplayData(p, new LionDisplayData(id, "delete"));
    }

    public static void sendDisplayText(Player p, String id, Integer offsetX, Integer offsetY, @Nullable DisplayAttachment attachment, Component text, Integer maxWidth){
        LionDisplayData ld = new LionDisplayData(id, "text", offsetX, offsetY, attachment);
        ld.setData("text", GsonComponentSerializer.gson().serialize(text));
        ld.setData("maxWidth", String.valueOf(maxWidth));
        sendDisplayData(p, ld);
    }
    public static void sendDisplayText(Player p, @Nullable DisplayAttachment attachment, String id, Component text){
        sendDisplayText(p, id, null, null, attachment, text, null);
    }

    /**
     * @deprecated Use {@link DisplayManager#sendDisplayRectangle(Player, String, Integer, Integer, DisplayAttachment, int, int, int)} instead
     */
    @Deprecated
    public static void sendDisplaySquare(Player p, String id, Integer offsetX, Integer offsetY, @Nullable DisplayAttachment attachment, int color, int width, int height){
        sendDisplayRectangle(p, id, offsetX, offsetY, attachment, color, width, height);
    }

    public static void sendDisplayRectangle(Player p, String id, Integer offsetX, Integer offsetY, @Nullable DisplayAttachment attachment, int color, int width, int height){
        LionDisplayData ld = new LionDisplayData(id, "square", offsetX, offsetY, attachment);
        ld.setData("color", String.valueOf(color));
        ld.setData("width", String.valueOf(width));
        ld.setData("height", String.valueOf(height));
        sendDisplayData(p, ld);
    }
    public static void sendDisplayCompass(Player p, String id, Integer offsetX, Integer offsetY, @Nullable DisplayAttachment attachment, int posX, Integer posY, int posZ){
        LionDisplayData ld = new LionDisplayData(id, "compass", offsetX, offsetY, attachment);
        ld.setData("x", String.valueOf(posX));
        ld.setData("y", String.valueOf(posY));
        ld.setData("z", String.valueOf(posZ));
        sendDisplayData(p, ld);
    }
    public static void sendDisplayCompass(Player p, String id, int posX, Integer posY, int posZ){
        LionDisplayData ld = new LionDisplayData(id, "square");
        ld.setData("x", String.valueOf(posX));
        ld.setData("y", String.valueOf(posY));
        ld.setData("z", String.valueOf(posZ));
        sendDisplayData(p, ld);
    }
    public static void sendDisplayFrame(Player p, String id, Integer offsetX, Integer offsetY, @Nullable DisplayAttachment attachment, int color, int width, int height){
        LionDisplayData ld = new LionDisplayData(id, "frame", offsetX, offsetY, attachment);
        ld.setData("color", String.valueOf(color));
        ld.setData("width", String.valueOf(width));
        ld.setData("height", String.valueOf(height));
        sendDisplayData(p, ld);
    }
    public static void sendDisplayItem(Player p, String id, Integer offsetX, Integer offsetY, @Nullable DisplayAttachment attachment, ItemStack is){
        String serializedItemStack = is.getType()+":"+
                is.getAmount()+":"+
                (is.getItemMeta().hasEnchantmentGlintOverride()||is.getItemMeta().hasEnchants())+":"+
                getCustomData(is)+":"+getCustomModelString(is);
        LionDisplayData ld = new LionDisplayData(id, "item", offsetX, offsetY, attachment);
        ld.setData("item", serializedItemStack);
        sendDisplayData(p, ld);
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
    private static void sendMessage(Player p, String message){
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
                            LionChat.sendSystemMessage(Component.text("Your Client is outdated! Please update the LionDisplays Mod to use it's Features"), player);
                        }else{
                            Runtime.Version rv = Runtime.Version.parse(getStringAtIndex(1, data));
                            int equality = rv.compareTo(version);
                            if (equality == 0){
                                LionChat.sendSystemMessage(Component.text("Your Client was registered successfully"), player);
                                GUIPlayerManager.setRenderWay(player, GUIPlayerManager.ClientRenderWay.LIONDISPLAYS_MOD);
                            }else if(equality < 0)
                                LionChat.sendSystemMessage(Component.text("Your Client is outdated! Please update the LionDisplays Mod to use it's Features"), player);
                            else
                                LionChat.sendSystemMessage(Component.text("This Server is outdated! Please use Version "+version+" of the LionDisplays Mod to use it's features on this Server"), player);
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
