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

import java.util.concurrent.TimeUnit;

import javax.cache.Cache;
import javax.cache.CacheBuilder;
import javax.cache.CacheConfiguration.Duration;
import javax.cache.CacheConfiguration.ExpiryType;
import javax.cache.CacheManager;
import javax.cache.CacheStatistics;
import javax.cache.Caching;
import javax.cache.OptionalFeature;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class HelloWorld {

    private static Cache<Integer, String> cache;

    private static CacheManager cacheManager;

    public static void main(String[] args) {
        /*** Getting a Cache Instance ***/
        getCacheInstance();
        /*** Puting, Geting and removing a value from the cache ***/
        putGetRemoveValuesFromCache();
        /*** Get Cache Statistics ***/
        getCacheStatistics();
        /*** Cache manager operations ***/
        cacheManagerOperations();
    }

    private static void getCacheInstance() {
        // A CacheManager is used for looking up Caches and controls their lifecycle. It represents a collection of caches.
        cacheManager = Caching.getCacheManager();

        // A CacheBuilder is used for creating Caches.
        CacheBuilder<Integer, String> cacheBuilder = cacheManager.<Integer, String> createCacheBuilder("benevidesCache");
        // enable statistics for every created cache
        cacheBuilder.setStatisticsEnabled(true);
        // Expire the cache after 1 second
        cacheBuilder.setExpiry(ExpiryType.MODIFIED, new Duration(TimeUnit.SECONDS, 1));

        // Create an instance of the cache
        cache = cacheBuilder.build();

        // Prints its status
        System.out.println("Cache Status: " + cache.getStatus());

        // Prints the cache name
        System.out.println("Cache Name: " + cache.getName());
    }

    private static void putGetRemoveValuesFromCache() {
        // put a value
        cache.put(1, "Benevides");

        // get the value
        String value = cache.get(1);
        System.out.println("Value: " + value);

        Duration expire = cache.getConfiguration().getExpiry(ExpiryType.MODIFIED);
        System.out.println("Expires:  " + expire.getDurationAmount() + " " + expire.getTimeUnit());

        try {
            // Wait for the cache expire
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        // get the value again - after expire
        value = cache.get(1);
        System.out.println("Value after 1 second: " + value);

        // remove the value
        cache.remove(1);
        value = cache.get(1);
        System.out.println("Value after removed: " + value);

        boolean hasKey = cache.containsKey(1);
        System.out.println("Has Key 1? " + hasKey);
    }

    private static void getCacheStatistics() {
        CacheStatistics cacheStatistics = cache.getStatistics();

        System.out.println("Cache hits: " + cacheStatistics.getCacheHits());
    }

    private static void cacheManagerOperations() {
        // Lookup caches
        System.out.println("Lookup Cache[newcache] in the CacheManager: " + cacheManager.getCache("newcache"));
        System.out.println("Lookup Cache[benevidesCache] in the CacheManager: " + cacheManager.getCache("benevidesCache"));

        // Query supported feature
        boolean suppotTransaction = cacheManager.isSupported(OptionalFeature.TRANSACTIONS);
        System.out.println("Support Optional Feature [Transactions] ?: " + suppotTransaction);

        // Shutdown the cache manager and all its caches
        cacheManager.shutdown();
    }

}
