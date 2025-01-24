package com.example.cache.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.cache.config.Database;
import com.example.cache.entity.Entity;
import com.example.cache.exception.CacheException;
import com.example.cache.exception.EntityNotFoundException;
import com.example.cache.exception.StorageException;

public class CachingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CachingService.class);
	private final int maxCacheSize;
	private final Map<Integer, Entity> cache;
	private final LinkedHashMap<Integer, Entity> lruCache;

	public CachingService(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
		this.cache = new ConcurrentHashMap<>();
		this.lruCache = new LinkedHashMap<>(maxCacheSize, 0.75f, true);
	}

	public synchronized void add(Entity e1) {
		if (e1 == null) {
			throw new CacheException("Cannot add null entity to cache.");
		}
		try {
			if (cache.size() >= maxCacheSize) {
				int leastUsedKey = lruCache.entrySet().iterator().next().getKey();
				Entity evictedEntity = lruCache.remove(leastUsedKey);
				Database.save(evictedEntity);
				cache.remove(leastUsedKey);
				LOGGER.info("Evicted entity {} to database.", leastUsedKey);
			}
			cache.put(e1.getId(), e1);
			lruCache.put(e1.getId(), e1);
			LOGGER.info("Added entity {} to cache.", e1);
		} catch (Exception ex) {
			throw new StorageException("Failed to add entity to cache.", ex);
		}
	}

	public synchronized void remove(Entity e1) {
		if (e1 == null) {
			throw new CacheException("Cannot remove null entity.");
		}
		
		try {
			cache.remove(e1.getId());
			lruCache.remove(e1.getId());
			Database.delete(e1.getId());
			LOGGER.info("Removed entity {} from cache and database.", e1);
		} catch (Exception ex) {
			throw new StorageException("Failed to remove entity from cache or database.", ex);
		}
	}

	public synchronized void removeAll() {
		try {
			cache.clear();
			lruCache.clear();
			Database.clear();
			LOGGER.info("Cleared all entities from cache and database.");
		} catch (Exception ex) {
			throw new StorageException("Failed to remove all entities.", ex);
		}
	}

	public synchronized Entity get(int id) {
		try {
			if (cache.containsKey(id)) {
				Entity entity = cache.get(id);
				lruCache.remove(id);
				lruCache.put(id, entity);
				LOGGER.info("Fetching entity {} from cache.", id);
				return entity;
			}
			Entity dbEntity = Database.findById(id);
			if (dbEntity != null) {
				add(dbEntity);
				LOGGER.info("Fetched entity {} from database and added to cache.", id);
				return dbEntity;
			}
			throw new EntityNotFoundException("Entity with ID " + id + " not found.");
		} catch (Exception ex) {
			throw new StorageException("Error occurred while fetching entity.", ex);
		}
	}

	public synchronized void clear() {
		try {
			cache.clear();
			lruCache.clear();
			LOGGER.info("Cleared cache without affecting database.");
		} catch (Exception ex) {
			throw new StorageException("Failed to clear cache.", ex);
		}
	}

}
