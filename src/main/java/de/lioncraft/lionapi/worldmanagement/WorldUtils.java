package de.lioncraft.lionapi.worldmanagement;

import de.lioncraft.lionapi.LionAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public final class WorldUtils {
    private WorldUtils(){}
    public static CompletableFuture<World> CloneWorld(World w, boolean overrideExistingData){
        String s = getNewName(w.getName());
        CompletableFuture<World> world = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(LionAPI.getPlugin(), () -> {
            File newFolder = new File(Bukkit.getWorldContainer(), s);
            if (!newFolder.exists()) {
                newFolder.mkdir();
            }
            if (overrideExistingData) {
                try {
                    copyFolder(w.getWorldFolder().toPath(), newFolder.toPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            World newworld = Bukkit.createWorld(new WorldCreator(s).copy(w));
            for (GameRule g : GameRule.values()) {
                if(w.getGameRuleValue(g)!=null){
                    newworld.setGameRule(g, w.getGameRuleValue(g));
                }
            }
            world.complete(newworld);
        });
        return world;
    }

    public static String getNewName(String previous){
        if (Pattern.matches("[0-9]",String.valueOf(previous.charAt(previous.length()-1)))){
            StringBuilder number = new StringBuilder();
            StringBuilder name = new StringBuilder();
            StringBuilder temp = new StringBuilder();
            for (char c : previous.toCharArray()){
                if (Character.isDigit(c)){
                    number.append(c);
                    temp.append(c);
                }else {
                    number = new StringBuilder();
                    name.append(temp);
                    temp = new StringBuilder();
                    name.append(c);
                }
            }
            int i = Integer.parseInt(number.toString());
            return name.append(i).toString();
        }
        return "world"+new Random().nextInt(99999);
    }
    private static void copyFolder(Path src, Path dest) throws IOException {
        Files.walk(src)
                .forEach(source -> {
                    try {
                        Path destination = dest.resolve(src.relativize(source));
                        if (Files.isDirectory(source)) {
                            if (!Files.exists(destination)) {
                                Files.createDirectories(destination);
                            }
                        } else {
                            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to copy " + source + " to " + dest, e);
                    }
                });
    }
}
