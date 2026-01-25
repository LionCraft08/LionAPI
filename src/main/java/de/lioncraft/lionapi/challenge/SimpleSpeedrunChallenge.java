package de.lioncraft.lionapi.challenge;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.Interaction.Setting;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.MainMenu;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.playerSettings.PlayerSettings;
import de.lioncraft.lionapi.timer.MainTimer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Objects;

import static de.lioncraft.lionapi.LionAPI.lm;
import static org.bukkit.Bukkit.getServer;

public class SimpleSpeedrunChallenge extends AdvancedController {
    public SimpleSpeedrunChallenge() {
        super(true, true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LionChat.sendLogMessage("Challenge resumed");
    }

    @Override
    public boolean canBePaused() {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        LionChat.sendLogMessage("Challenge paused");
        getServer().playSound(Sound.sound(Key.key("block.beacon.deactivate"), Sound.Source.MASTER, 2f, 1f));
//        getServer().getServerTickManager().setFrozen(true);
//
//        PlayerSettings.getSettings(null).setInvulnerable(true);
//        PlayerSettings.getSettings(null).setCanMineBlocks(false);
//        PlayerSettings.getSettings(null).setCanHitEntities(false);
//        PlayerSettings.getSettings(null).setCanPickupItems(false);

    }

    @Override
    protected void onFinish(ChallengeEndData data) {
        super.onFinish(data);
        if (data.isSuccessful()){
            getServer().sendMessage(Component.text("--------------------------------", TextColor.color(0, 255, 255)));
            getServer().sendMessage(lm().msg("challenges.simple.end.dragon"));
            getServer().sendMessage(lm().msg("challenges.simple.end.time").append(MainTimer.getTimer().getMessage()));
            getServer().sendMessage(Component.text("--------------------------------", TextColor.color(0, 255, 255)));
            getServer().playSound(Sound.sound(Key.key("ui.toast.challenge_complete"), Sound.Source.MASTER, 2f, 1f));
        }else{
            Component type = switch ((String) data.getArgs().get("type")){
                case "playerDeath" -> lm().msg("challenges.simple.end.player", (String) data.getArgs().get("player"));
                case "timerExpired" -> lm().msg("challenges.simple.end.timer");
                default -> lm().msg("challenges.simple.end.default");
            };
            getServer().sendMessage(Component.text("--------------------------------", TextColor.color(150, 0, 200)));
            getServer().sendMessage(lm().msg("challenges.simple.end.lost"));
            getServer().sendMessage(type);
            getServer().sendMessage(lm().msg("challenges.simple.end.time").append(MainTimer.getTimer().getMessage()));
            getServer().sendMessage(Component.text("--------------------------------", TextColor.color(150, 0, 200)));
            getServer().playSound(Sound.sound(Key.key("block.beacon.deactivate"), Sound.Source.MASTER, 2f, 1f));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getServer().playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 1f, 1f));
//        getServer().getServerTickManager().setFrozen(false);
//
//        PlayerSettings.getSettings(null).setInvulnerable(false);
//        PlayerSettings.getSettings(null).setCanMineBlocks(true);
//        PlayerSettings.getSettings(null).setCanHitEntities(true);
//        PlayerSettings.getSettings(null).setCanPickupItems(true);
//        PlayerSettings.getSettings(null).setCanMove(true);
    }

//    @Override
//    protected void onLoad() {
//        Bukkit.getServerTickManager().setFrozen(true);
//        Bukkit.getServer().getWorld("world").setTime(1000);
//
//        PlayerSettings.getSettings(null).setInvulnerable(true);
//        PlayerSettings.getSettings(null).setCanMineBlocks(false);
//        PlayerSettings.getSettings(null).setCanHitEntities(false);
//        PlayerSettings.getSettings(null).setCanPickupItems(false);
//        PlayerSettings.getSettings(null).setCanChat(true);
//        PlayerSettings.getSettings(null).setCanMove(true);
//        PlayerSettings.getSettings(null).setCanFly(false);
//    }

    @Override
    protected void onJoin(Player p) {

    }

    private Inventory inv;

    public SimpleSpeedrunChallenge(Map<String, Object> map){
        super(map);
    }
    @Override
    public @NonNull Map<String, Object> serialize() {
        return super.serialize();
    }


    @Override
    protected Inventory getConfigInventory() {
        if (inv == null) buildInv();
        return inv;
    }

    private void buildInv(){
        inv = Bukkit.createInventory(null, 54, Component.text("Simple Challenge Settings"));
        inv.setContents(Items.blockButtons);
        inv.setItem(49, Items.closeButton);

        inv.setItem(45, MainMenu.getToMainMenuButton());
    }


}
