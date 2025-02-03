package com.example.cache;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
	
	@Test
	void testFetchFromDatabaseAfterEviction() {
		Entity e1 = new Entity(1, "Data1");
		cachingService.add(e1);
		cachingService.add(new Entity(2, "Data2"));
		cachingService.add(new Entity(3, "Data3"));

		Entity fetchedEntity = cachingService.get(1);
		assertNotNull(fetchedEntity);
		assertEquals("Data1", fetchedEntity.getData());
	}

	@Test
	void testAddDuplicateEntity() {
		Entity e1 = new Entity(1, "Data1");
		cachingService.add(e1);
		cachingService.add(e1);

		assertNotNull(cachingService.get(1));
	}

	@Test
	void testUpdateEntityData() {
		Entity e1 = new Entity(1, "Data1");
		cachingService.add(e1);

		Entity updatedEntity = new Entity(1, "UpdatedData");
		cachingService.add(updatedEntity);

		Entity fetchedEntity = cachingService.get(1);
		assertEquals("UpdatedData", fetchedEntity.getData());
	}

	@Test
	void testEvictionOrderWithLRU() {
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
	void testClearCacheDoesNotAffectDatabase() {
		Entity e1 = new Entity(1, "Data1");
		cachingService.add(e1);
		cachingService.clear();

		Entity fetchedEntity = cachingService.get(1);
		assertNotNull(fetchedEntity);
		assertEquals("Data1", fetchedEntity.getData());
	}

	@Test
	void testConcurrentAccess() {
		Runnable task = () -> {
			for (int i = 0; i < 10; i++) {
				cachingService.add(new Entity(i, "Data" + i));
			}
		};

		Thread t1 = new Thread(task);
		Thread t2 = new Thread(task);

		t1.start();
		t2.start();

		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertNotNull(cachingService.get(9));
	}

	@Test
	void testAddAndFetchMultipleEntities() {
		Entity e1 = new Entity(1, "Data1");
		Entity e2 = new Entity(2, "Data2");
		Entity e3 = new Entity(3, "Data3");

		cachingService.add(e1);
		cachingService.add(e2);
		cachingService.add(e3);

		assertNotNull(cachingService.get(2));
		assertNotNull(cachingService.get(3));
	}

	@Test
	void testRemoveNonExistentEntity() {
		Entity e1 = new Entity(1, "Data1");
		cachingService.add(e1);
		cachingService.remove(new Entity(99, "NonExistent"));

		assertNotNull(cachingService.get(1));
	}

	@Test
	void testCacheBehaviorAfterMultipleEvictions() {
		cachingService.add(new Entity(1, "Data1"));
		cachingService.add(new Entity(2, "Data2"));
		cachingService.add(new Entity(3, "Data3")); // e1 should be evicted
		cachingService.add(new Entity(4, "Data4")); // e2 should be evicted

		assertNotNull(cachingService.get(3));
		assertNotNull(cachingService.get(4));
	}

}



