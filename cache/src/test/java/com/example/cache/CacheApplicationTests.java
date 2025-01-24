package com.example.cache;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.cache.entity.Entity;
import com.example.cache.service.impl.CachingService;

class CacheApplicationTests {

	private CachingService cachingService;

	@BeforeEach
	void setUp() {
		cachingService = new CachingService(2);
	}

	@Test
	void testEvictionOnMaxSizeExceeded() {
		Entity e1 = new Entity(1, "Data1");
		Entity e2 = new Entity(2, "Data2");
		Entity e3 = new Entity(3, "Data3");

		cachingService.add(e1);
		cachingService.add(e2);

		cachingService.get(1);
		cachingService.add(e3);

		assertNotNull(cachingService.get(1));
	}

	@Test
	void testCacheClearing() {
		cachingService.add(new Entity(1, "Data1"));
		cachingService.clear();
		assertThrows(Exception.class, () -> cachingService.get(1));
	}

	@Test
	void testRemoveEntity() {
		Entity e1 = new Entity(1, "Data1");
		cachingService.add(e1);
		cachingService.remove(e1);
		assertThrows(Exception.class, () -> cachingService.get(1));
	}

	@Test
	void testRemoveAllEntities() {
		cachingService.add(new Entity(1, "Data1"));
		cachingService.add(new Entity(2, "Data2"));
		cachingService.removeAll();
		assertThrows(Exception.class, () -> cachingService.get(1));
		assertThrows(Exception.class, () -> cachingService.get(2));
	}

	@Test
	void testAddNullEntity() {
		assertThrows(Exception.class, () -> cachingService.add(null));
	}

}
