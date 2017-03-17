/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.openjdk.shinyafox.jshell.maven.plugin;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 *
 * @author bitter_fox
 */
@Mojo(
        name = "jshell",
        defaultPhase = LifecyclePhase.INSTALL,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
        requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME
)
public class JShellMavenPlugin extends AbstractMojo {

    @Parameter( defaultValue = "${project.compileClasspathElements}", readonly = true, required = true )
    private List<String> compilePath;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader()); // promote class loader

            String path = compilePath.stream()
                    .filter(p -> Files.exists(Paths.get(p)))
                    .collect(Collectors.joining(":"));

            getLog().info("Starting jshell with " + path);
            jdk.internal.jshell.tool.JShellToolProvider.main(new String[]{
                    "--class-path", path,
                    "-C-classpath", "-C"+path
            });
        } catch (Exception e) {
            getLog().error(e);
            throw new MojoExecutionException("Cannot execute jshell. ", e);
        }
    }

}
