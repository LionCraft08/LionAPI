package de.lioncraft.lionapi.actions;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.Items;
import de.lioncraft.lionapi.playerSettings.PlayerSettings;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum LionActions {
    FREEZE_WORLD(
            Items.get(
                    LionAPI.lm().msg("inv.actions.freeze-world.title"),
                    Material.BLUE_ICE,
                    LionAPI.lm().getMessageAsList("inv.actions.freeze-world.lore")
            ),
            b -> Bukkit.getServerTickManager().setFrozen(b)
    ),
    FREEZE_PLAYERS(
            Items.get(
                    LionAPI.lm().msg("inv.actions.freeze-players.title"),
                    Material.CHAINMAIL_BOOTS,
                    LionAPI.lm().getMessageAsList("inv.actions.freeze-players.lore")
            ),
            b -> PlayerSettings.getSettings(null).setCanMove(!b)
    ),
    INVULNERABLE_PLAYERS(
            Items.get(
                    LionAPI.lm().msg("inv.actions.invulnerable-players.title"),
                    Material.SHIELD,
                    LionAPI.lm().getMessageAsList("inv.actions.invulnerable-players.lore")
            ),
            b -> PlayerSettings.getSettings(null).setInvulnerable(b)

    ),
    BLOCK_WORLD_INTERACTIONS(
            Items.get(
                    LionAPI.lm().msg("inv.actions.block-world-interactions.title"),
                    Material.STRUCTURE_BLOCK,
                    LionAPI.lm().getMessageAsList("inv.actions.block-world-interactions.lore")
            ),
            b -> {
                PlayerSettings.getSettings(null).setCanMineBlocks(!b);
                PlayerSettings.getSettings(null).setCanHitEntities(!b);
                PlayerSettings.getSettings(null).setCanPickupItems(!b);
            }
    ),
    ALLOW_PVP(
            Items.get(
                    LionAPI.lm().msg("inv.actions.allow-pvp.title"),
                    Material.END_CRYSTAL,
                    LionAPI.lm().getMessageAsList("inv.actions.allow-pvp.lore")
            ),
            b ->
                Bukkit.getWorlds().forEach(world -> {
                    world.setGameRule(GameRules.PVP, b);
                })

    ),
    RESET_PLAYER_HEALTH(
            Items.get(
                    LionAPI.lm().msg("inv.actions.reset-health.title"),
                    Material.GOLDEN_APPLE,
                    LionAPI.lm().getMessageAsList("inv.actions.reset-health.lore")
            ),
            b -> {
                if (b)
                    for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getValue());
                    player.setFoodLevel(20);
                    player.setSaturation(5);
                }
            }
    ),
    ALLOW_FLYING(
            Items.get(
                    LionAPI.lm().msg("inv.actions.allow-flying.title"),
                    Material.PHANTOM_MEMBRANE,
                    LionAPI.lm().getMessageAsList("inv.actions.allow-flying.lore")
            ),
            b -> {
                PlayerSettings.getSettings(null).setCanFly(b);
            }
    ),
    RESET_WORLD_TIME(
            Items.get(
                    LionAPI.lm().msg("inv.actions.reset-world-time.title"),
                    Material.CLOCK,
                    LionAPI.lm().getMessageAsList("inv.actions.reset-world-time.lore")
            ),
            b -> {
                for (World w : Bukkit.getWorlds()) {
                    w.setTime(1000);
                }
            }
    ),
    SPAWN_STRUCTURE(
            Items.get(
                    LionAPI.lm().msg("inv.actions.spawn-structure.title"),
                    Material.SPRUCE_LOG,
                    LionAPI.lm().getMessageAsList("inv.actions.spawn-structure.lore")
            ),
            b -> {
                ActionPlaceholder.getPlaceholder("spawn_structure").accept(b);
            }
    )
    ;

    private final ItemStack configPreset;
    private final BooleanConsumer onValueChange;
    LionActions(ItemStack configPreset, BooleanConsumer onValueChange){
        this.configPreset = configPreset;
        this.onValueChange = onValueChange;
    }

    public ItemStack getConfigPreset() {
        return configPreset;
    }

    public void onValueChange(boolean newValue) {
        onValueChange.accept(newValue);
    }

    public BooleanConsumer getOnValueChange() {
        return onValueChange;
    }
}
