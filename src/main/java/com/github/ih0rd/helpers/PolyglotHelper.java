package com.github.ih0rd.helpers;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.io.IOAccess;
import org.graalvm.python.embedding.utils.VirtualFileSystem;

import java.util.HashMap;
import java.util.Map;

import static com.github.ih0rd.utils.Constants.*;

public class PolyglotHelper {


    /**
     * This method returned configured Context for working in runtime with Python
     *
     * @return Context
     */
    public static Context getContext() {
        var vfs = getVirtualFileSystem();
        var configOptions = getConfigOptions(vfs);

        var ioAccess = IOAccess.newBuilder()
                .allowHostSocketAccess(true)
                .fileSystem(vfs)
                .build();
        return Context.newBuilder()
                // set true to allow experimental options
                .allowExperimentalOptions(false)
                // setting false will deny all privileges unless configured below
                .allowAllAccess(true)
                // allows python to access the java language
                .allowHostAccess(HostAccess.ALL)
                // allow access to the virtual and the host filesystem, as well as sockets
                .allowIO(ioAccess)
                // allow creating python threads
                .allowCreateThread(true)
                // allow running Python native extensions
                .allowNativeAccess(true)
                // allow exporting Python values to polyglot bindings and accessing Java from Python
                .allowPolyglotAccess(PolyglotAccess.ALL)
                .options(configOptions)
                .build();
    }


    /**
     * You can specify more logic in extractFilter what in files in the virtual filesystem need to be accessed outside the Truffle sandbox.
     * e.g. if they need to be accessed by the operating system loader.
     *
     * @return VirtualFileSystem
     */
    private static org.graalvm.python.embedding.utils.VirtualFileSystem getVirtualFileSystem() {
        return VirtualFileSystem.newBuilder().build();
    }


    /**
     * Specified all options used in build of Context for python.
     *
     * @return Map<String,String> with options
     */
    private static Map<String, String> getConfigOptions(VirtualFileSystem vfs) {
        var pythonVerbose = System.getenv("PYTHONVERBOSE") != null ? "true" : "false";
        var pythonLogLevel = System.getenv("PYTHONVERBOSE") != null ? "FINE" : "SEVERE";
        var warnOptions = System.getenv("PYTHONWARNINGS") == null ? "" : System.getenv("PYTHONWARNINGS");
        var executable = vfs.resourcePathToPlatformPath(VENV_PREFIX) + (VirtualFileSystem.isWindows() ? "\\Scripts\\python.exe" : "/bin/python");

        var pythonOptions = getPythonOptions(pythonVerbose, pythonLogLevel, warnOptions, executable);
        var vfsOptions = getVfsOptions(vfs);

        Map<String, String> options = new HashMap<>();
        options.putAll(pythonOptions);
        options.putAll(vfsOptions);

        return options;
    }

    /**
     * Specified options for vfs and manage embedded resources as project and home files.
     *
     * @return Map<String,String> with options
     */
    private static Map<String, String> getVfsOptions(VirtualFileSystem vfs) {
        return Map.of(
                // Set the python home to be read from the embedded resources
                "python.PythonHome", vfs.resourcePathToPlatformPath(HOME_PREFIX),
                // Set python path to point to sources stored in src/main/resources/vfs/proj
                "python.PythonPath", vfs.resourcePathToPlatformPath(PROJ_PREFIX)
        );
    }

    /**
     * Specified options for python logging and engine config.
     *
     * @return Map<String,String> with options
     */
    private static Map<String, String> getPythonOptions(String pythonVerbose, String pythonLogLevel, String warnOptions, String executable) {
        return Map.of(
                // choose the backend for the POSIX module
                "python.PosixModuleBackend", "java",
                // equivalent to the Python -B flag
                "python.DontWriteBytecodeFlag", "true",
                //equivalent to the Python -v flag
                "python.VerboseFlag", pythonVerbose,
                // log level
                "log.python.level", pythonLogLevel,
                // equivalent to setting the PYTHONWARNINGS environment variable
                "python.WarnOptions", warnOptions,
                // print Python exceptions directly
                "python.AlwaysRunExcepthook", "true",
                // Force to automatically import site.py module, to make Python packages available
                "python.ForceImportSite", "true",
                // The sys.executable path, a virtual path that is used by the interpreter to discover packages
                "python.Executable", executable,
                // Do not warn if running without JIT. This can be desirable for short running scripts  to reduce memory footprint.
                "engine.WarnInterpreterOnly", "false"
        );
    }

}
