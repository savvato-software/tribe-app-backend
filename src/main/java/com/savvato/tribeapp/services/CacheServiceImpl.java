package com.savvato.tribeapp.services;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CacheServiceImpl implements CacheService {

	HashMap<String, Cache<String, String>> mapCacheNameToCacheOfStringKtoStringV = new HashMap<>();
	
	public CacheServiceImpl() {
		log.debug("Just created instance of CacheServiceImpl");
	}
	
	public void put(String cacheName, String key, String value) {
		getCache(cacheName).put(key, value);
	}
	
	public String get(String cacheName, String key) {
		return getCache(cacheName).get(key);
	}
	
	public boolean contains(String cacheName, String key) {
		return getCache(cacheName).containsKey(key);
	}
	
	public void remove(String cacheName, String key) {
		getCache(cacheName).remove(key);
	}
	
	private Cache<String, String> getCache(String cacheName) {
		if (mapCacheNameToCacheOfStringKtoStringV.containsKey(cacheName)) {
			return mapCacheNameToCacheOfStringKtoStringV.get(cacheName);
		}
		
		log.debug("Cache " + cacheName + " was not found in the CacheService. Creating a new instance.");
		
		CacheManager cm = CacheManagerBuilder.newCacheManagerBuilder()
				.withCache(cacheName,  CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(10))
										.withExpiry(Expirations.timeToIdleExpiration(new Duration(5L, TimeUnit.MINUTES))))
				.build();
		
		cm.init();
		
		Cache<String, String> cache = cm.getCache(cacheName, String.class, String.class);
		
		mapCacheNameToCacheOfStringKtoStringV.put(cacheName, cache);
		
		return cache;
	}
}
