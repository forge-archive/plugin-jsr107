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

package org.jboss.jsr107.helloworld;

import java.util.HashMap;
import java.util.Map;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKeyParam;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;

@CacheDefaults(cacheName = "blogManager")
public class BlogManager {

    private Map<String, Post> posts = new HashMap<String, Post>();

    @CacheResult
    public Post getBlogPost(@CacheKeyParam String title) {
        try {
            System.out.println("Long running operation");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        return posts.get(title);
    }

//    @CacheRemoveEntry
    public void removeBlogEntry(String title) {
        posts.remove(title);
    }

//    @CacheRemoveAll
    public void removeAllBlogs() {
        posts.clear();
    }

    @CachePut
    public void createEntry(@CacheKeyParam String title, @CacheValue Post post) {
        posts.put(title, post);
    }

}