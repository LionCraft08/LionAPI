package de.lioncraft.lionapi.timer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import de.lioncraft.lionapi.LionAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtocolLibBridge extends PacketAdapter {
    private static ProtocolLibBridge instance;
    public static void register(){
        if(instance == null){
            instance = new ProtocolLibBridge();
        }
        ProtocolLibrary.getProtocolManager().addPacketListener(instance);
    }

    public static void unregister(){
        ProtocolLibrary.getProtocolManager().removePacketListener(instance);
        instance = null;

    }

    Pattern pattern = Pattern.compile("^(?:(\\d+d)\\s+)?(?:(\\d+h)\\s+)?(?:(\\d+m)\\s+)?(\\d+s)");

    public ProtocolLibBridge() {
        super(LionAPI.getPlugin(), PacketType.Play.Server.SET_ACTION_BAR_TEXT, PacketType.Play.Server.SYSTEM_CHAT);
    }

    @Override
    public void onPacketSending(PacketEvent e) {
        PacketContainer packet = e.getPacket();

        if(e.getPacketType() == PacketType.Play.Server.SYSTEM_CHAT) {
            if(!packet.getBooleans().read(0)) return;
        }


        // Use ProtocolLib's ChatComponent converter
        WrappedChatComponent component = packet.getChatComponents().read(0);
        String jsonText = component.getJson();
        Component textComponent = GsonComponentSerializer.gson().deserialize(jsonText);

        if (!isTimer(textComponent)) {
            //Prevent flickering messages on the client
            e.setCancelled(true);
            if(textComponent instanceof TranslatableComponent tc){
                // Check for miscasts: Integer values are sometimes printed as floats,
                // resulting in messages like '1.0/2.0 players sleeping' to occur
                List<TranslationArgument> cleanArgs = new ArrayList<>();
                tc.arguments().forEach(translationArgument -> {
                    if(translationArgument.value() instanceof Number n){
                        if(n.intValue() == n.floatValue()){
                            cleanArgs.add(TranslationArgument.numeric(n.intValue()));
                            return;
                        }
                    }
                    cleanArgs.add(translationArgument);
                });
                textComponent = tc.arguments(cleanArgs);


            }
            MainTimer.setPlayerSuffix(e.getPlayer(), textComponent, 80);
        }

    }


    /**
     * Checks whether a given hotbar message contains a Timer from LionAPI.
     * @param component The message to check.
     * @return True if the message contains a Timer, false otherwise.
     */
    private boolean isTimer(Component component) {
        if(component.hoverEvent() != null){
            return component.hoverEvent().equals(TimerLike.getUniqueIdentifier());
        }
        if(true) return false;

        //Alternative approach to detect the content of the Timer failed when messages like 'Timer Paused' were displayed
        String s = PlainTextComponentSerializer.plainText().serialize(component);
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }
}
