module net.java.openjdk.shinyafox.jshell.maven {
    exports net.java.openjdk.shinyafox.jshell.maven.plugin;

    requires jdk.jshell;
    requires maven.plugin.api;
    requires maven.plugin.annotations;
}