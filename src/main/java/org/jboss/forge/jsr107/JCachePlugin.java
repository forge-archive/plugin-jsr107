package org.jboss.forge.jsr107;

import javax.inject.Inject;

import org.jboss.forge.project.Project;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.dependencies.DependencyInstaller;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Plugin;
import org.jboss.forge.shell.plugins.RequiresProject;
import org.jboss.forge.shell.plugins.SetupCommand;

/**
 * JCache Forge Plugin
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 *
 */
@Alias("jcache")
@RequiresProject
public class JCachePlugin implements Plugin
{
   @Inject
   private DependencyInstaller dependencyInstaller;

   @Inject
   private Project project;

   @SetupCommand
   public void setup()
   {
      DependencyBuilder dependency = DependencyBuilder
               .create("javax.cache:cache-api");
      dependencyInstaller.install(project, dependency);
   }
}
