package de.lioncraft.lionapi.addons;

import com.google.errorprone.annotations.ForOverride;
import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAddon {
    private String id;
    private Component name;
    protected abstract void onLoad();
    protected abstract void onUnload();
    private List<Listener> listeners = new ArrayList<>();
    private boolean useOwnChannel = true;
    private boolean enabled = false;

    public AbstractAddon(String id, Component name) {
        this.id = id.replace(" ", "_").toLowerCase();
        this.name = name;
    }

    /**Registers a Listener that can be disabled via {@link #unloadEvents()}
     * Note: {@link #unloadEvents()} must be invoked when overriding the {@link #onUnload()} Function for this to work
     * @param l The Event Listener Object.
     */
    public void registerEvent(Listener l){
        Bukkit.getServer().getPluginManager().registerEvents(l, LionAPI.getPlugin());
        listeners.add(l);
    }

    public boolean load(){
        if (enabled) return false;
        try {
            onLoad();
        } catch (Exception e) {
            return false;
        }
        enabled = true;
        return true;
    }

    public boolean unload(){
        if (!enabled) return false;
        try {
            onUnload();
        } catch (Exception e) {
            return false;
        }
        enabled = false;
        return true;
    }

    /**Unloads every Event registered via @{@link #registerEvent(Listener)}
     * Has to be called manually overriding the onUnload method
     */
    public void unloadEvents(){
        for (Listener l : listeners){
            for(Method method : l.getClass().getMethods()){
                if (method.isAnnotationPresent(EventHandler.class)){
                    Class<Event> clazz = (Class<Event>) method.getParameters()[0].getType();
                    try {
                        ((HandlerList) clazz.getDeclaredMethod("getHandlerList").invoke(null)).unregister(l);
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        Bukkit.getServer().getConsoleSender().sendMessage("Seems like there is an invalid Event Listener for \""+
                                clazz.getName() +"\" in "+l.getClass().getName());
                    }
                }
            }
        }
    }

    public String getId() {
        return id;
    }

    public Component getName() {
        return name;
    }

    public void setName(Component name) {
        this.name = name;
    }

    public boolean isUseOwnChannel() {
        return useOwnChannel;
    }

    public void setUseOwnChannel(boolean useOwnCHannel) {
        this.useOwnChannel = useOwnCHannel;
        if (useOwnCHannel){
            AddonManager.registerAddonChannel(this);
        }
    }

    public void sendMessage(Component message, Audience player){
        if (isUseOwnChannel()){
            LionChat.sendMessageOnChannel(getId(), message, player);
        }else{
            LionChat.sendSystemMessage(message, player);
        }

    }

    public void sendMessage(String message, Audience player){
        if (isUseOwnChannel()){
            LionChat.sendMessageOnChannel(getId(), MiniMessage.miniMessage().deserialize(message), player);
        }else{
            LionChat.sendSystemMessage(message, player);
        }
    }

    /** When set to null, no settings Icon will show up in the LionAPI Config Menu.
     * @return the Settings-Button or null
     */
    @ForOverride
    public @Nullable ItemStack getSettingsIcon(){
        return null;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
