package io.github.yamilmedina.gitsvn.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

/**
 *
 * @author Y.Medina
 */
public final class CommandExecutor {

    private static CommandExecutor instance = null;
    private static Log log;

    private CommandExecutor() {
        log = new SystemStreamLog();
    }

    public CommandResponse execute(String... command) {
        CommandResponse commandResponse = new CommandResponse();
        try {
            Process process = Runtime.getRuntime().exec(command);
            final int exitCode = process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            commandResponse.setResponseCode(exitCode);
            commandResponse.setResponse(output.toString());
        } catch (IOException e) {
            log.error(String.format("Error executing command %s ", Arrays.toString(command)) + e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(String.format("Error executing command %s ", Arrays.toString(command)) + e);
        }
        return commandResponse;
    }

    public static CommandExecutor getInstance() {
        if (instance == null) {
            instance = new CommandExecutor();
        }
        return instance;
    }

}
