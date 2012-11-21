/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.forge.jsr107;

import javax.inject.Inject;

import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.dependencies.DependencyInstaller;
import org.jboss.forge.project.facets.BaseFacet;
import org.jboss.forge.shell.plugins.Alias;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 *
 */
@Alias("jcache.api")
public class JCacheApiFacet extends BaseFacet {
    
    @Inject
    private DependencyInstaller dependencyInstaller;

    private DependencyBuilder jCacheAPIDependency = DependencyBuilder.create("javax.cache:cache-api");

    /* (non-Javadoc)
     * @see org.jboss.forge.project.Facet#install()
     */
    @Override
    public boolean install() {
        dependencyInstaller.install(project, jCacheAPIDependency);
        return true;
    }

    /* (non-Javadoc)
     * @see org.jboss.forge.project.Facet#isInstalled()
     */
    @Override
    public boolean isInstalled() {
        return dependencyInstaller.isInstalled(project, jCacheAPIDependency);
    }

}
