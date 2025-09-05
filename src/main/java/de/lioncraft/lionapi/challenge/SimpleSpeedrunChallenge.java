package de.lioncraft.lionapi.challenge;

import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.guimanagement.MainMenu;
import de.lioncraft.lionapi.messageHandling.DM;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.timer.MainTimer;
import de.lioncraft.lionutils.utils.Settings;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getServer;

public class SimpleSpeedrunChallenge extends ChallengeController {
    public SimpleSpeedrunChallenge(Map<String, Object> map){
        super(map);
    }
    public SimpleSpeedrunChallenge() {
        super(true, true);
    }

    @Override
    protected void onStart() {
        onResume();
        LionChat.sendLogMessage("Challenge wurde gestartet!");
    }

    @Override
    public boolean canBePaused() {
        return true;
    }

    @Override
    protected void onPause() {
        LionChat.sendLogMessage("Die Challenge wurde pausiert.");
        getServer().playSound(Sound.sound(Key.key("block.beacon.deactivate"), Sound.Source.MASTER, 2f, 1f));
        getServer().getServerTickManager().setFrozen(true);
        if (getPluginManager().isPluginEnabled("LionUtils")){
            Settings.getSettings(null).setInvulnerable(true);
            Settings.getSettings(null).setCanMineBlocks(false);
            Settings.getSettings(null).setCanHitEntities(false);
            Settings.getSettings(null).setCanPickupItems(false);
            Settings.getSettings(null).setCanMove(false);
        }
    }

    @Override
    protected void onFinish(ChallengeEndData data) {
        if (data.isSuccessful()){
            getServer().sendMessage(Component.text("--------------------------------", TextColor.color(0, 255, 255)));
            getServer().sendMessage(Component.text("Der Ender Dragon ist gestorben!", TextColor.color(0, 255, 25)));
            getServer().sendMessage(Component.text("Zeit: ", TextColor.color(20, 200, 20)).append(MainTimer.getTimer().getMessage()));
            getServer().sendMessage(Component.text("--------------------------------", TextColor.color(0, 255, 255)));

        }else{
            String type = switch ((String) data.getArgs().get("type")){
                case "playerDeath" -> "<#FF6419>Der Spieler <#00FFFF>"+data.getArgs().get("player")+"<#FF6419> ist gestorben.";
                case "timerExpired" -> "Der Timer ist abgelaufen";
                default -> "/reset to try again";
            };
            getServer().sendMessage(Component.text("--------------------------------", TextColor.color(150, 0, 200)));
            getServer().sendMessage(Component.text("Challenge verloren!", TextColor.color(255, 100, 25)));
            getServer().sendMessage(MiniMessage.miniMessage().deserialize(type));
            getServer().sendMessage(Component.text("Zeit: ", TextColor.color(255, 100, 25)).append(MainTimer.getTimer().getMessage()));
            getServer().sendMessage(Component.text("--------------------------------", TextColor.color(150, 0, 200)));
            for (Player p : Bukkit.getOnlinePlayers()){
                p.setGameMode(GameMode.SPECTATOR);
            }
        }

    }

    @Override
    protected void onResume() {
        getServer().playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 1f, 1f));
        getServer().getServerTickManager().setFrozen(false);
        if (getPluginManager().isPluginEnabled("LionUtils")){
            Settings.getSettings(null).setInvulnerable(false);
            Settings.getSettings(null).setCanMineBlocks(true);
            Settings.getSettings(null).setCanHitEntities(true);
            Settings.getSettings(null).setCanPickupItems(true);
            Settings.getSettings(null).setCanMove(true);
        }
    }

    @Override
    protected void onLoad() {
        Bukkit.getServerTickManager().setFrozen(true);
        Bukkit.getServer().getWorld("world").setTime(1000);
        getSettings().setChallengeEndsOnPlayerDeath(true);
        getSettings().setChallengeEndsOnDragonDeath(true);
        if (getPluginManager().isPluginEnabled("LionUtils")){
            Settings.getSettings(null).setInvulnerable(true);
            Settings.getSettings(null).setCanMineBlocks(false);
            Settings.getSettings(null).setCanHitEntities(false);
            Settings.getSettings(null).setCanPickupItems(false);
        }
    }

    @Override
    protected void onJoin(Player p) {

    }

    private Inventory inv;

    @Override
    protected Inventory getConfigInventory(Player user) {
        if (inv == null) buildInv();
        return inv;
    }

    private void buildInv(){
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Simple Challenge Settings"));
        inv.setContents(Items.blockButtons);
        inv.setItem(49, Items.closeButton);
        inv.setItem(45, MainMenu.getToMainMenuButton());
    }


}
