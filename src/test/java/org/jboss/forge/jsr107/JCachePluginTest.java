package org.jboss.forge.jsr107;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;

public class JCachePluginTest extends AbstractShellTest
{
   @Deployment
   public static JavaArchive getDeployment()
   {
      return AbstractShellTest.getDeployment().addPackages(true, JCachePlugin.class.getPackage());
   }

   @Test
   public void testSetup() throws Exception
   {
      queueInputLines("");
      getShell().execute("jcache setup");
      DependencyBuilder dependency = DependencyBuilder
               .create("javax.cache:cache-api");

      Assert.assertTrue(getProject().getFacet(DependencyFacet.class).hasDirectDependency(dependency));
   }
}
