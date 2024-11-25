package dev.relocation;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

/**
 * A Maven plugin that increments a build counter for the project.
 *
 * @author Gabriel Delgado, Abyss Development LLC
 * @version 1.0.0-stable
 *
 * @goal increment-build
 * @phase initialize
 *
 * @see <a href=https://www.github.com/relocationdev/>My GitHub</a>
 * @see <a href=https://www.abyssdev.net/>Abyss Development LLC</a>
 */
@Mojo(name = "increment-build", defaultPhase = LifecyclePhase.INITIALIZE)
public class BuildIncrementerMojo extends AbstractMojo {

    private final static String BUILD_FILE = "build-counter.properties";

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private org.apache.maven.project.MavenProject project;

    @Parameter(defaultValue = "${project.basedir}", readonly = true, required = true)
    private File baseDir;

    @Parameter(defaultValue = "${project.version}", readonly = true, required = true)
    private String projectVersion;

    @Override @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException {
        final File buildFile = new File(this.baseDir, BuildIncrementerMojo.BUILD_FILE);
        final Properties properties = new Properties();

        try {
            if (buildFile.exists()) {
                properties.load(Files.newBufferedReader(buildFile.toPath()));
            }

            final String versionKey = "rBuildsVersion_" + this.projectVersion;

            final int totalBuilds = Integer.parseInt(properties.getProperty("rBuilds", "0")) + 1;
            final int versionBuilds = Integer.parseInt(properties.getProperty(versionKey, "0")) + 1;

            properties.setProperty("rBuilds", String.valueOf(totalBuilds));
            properties.setProperty(versionKey, String.valueOf(versionBuilds));

            properties.store(Files.newBufferedWriter(buildFile.toPath()), "Build Counter for " + this.baseDir.getName());

            this.project.getProperties().setProperty("rBuilds", String.valueOf(totalBuilds));
            this.project.getProperties().setProperty("rBuildsVersion", String.valueOf(versionBuilds));

            this.getPluginContext().put("rBuilds", String.valueOf(totalBuilds));
            this.getPluginContext().put("rBuildsVersion", String.valueOf(versionBuilds));

            System.setProperty("rBuilds", String.valueOf(totalBuilds));
            System.setProperty("rBuildsVersion", String.valueOf(versionBuilds));

            this.getLog().info("(maven-build-incrementer) Total Builds: " + totalBuilds);
            this.getLog().info("(maven-build-incrementer) Builds for Version " + this.projectVersion + ": " + versionBuilds);
            this.getLog().info("(maven-build-incrementer) Successfully updated build file: " + buildFile.getAbsolutePath());

        } catch (final IOException | NumberFormatException e) {
            throw new MojoExecutionException("(maven-build-incrementer) Failed to update build file: " + buildFile.getAbsolutePath(), e);
        }
    }

}