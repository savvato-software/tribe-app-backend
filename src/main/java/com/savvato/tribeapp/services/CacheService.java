package com.savvato.tribeapp.services;

public interface CacheService {

	public void put(String cacheName, String key, String value);
	public String get(String cacheName, String key);
	public boolean contains(String cacheName, String key);
	public void remove(String cacheName, String key);	

}
