/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.openjdk.shinyafox.jshell.maven.plugin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.tools.Tool;
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
        name = "compile",
        defaultPhase = LifecyclePhase.INSTALL,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
        requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME
)
public class JShellMavenPlugin extends AbstractMojo {

    private static final String JSHELL_NAME = "jshell";

    @Parameter(defaultValue = "${project.compileClasspathElements}", readonly = true, required = true)
    private List<String> compilePaths;

    @Parameter
    private List<String> jshellArgs;

    @Parameter
    private List<String> jvmArgs;
    
    @Parameter
    private List<String> remoteJvmArgs;
    
    @Parameter
    private List<String> compilerArgs;

    @Parameter
    private List<String> classpaths;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader()); // promote class loader

            String[] args = buildArgs();
            getLog().info("Starting jshell with " + Arrays.toString(args));

            for (Tool tool : ServiceLoader.load(Tool.class)) {
                if (JSHELL_NAME.equals(tool.name())) {
                    if (tool.run(null, null, null, args) != 0) {
                        throw new Exception("Failed in jshell session.");
                    }
                    return;
                }
            }
            throw new NoSuchElementException("jshell tool not found");
        } catch (Exception e) {
            getLog().error(e);
            throw new MojoExecutionException("Cannot execute jshell. ", e);
        }
    }

    private String[] buildArgs() {
            String path = Stream.concat(asStream(compilePaths), asStream(classpaths))
                    .filter(p -> Files.exists(Paths.get(p)))
                    .collect(Collectors.joining(File.pathSeparator));

        return Stream.of(
                asStream(jshellArgs),
                asStream(jvmArgs).map(prefixing("-J")),
                asStream(remoteJvmArgs).map(prefixing("-R")),
                asStream(compilerArgs).map(prefixing("-C")),
                Stream.of("--class-path", path))
                .flatMap(Function.identity())
                .toArray(String[]::new);
    }

    private static <T> Stream<T> asStream(Collection<? extends T> collection) {
        return collection == null ? Stream.empty() : (Stream<T>)collection.stream();
    }

    private Function<String, String> prefixing(String prefix) {
        return s -> prefix+s;
    }

}
