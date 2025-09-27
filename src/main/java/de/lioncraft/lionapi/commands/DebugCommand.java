package de.lioncraft.lionapi.commands;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.pluginPlusAPI.ParticleDelayGenerator;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class DebugCommand implements BasicCommand {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (sender instanceof Player p){
            if (args.length>=4){
                new ParticleDelayGenerator(Integer.parseInt(args[2]), p, Double.parseDouble(args[3]), 100).runTaskTimer(LionAPI.getPlugin(), Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            }else{
                try {
                    Path path = LionAPI.getPlugin().getDataPath().resolve("items/item.yml");
                    ItemStack is = ItemStack.of(
                            Material.STONE,
                            12
                    );
                    is.lore(List.of(
                            MiniMessage.miniMessage().deserialize("<red>Hallo Welt"),
                            MiniMessage.miniMessage().deserialize("<rainbow>kurzer text")
                    ));
                    is.editMeta(itemMeta -> {
                        itemMeta.displayName(MiniMessage.miniMessage().deserialize("<blue>Name dieses Gegenstandes"));
                        itemMeta.setHideTooltip(true);
                        itemMeta.setEnchantmentGlintOverride(true);
                    });
                    if(path.toFile().createNewFile()){
                        YamlConfiguration yml = YamlConfiguration.loadConfiguration(path.toFile());
                        yml.set("data", is.serialize());
                        yml.save(path.toFile());
                    }else{
                        YamlConfiguration yml = YamlConfiguration.loadConfiguration(path.toFile());
                        p.give(ItemStack.deserialize(yml.getValues(false)));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] strings) {
        onCommand(commandSourceStack.getSender(), strings);
    }
}
