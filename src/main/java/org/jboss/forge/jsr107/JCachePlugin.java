package org.jboss.forge.jsr107;

import java.io.FileNotFoundException;
import java.util.List;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.forge.jsr107.implementations.ImplementationConfiguration;
import org.jboss.forge.jsr107.implementations.JSR107Implementation;
import org.jboss.forge.jsr107.implementations.JSR107ImplementationsCommandCompleter;
import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.project.facets.events.InstallFacets;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Command;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.Plugin;
import org.jboss.forge.shell.plugins.RequiresFacet;
import org.jboss.forge.shell.plugins.RequiresProject;
import org.jboss.forge.shell.plugins.SetupCommand;

/**
 * JCache Forge Plugin
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@Alias("jcache")
@RequiresProject
@RequiresFacet({ JCacheApiFacet.class, JCacheImplFacet.class })
public class JCachePlugin implements Plugin {

    @Inject
    private Shell shell;

    @Inject
    private Project project;

    @Inject
    private Event<InstallFacets> request;

    @Inject
    private ImplementationConfiguration implementationConfiguration;

    @Inject
    private List<JSR107Implementation> implementations;

    @SetupCommand
    public void setup(
            @Option(name = "implementation", completer = JSR107ImplementationsCommandCompleter.class, defaultValue = "ri") String implementationName) {
        if (project.hasFacet(JCacheApiFacet.class)) {
            ShellMessages.warn(shell, "Setup aborted. JCache API was already installed!");
        } else {
            request.fire(new InstallFacets(JCacheApiFacet.class));
        }
        if (project.hasFacet(JCacheImplFacet.class)) {
            ShellMessages.warn(shell, "Setup aborted. JCache Impl was already installed!");
        } else {
            installImplementation(implementationName);
        }
    }

    @Command(value = "changeImplementation")
    public void changeImplementation(
            @Option(name = "newImplementation", completer = JSR107ImplementationsCommandCompleter.class, defaultValue = "ri") String implementationName) {
        JCacheImplFacet implFacet = project.getFacet(JCacheImplFacet.class);
        implFacet.removeImplementations();
        ShellMessages.success(shell, "All previous implementations removed");
        installImplementation(implementationName);
    }

    @Command(value = "installExample", help = "Install hello-word jsr107 example on this project")
    public void installExample() {
        JavaSourceFacet javaSourceFacet = project.getFacet(JavaSourceFacet.class);
        JavaSource<?> helloWorldExample = JavaParser.parse(this.getClass().getResourceAsStream("/examples/HelloWorld.java"));
        try {
            if (javaSourceFacet.getJavaResource(helloWorldExample).exists()) {
                ShellMessages.warn(shell, "Example already exists");
                boolean overwrite = shell.promptBoolean("Do you want do Overwrite?", false);
                if (!overwrite) {
                    return;
                }
            }
            javaSourceFacet.saveJavaSource(helloWorldExample);
            ShellMessages.success(shell, "Example installed");
        } catch (FileNotFoundException e) {
            ShellMessages.error(shell, "Error saving example. Cause: " + e);
        }
    }

    private JSR107Implementation findImpl(String implementationName) {
        for (JSR107Implementation impl : implementations) {
            if (impl.getImplementationName().equals(implementationName)) {
                return impl;
            }
        }
        return null;
    }

    private void installImplementation(String implementationName) {
        JSR107Implementation implementation = findImpl(implementationName);
        implementationConfiguration.setImplementation(implementation);
        request.fire(new InstallFacets(JCacheImplFacet.class));
    }

}
