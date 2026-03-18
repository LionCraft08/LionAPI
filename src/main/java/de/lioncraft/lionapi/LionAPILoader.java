package de.lioncraft.lionapi;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.JarLibrary;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * Why does this class exist?
 * This was an approach to add other Plugins that depend on this one to the class loader.
 * Doesn't work as intended, so it is currently unused.
 */
public class LionAPILoader implements PluginLoader {
    private static final String MY_PLUGIN_NAME = "lionAPI";

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        File pluginsFolder = new File("plugins");
        if (!pluginsFolder.exists() || !pluginsFolder.isDirectory()) return;

        File[] files = pluginsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (files == null) return;

        Yaml yaml = new Yaml();

        for (File file : files) {
            try (JarFile jarFile = new JarFile(file)) {
                // Check both standard and Paper-specific config files
                JarEntry entry = jarFile.getJarEntry("paper-plugin.yml");
                if (entry == null) entry = jarFile.getJarEntry("plugin.yml");
                if (entry == null) continue;

                try (InputStream is = jarFile.getInputStream(entry)) {
                    Map<String, Object> data = yaml.load(is);
                    if (data == null) continue;

                    System.out.println("----------------------");
                    System.out.println("Checking " + file.getName());

                    if (dependsOnMe(data)) {
                        // Add the actual JAR file to our classpath
                        classpathBuilder.addLibrary(new JarLibrary(file.toPath()));
                        System.out.println(file.toPath() + " has been added as a dependency to LionAPI!");
                    }
                    System.out.println("----------------------");
                }
            } catch (Exception e) {
                // Log via System.err as Logger is often not ready
                System.err.println("Failed to scan potential dependent: " + file.getName());
            }
        }
    }

    private boolean dependsOnMe(Map<String, Object> data) {
        // 1. Check legacy Bukkit "depend" and "softdepend"
        if (isNameInList(data.get("depend")) || isNameInList(data.get("softdepend"))) {
            System.out.println("Found Bukkit Dependency");
            return true;
        }

        System.out.println("Checking Paper-specific dependencies");

        // 2. Check Paper-specific "dependencies"
        Object deps = data.get("dependencies");
        if (deps instanceof Map<?, ?> map) {
            System.out.println("Found Paper Dependencies: "+ map.toString());

            // Paper dependencies can be nested under server/bootstrap/etc.
            if(map.containsKey("server") && map.get("server") instanceof Map<?, ?> serverMap){
                System.out.println("Found Server Dependencies: "+ serverMap);
                return serverMap.containsKey(MY_PLUGIN_NAME);
            }

            return map.containsKey(MY_PLUGIN_NAME);
        } else if (deps instanceof List<?> list) {
            System.out.println("Checking dependencies as a List");
            return list.stream().anyMatch(obj -> MY_PLUGIN_NAME.equals(obj.toString()));
        }

        return false;
    }

    private boolean isNameInList(Object obj) {
        if (obj instanceof List<?> list) {
            return list.contains(MY_PLUGIN_NAME);
        } else if (obj instanceof String str) {
            return str.equals(MY_PLUGIN_NAME);
        }
        return false;
    }

//    // Helper implementation for Path-based libraries
//    private record JarLibrary(Path path) implements ClassPathLibrary {
//        @Override
//        public void register(@NotNull LibraryStore store) {
//            store.addLibrary(path);
//        }
//    }
}
