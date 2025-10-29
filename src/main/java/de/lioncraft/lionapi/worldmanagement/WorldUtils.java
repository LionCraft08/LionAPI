package de.lioncraft.lionapi.worldmanagement;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public final class WorldUtils {
    private WorldUtils(){}

    public static CompletableFuture<World> cloneWorld(World w, boolean overrideExistingData){
        return cloneWorld(w, overrideExistingData, getNewName(w.getName()));
    }

    public static CompletableFuture<World> cloneWorld(World w, boolean overrideExistingData, String str){
        String s = toValidWindowsFolderName(str);
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
            Bukkit.getScheduler().runTask(LionAPI.getPlugin(), () -> {
                World newworld = Bukkit.createWorld(new WorldCreator(s).copy(w));
                for (String gamerule : w.getGameRules()) {
                    GameRule g = GameRule.getByName(gamerule);
                    newworld.setGameRule(g, w.getGameRuleValue(g));

                }
                world.complete(newworld);
            });

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
                    if (source.endsWith("uid.dat")|| source.endsWith("session.lock")) return;
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
                        LionChat.sendLogMessage("Couldn't copy "+src+" to "+dest+": "+e.getMessage());
                    }
                });
    }

    private static final Pattern RESERVED_NAMES = Pattern.compile(
            "^(CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])$", Pattern.CASE_INSENSITIVE
    );

    // Invalid characters for Windows folder names: < > : " / \\ | ? *
    private static final String INVALID_CHARS_REGEX = "[<>:\"/\\\\|?*]";

    /**
     * Converts an input string into a valid folder name for the Windows operating system.
     * * It handles:
     * 1. Invalid characters (replaces them with a single underscore '_').
     * 2. Leading/Trailing spaces and periods (removes them).
     * 3. Windows reserved names (appends "_invalid").
     * 4. Maximum path length (truncates to a safe limit, typically 255 for a single component).
     * * @param input The original string.
     * @return A Windows-compatible folder name string.
     */
    public static String toValidWindowsFolderName(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "NewFolder"; // Default safe name
        }

        String safeName = input.trim();

        safeName = safeName.toLowerCase();

        safeName = safeName.replace("ö", "oe");
        safeName = safeName.replace("ä", "ae");
        safeName = safeName.replace("ü", "ue");

        // 1. Replace invalid characters with an underscore
        safeName = safeName.replaceAll(INVALID_CHARS_REGEX, "_");
        safeName = safeName.replaceAll("[^a-z0-9_\\-./]", "_");

        // 2. Remove trailing periods (Windows doesn't like names ending in a period)
        safeName = safeName.replaceAll("\\.+$", "");

        // 3. Truncate to a reasonable length (Windows max path component is usually 255)
        // We'll use 200 for extra safety, as full path limit is 260.
        if (safeName.length() > 200) {
            safeName = safeName.substring(0, 200);
        }

        // 4. Handle Windows reserved names (e.g., CON, AUX, NUL)
        if (RESERVED_NAMES.matcher(safeName).matches()) {
            safeName += "_invalid";
        }

        // Final trim again, just in case truncation left a trailing space or period
        safeName = safeName.trim();

        // Ensure it's not empty after all replacements
        if (safeName.isEmpty()) {
            return "ValidNameFallback";
        }

        return safeName;
    }
}
