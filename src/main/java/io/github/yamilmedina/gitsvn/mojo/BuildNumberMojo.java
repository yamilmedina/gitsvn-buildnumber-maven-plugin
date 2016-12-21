package io.github.yamilmedina.gitsvn.mojo;

import static io.github.yamilmedina.gitsvn.util.EnvironmentUtils.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import java.util.Arrays;

/**
 *
 * @author Y.Medina
 */
@Mojo(name = "run", defaultPhase = LifecyclePhase.VALIDATE, requiresProject = true, threadSafe = true)
public class BuildNumberMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated/build-metadata", required = true)
    private File outputDirectory;

    @Parameter(property = "outputName", defaultValue = "build.json", required = true)
    private String outputName;

    @Parameter(property = "skip", defaultValue = "false")
    private boolean skip;

    @Parameter(property = "pathToRevisionControlExec", required = true)
    private String pathToRevisionControlExec;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("Skipping...");
            return;
        }

        if (isUnixOS()) {
            getLog().info("*NIX Platform");
            CommandResponse command = executeCommand(pathToRevisionControlExec, "info");
            if (!command.successfulExecution()) {
                command = executeCommand(pathToRevisionControlExec, "svn log --oneline -1 | cut -d '|' -f1");
            }
            if (command.successfulExecution()) {

            }
        } else {
            //@todo: implement for windows platform.
            getLog().info("WINDOWS Platform");
            throw new MojoExecutionException("WINDOWS platform not supported yet.");
        }
    }

    private CommandResponse executeCommand(String... command) {
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
            getLog().error(String.format("Error executing command %s ", Arrays.toString(command)) + e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            getLog().error(String.format("Error executing command %s ", Arrays.toString(command)) + e);
        }
        return commandResponse;
    }

}
