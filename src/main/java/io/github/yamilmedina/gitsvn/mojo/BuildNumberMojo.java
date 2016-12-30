package io.github.yamilmedina.gitsvn.mojo;

import io.github.yamilmedina.gitsvn.util.CommandExecutor;
import io.github.yamilmedina.gitsvn.util.CommandResponse;
import static io.github.yamilmedina.gitsvn.util.EnvironmentUtils.*;
import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

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

    @Parameter(property = "pathToRevisionControlExec", required = false)
    private String pathToRevisionControlExec;

    @Parameter(property = "useGitRevision", defaultValue = "false")
    private boolean useGitRevision;

    @Parameter(property = "revisionControlSystem", defaultValue = "SVN")
    private RevisionControlSystem revisionControlSystem;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("Skipping...");
            return;
        }

        if (isWindowsOS()) {
            throw new MojoExecutionException("WINDOWS platform not supported yet.");
        }

        String svnExec = pathToRevisionControlExec != null ? pathToRevisionControlExec : RevisionControlSystem.SVN.toString();
        final boolean isSvnRepo = isSvnRepo(svnExec);
        String gitExec = pathToRevisionControlExec != null ? pathToRevisionControlExec : RevisionControlSystem.GIT.toString();
        final boolean isGitRepo = isGitRepo(gitExec);
        if (!isSvnRepo && !isGitRepo) {
            throw new MojoFailureException("Maybe this is not a SVN/GIT repo or the path to SVN/GIT binaries are incorrect");
        }

        if (isSvnRepo) {
            final String[] cmd = {"/bin/sh", "-c", svnExec + " log -l 1 | grep -e '^r[0-9]*'"};
            findRevisionInfo(cmd);
        }
        if (!isSvnRepo && isGitRepo) {
            final String[] cmd = {"/bin/sh", "-c", gitExec + " log --oneline -1 | cut -d '|' -f1"};
            findRevisionInfo(cmd);
        }
    }

    private String findRevisionInfo(String[] cmd) {
        CommandResponse log = CommandExecutor.getInstance().execute(cmd);
        String revisionInfo = log.successfulExecution() ? log.getResponse() : "@todo: fechaBuild on fail";
        getLog().info(revisionInfo);
        return revisionInfo;
    }

}
