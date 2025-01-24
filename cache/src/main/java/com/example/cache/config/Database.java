package com.example.cache.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.cache.entity.Entity;

public class Database {

	private static final Map<Integer, Entity> DB_STORE = new ConcurrentHashMap<>();

	public static void save(Entity entity) {
		DB_STORE.put(entity.getId(), entity);
	}

	public static void delete(int id) {
		DB_STORE.remove(id);
	}

	public static void clear() {
		DB_STORE.clear();
	}

	public static Entity findById(int id) {
		return DB_STORE.get(id);
	}

}
