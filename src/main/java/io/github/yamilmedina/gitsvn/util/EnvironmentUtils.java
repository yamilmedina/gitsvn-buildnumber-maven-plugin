package io.github.yamilmedina.gitsvn.util;

import io.github.yamilmedina.gitsvn.mojo.OS;
import java.util.Locale;

/**
 *
 * @author Y.Medina
 */
public class EnvironmentUtils {

    private EnvironmentUtils() {
    }

    public static OS getOperatingSystemEnvironment() {
        return System.getProperty("os.name").toUpperCase(Locale.ENGLISH).startsWith("WINDOWS") ? OS.WINDOWS : OS.UNIX;
    }

    public static boolean isUnixOS() {
        return OS.UNIX.equals(getOperatingSystemEnvironment());
    }

    public static boolean isWindowsOS() {
        return !isUnixOS();
    }

    public static boolean isGitRepo(String rcsExec) {
        CommandResponse cmdResponse = CommandExecutor.getInstance().execute(rcsExec, "log", "--oneline", "-1");
        return cmdResponse.successfulExecution();
    }

    public static boolean isSvnRepo(String rcsExec) {
        CommandResponse cmdResponse = CommandExecutor.getInstance().execute(rcsExec, "info");
        return cmdResponse.successfulExecution();
    }
}
