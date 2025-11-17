package de.lioncraft.lionapi.features.customcode;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.PluginClassLoader;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class CodeExecutor {
    private static int classNameCounter = 0;
    private static int getNextCounter(){
        classNameCounter++;
        return classNameCounter;
    }
    private static int getClassNameCounter(){
        return classNameCounter;
    }
    public enum CodePrepareOptions{
        EMPTY, ADD_CLASS, ADD_FUNCTION
    }
    public static String prepareCodeString(String code, CodePrepareOptions cpo){
        return switch (cpo){
            case EMPTY -> code;
            case ADD_CLASS -> "package de.lioncraft.lionapi.features.customcode;\n" +
                    "import de.lioncraft.lionapi.messageHandling.MSG;\n" +
                    "import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;\n" +
                    "import de.lioncraft.lionapi.timer.MainTimer;\n" +
                    "import org.bukkit.Bukkit;" +
                    "import net.kyori.adventure.text.Component;" +
                    "import net.kyori.adventure.text.format.*;" +
                    "public class CustomClass"+getNextCounter()+" {\n" +
                    code+
                    "\n" +
                    "}";
            case ADD_FUNCTION -> prepareCodeString(
                    "    public static void execute(){" +
                            code+"\n" +
                            "}", CodePrepareOptions.ADD_CLASS
            );

        };
    }

    public static void compileAndExecuteAsync(String code){
        Bukkit.getAsyncScheduler().runNow(LionAPI.getPlugin(), scheduledTask -> {
            try {
                compileAndExecute(null, prepareCodeString(code, CodePrepareOptions.ADD_FUNCTION));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Compiles and executes the 'execute()' method from a given Java source code string.
     *
     * @param className   The fully qualified name of the class to compile (e.g., "my.pkg.MyClass")
     * @param sourceCode  The Java source code as a string.
     * @return The object returned by the 'execute()' method.
     * @throws Exception if compilation, instantiation, or execution fails.
     */
    public static Object compileAndExecute(String className, String sourceCode) throws Exception {
        if (!LionAPI.getPlugin().getConfig().getBoolean("settings.allow-code-execution")){
            LionChat.sendLogMessage("A unknown source tried to compile and execute custom string/bytecode." +
                    "The action was blocked by the system, because code execution is blocked in the settings." +
                    "Check the console for more details"
            );
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            LionAPI.getPlugin().getLogger().warning("See below to identify the source of the custom code compilation request:");

            for (StackTraceElement element : stackTrace) {
                LionAPI.getPlugin().getLogger().info(element.toString());
            }
            return 0;
        }

        if (className == null) className = "de.lioncraft.lionapi.features.customcode.CustomClass"+getClassNameCounter();

        // 1. Get the system Java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new RuntimeException("JDK required (running on JRE?). No JavaCompiler found.");
        }

        // 2. Set up a diagnostic listener to capture compilation errors
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        // 3. Set up an in-memory file manager
        InMemoryFileManager fileManager = new InMemoryFileManager(compiler.getStandardFileManager(diagnostics, null, null));

        // 4. Create a JavaFileObject from the source code string
        JavaFileObject sourceFile = new StringSourceFile(className, sourceCode);

        ClassLoader pluginClassLoader = LionAPI.getLionAPIClassLoader();

        // 5. Build the complete classpath
        List<String> options = new ArrayList<>();
        options.add("-classpath");

        // Use a Set to avoid duplicate paths
        Set<String> classPathEntries = new HashSet<>();

        // 1. Get URLs from the plugin's own classloader
        if (pluginClassLoader instanceof URLClassLoader) {
            for (URL url : ((URLClassLoader) pluginClassLoader).getURLs()) {
                classPathEntries.add(new File(url.toURI()).getAbsolutePath());
            }
        } else LionChat.sendDebugMessage("Plugin Class Loader is not a URLClassLoader");

        // 2. Get URLs from the Bukkit API's classloader (THE KEY FIX)
        // We get this from a class we know is in the API, like org.bukkit.Bukkit
        ClassLoader bukkitClassLoader = Bukkit.class.getClassLoader();
        if (bukkitClassLoader instanceof URLClassLoader) {
            for (URL url : ((URLClassLoader) bukkitClassLoader).getURLs()) {
                classPathEntries.add(new File(url.toURI()).getAbsolutePath());
            }
        }

        // 3. Get URLs from the Bukkit API's classloader (THE KEY FIX)
        // We get this from a class we know is in the API
        ClassLoader kyoryClassLoader = Component.class.getClassLoader();
        if (kyoryClassLoader instanceof URLClassLoader) {
            for (URL url : ((URLClassLoader) kyoryClassLoader).getURLs()) {
                classPathEntries.add(new File(url.toURI()).getAbsolutePath());
            }
        }

        // Build the final classpath string from the Set
        StringBuilder cp = new StringBuilder();
        for (String entry : classPathEntries) {
            cp.append(entry).append(File.pathSeparator);
        }

        options.add(cp.toString());

        JavaCompiler.CompilationTask task = compiler.getTask(
                null,       // Output writer
                fileManager, // File manager
                diagnostics, // Diagnostic listener
                options,       // Compiler options
                null,       // Classes for annotation processing
                Arrays.asList(sourceFile) // Source files to compile
        );

        boolean success = task.call();

        // 6. Check for compilation errors and report them
        if (!success) {
            StringBuilder sb = new StringBuilder("Compilation failed:\n");
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                String s = "";
                if(diagnostic.getSource() != null)  {
                    s = diagnostic.getSource().toUri().toString();
                }else s = "<empty>";
                sb.append(String.format("Error on line %d in %s%n%s%n",
                        diagnostic.getLineNumber(),
                        s,
                        diagnostic.getMessage(null)));
            }
            throw new RuntimeException(sb.toString());
        }

        // 7. Create a new class loader to load our compiled class
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        BytecodeClassLoader byteClassLoader = new BytecodeClassLoader(fileManager.getBytecode(), currentClassLoader);

        // 8. Load the class by name
        Class<?> compiledClass = byteClassLoader.loadClass(className);

        // 9. Instantiate the class (assumes a public no-arg constructor)
        Object instance = compiledClass.getConstructor().newInstance();

        // 10. Find the 'execute' method (assumes no parameters)
        Method executeMethod = compiledClass.getMethod("execute");

        // 11. Invoke the 'execute' method and return its result
        return executeMethod.invoke(instance);
    }

}

// --- Helper Classes for In-Memory Compilation ---

/**
 * A JavaFileObject that holds source code in a String.
 */
class StringSourceFile extends SimpleJavaFileObject {
    private final String code;

    StringSourceFile(String name, String code) {
        // Create a URI for the class name
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}

/**
 * A JavaFileObject that holds compiled bytecode in a byte array.
 */
class ClassOutputBuffer extends SimpleJavaFileObject {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    ClassOutputBuffer(String name) {
        super(URI.create("memory:///" + name.replace('.', '/') + Kind.CLASS.extension), Kind.CLASS);
    }

    public byte[] getBytes() {
        return outputStream.toByteArray();
    }

    @Override
    public OutputStream openOutputStream() {
        return outputStream;
    }
}

/**
 * A custom File Manager that stores compiled bytecode in memory.
 */
class InMemoryFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
    private final Map<String, ClassOutputBuffer> compiledClasses = new HashMap<>();

    InMemoryFileManager(StandardJavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        if (kind == JavaFileObject.Kind.CLASS) {
            ClassOutputBuffer outputBuffer = new ClassOutputBuffer(className);
            compiledClasses.put(className, outputBuffer);
            return outputBuffer;
        } else {
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }
    }

    /**
     * Provides the compiled bytecode to the class loader.
     */
    public Map<String, byte[]> getBytecode() {
        Map<String, byte[]> bytecode = new HashMap<>();
        for (Map.Entry<String, ClassOutputBuffer> entry : compiledClasses.entrySet()) {
            bytecode.put(entry.getKey(), entry.getValue().getBytes());
        }
        return bytecode;
    }
}

/**
 * A custom ClassLoader that loads classes from a Map of bytecode.
 */
class BytecodeClassLoader extends ClassLoader {
    private final Map<String, byte[]> bytecode;

    public BytecodeClassLoader(Map<String, byte[]> bytecode, ClassLoader currentClassLoader) {
        super(currentClassLoader);
        this.bytecode = bytecode;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classBytes = bytecode.get(name);
        if (classBytes != null) {
            // Define the class from the byte array
            return defineClass(name, classBytes, 0, classBytes.length);
        }
        return super.findClass(name); // Fallback to parent classloader
    }
}
