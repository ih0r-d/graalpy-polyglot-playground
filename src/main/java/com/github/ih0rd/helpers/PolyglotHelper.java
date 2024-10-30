package com.github.ih0rd.helpers;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.io.IOAccess;
import org.graalvm.python.embedding.utils.GraalPyResources;
import org.graalvm.python.embedding.utils.VirtualFileSystem;

import java.nio.file.Path;

public class PolyglotHelper {


    /**
     * This method returned configured Context for working in runtime with Python
     *
     * @return Context
     */
    public static Context getContext() {
        final VirtualFileSystem fileSystem = getVirtualFileSystem();
        return configureContext(GraalPyResources.contextBuilder(fileSystem));
    }

    public static Context getContext(Path resourceDir) {
        return configureContext(GraalPyResources.contextBuilder(resourceDir));
    }

    private static Context configureContext(Context.Builder builder) {
        return builder
                .option("python.PythonHome", "")
                // set true to allow experimental options
                .allowExperimentalOptions(false)
                // setting false will deny all privileges unless configured below
                .allowAllAccess(true)
                // allows python to access the java language
                .allowHostAccess(HostAccess.ALL)
                // allow access to the virtual and the host filesystem, as well as sockets
                .allowIO(IOAccess.ALL)
                // allow creating python threads
                .allowCreateThread(true)
                // allow running Python native extensions
                .allowNativeAccess(true)
                // allow exporting Python values to polyglot bindings and accessing Java from Python
                .allowPolyglotAccess(PolyglotAccess.ALL)
                .build();
    }

    /**
     * You can specify more logic in extractFilter what in files in the virtual filesystem need to be accessed outside the Truffle sandbox.
     * e.g. if they need to be accessed by the operating system loader.
     *
     * @return VirtualFileSystem
     */
    private static VirtualFileSystem getVirtualFileSystem() {
        return VirtualFileSystem.newBuilder().build();
    }
}
