package com.comugamers.sentey.core.service.dependency;

import com.comugamers.sentey.common.Service;
import net.byteflux.libby.BukkitLibraryManager;
import org.bukkit.plugin.Plugin;

// TODO: we aren't relocating anything - investigate how to do it since jar-relocation complains
//  about a missing class even if you shade the dependency during build time. If we are able to
//  relocate dependencies, we may as well download bStats during runtime.
public class DependencyService implements Service {

    private final BukkitLibraryManager libraryManager;
    private final Plugin plugin;

    // We can't inject our plugin instance since Guice has not been added to the classpath
    public DependencyService(Plugin plugin) {
        this.libraryManager = new BukkitLibraryManager(plugin);
        this.plugin = plugin;
    }

    @Override
    public void start() {
        // Log that we're downloading required dependencies
        plugin.getLogger().info("Loading required dependencies, please wait...");

        // Add the Maven Central repository
        libraryManager.addMavenCentral();

        // Load all required dependencies using the auto-generated dependencies.json file
        libraryManager.fromGeneratedResource(plugin.getResource("dependencies.json")).forEach(library -> {
            try {
                // Load the library
                libraryManager.loadLibrary(library);
            } catch (RuntimeException ex) {
                // Log a warning
                plugin.getLogger().warning(
                        "Unable to download \"" + library + "\", please attach the following stack trace in " +
                                "case of a bug report:"
                );

                // Print the stack trace
                ex.printStackTrace();
            }
        });
    }
}
