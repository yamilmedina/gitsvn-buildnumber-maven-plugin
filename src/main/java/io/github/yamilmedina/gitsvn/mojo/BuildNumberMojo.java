package io.github.yamilmedina.gitsvn.mojo;

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

import static io.github.yamilmedina.gitsvn.util.EnvironmentUtils.*;

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

        if (isWindowsOS()) {
            //@todo: implement for windows platform.
            getLog().info("WINDOWS Platform");
            throw new MojoExecutionException("WINDOWS platform not supported yet.");
        } else {
            getLog().info("*NIX Platform");
            String command = executeCommand(pathToRevisionControlExec + " log1");

        }
    }

    private String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            getLog().info(output.toString());
        } catch (IOException e) {
            getLog().error("Error executing plugin " + e);
        } catch (InterruptedException e) {
            getLog().error("Error executing plugin " + e);
        }
        return output.toString();
    }

}
