package de.lioncraft.lionapi.messageHandling.lang;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

/**This Class provides utility to manage language files.<br><br>
 * How to set up?<br>-----------------<br>
 * Create a folder called "lang" in your plugin's resource folder.<br>
 * Place all of your language files in it (should be named like "en_us.yml").<br>
 * Place an index.txt file in it containing every name of each language file (for example "en_us.yml", each name on a new line).<br>
 * Call {@link LanguageFileManager#saveLangFiles(Plugin)} on startup to save the files and allow the admin to edit them.
 * Create an instance of {@link LanguageFileManager} (either with {@link LanguageFileManager#LanguageFileManager(YamlConfiguration, Configuration)} of
 * with {@link LanguageFileManager#createManager(Plugin, String)}) and save it to a static variable so you can access it in other classes.
 */
public class LanguageFileManager {
    private static HashMap<String, String> colorCodeDefaultReplacements;

    public static @NotNull HashMap<String, String> getColorCodeDefaultReplacements() {
        if (colorCodeDefaultReplacements == null) {
            colorCodeDefaultReplacements = new HashMap<>();
            for (Map.Entry<String, Object> code : LionAPI.getPlugin().getConfig().getConfigurationSection("language-file-color-codes").getValues(false).entrySet()){
                colorCodeDefaultReplacements.put(code.getKey(), (String) code.getValue());
            }
        }
        return colorCodeDefaultReplacements;
    }

    /**
     *This method saves the lang files from a plugin. <br>
     * The plugin needs a resource folder named "lang" and an<br>
     * index.txt file in it that contains every language file's name.
     * Example content:<br>
     * de_de.yml<br>
     * en_us.yml<br>
     */
    public static void saveLangFiles(Plugin plugin){
        try (InputStream is = plugin.getResource("lang/index.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)))) {

            String resourcePath;
            Path output =  plugin.getDataPath().resolve("lang");
            // 4. Loop through each line (resource path) in index.txt
            while ((resourcePath = reader.readLine()) != null) {
                resourcePath = resourcePath.trim(); // Clean up whitespace

                if (!resourcePath.isEmpty()) {
                    saveResource("lang/"+resourcePath, output, plugin);
                }
            }

        } catch (IOException e) {
            plugin.getLogger().warning("Error reading index file or accessing resource stream: " + e.getMessage());
        } catch (NullPointerException e) {
            plugin.getLogger().warning("ERROR: Could not find 'lang/index.txt' in the classpath. Ensure it's in the JAR.");
        }
    }
    private static void saveResource(String resourcePath, Path outputDirPath, Plugin plugin) {
        // 5. Load the specific resource
        try (InputStream resourceStream = plugin.getResource(resourcePath)) {
            if (resourceStream == null) {
                plugin.getLogger().warning("  WARN: Resource not found (skipping): " + resourcePath);
                return;
            }

            // Get the file name from the path (e.g., "data/image.png" -> "image.png")
            Path targetFileName = Paths.get(resourcePath).getFileName();
            Path targetFilePath = outputDirPath.resolve(targetFileName);

            // 6. Save the resource stream to the local file
            try {
                if(!Files.exists(outputDirPath)){
                    Files.createDirectory(outputDirPath);
                }
                Files.copy(resourceStream, targetFilePath);
            }catch (FileAlreadyExistsException ignored){}
        } catch (IOException e) {
            plugin.getLogger().warning("  ERROR saving resource '" + resourcePath + "': " + e.getMessage());
        }
    }


    private YamlConfiguration texts;

    public LanguageFileManager(YamlConfiguration config, @Nullable Configuration defaultConfig){
        texts = config;
        if (defaultConfig != null) texts.setDefaults(defaultConfig);
    }

    public static LanguageFileManager createManager(Plugin plugin, String selectedLanguage){
        if (!selectedLanguage.endsWith(".yml")) selectedLanguage = selectedLanguage + ".yml";
        File f = plugin.getDataPath().resolve("lang").resolve(selectedLanguage).toFile();
        if (!f.exists()){
            saveLangFiles(plugin);
        }
        if (f.exists()){
            InputStream is = plugin.getResource("lang/"+selectedLanguage);
            if (is != null) return new LanguageFileManager(YamlConfiguration.loadConfiguration(f), YamlConfiguration.loadConfiguration(new InputStreamReader(is)));
            else return new LanguageFileManager(YamlConfiguration.loadConfiguration(f), null);
        }else {
            if (selectedLanguage.equals("en_us.yml")){
                plugin.getLogger().warning("The default language (en_us) could not be found. WARNING! This will significantly" +
                        " destroy this plugin, as most of the text cannot be displayed. Fix this ASAP!");
                return new LanguageFileManager(new YamlConfiguration(), null);
            } else {
                plugin.getLogger().warning("The language \"" + selectedLanguage + "\" could not be found, resetting to default.");
                return createManager(plugin, "en_us.yml");
            }
        }

    }

    private HashMap<String, String> colorCodeReplacements = new HashMap<>(getColorCodeDefaultReplacements());

    public HashMap<String, String> getColorCodeReplacements() {
        return colorCodeReplacements;
    }

    public void setColorCodeReplacements(HashMap<String, String> colorCodeReplacements) {
        this.colorCodeReplacements = colorCodeReplacements;
    }

    private String replaceColorCodes(String code){
        for (String s : getColorCodeReplacements().keySet()) {
            code = code.replace("<"+s+">", "<"+getColorCodeReplacements().get(s)+">");
        }
        return code;
    }

    public Component msg(String id, String... placeholders){
        return getMessage(id, placeholders);
    }
    public Component msg(String id){
        return getMessage(id, new String[0]);
    }
    public Component msg(String id, Component... placeholders){
        return getMessage(id, placeholders);
    }

    public String getMessageAsString(String id){
        return texts.getString(id, "Error: This Message cannot be displayed");
    }

    public Component getMessage(String id, String... placeholders){
        String text = texts.getString(id,
                "<hover:show_text:'TEXT ID: "+id+"'><red>Error: This Message cannot be displayed</hover>");
        for (String p : placeholders){
            if (text.contains("{}")){
                text = text.replaceFirst("[{][}]", p);
            }else {
                LionChat.sendLogMessage("Message \"" + id + "\" does not contain enough placeholders. Make sure to adjust this in the language files");
                break;
            }
        }
        return MiniMessage.miniMessage().deserialize(replaceColorCodes(text));
    }
    public Component getMessage(String id, Component... placeholders){
        String text = texts.getString(id,
                "<hover:show_text:'TEXT ID: "+id+"'><red>Error: This Message cannot be displayed</hover>");
        text = replaceColorCodes(text);
        List<String> textparts = new ArrayList<>(List.of(text.split("\\{}")));
        Component c = Component.text("");
        for (Component p : placeholders){
            if (textparts.isEmpty()){
                LionChat.sendLogMessage("Message \"" + id + "\" does not contain enough placeholders. Make sure to adjust this in the language files");
                break;
            }
            c = c.append(MiniMessage.miniMessage().deserialize(textparts.get(0)))
                    .append(p);
            textparts.remove(0);
        }
        if (!textparts.isEmpty()){
            for (String s : textparts) {
                c = c.append(MiniMessage.miniMessage().deserialize(s));
            }
        }
        return c;
    }
}
