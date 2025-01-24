package com.example.cache.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cache.entity.Entity;
import com.example.cache.service.impl.CachingService;

@RestController
@RequestMapping("/cache")
public class CacheController {

	private final CachingService cachingService;

	public CacheController() {
		this.cachingService = new CachingService(5);
	}

	@PostMapping("/add")
	public String addEntity(@RequestBody Entity entity) {
		cachingService.add(entity);
		return "Entity added to cache.";
	}

	@DeleteMapping("/remove")
	public String removeEntity(@RequestBody Entity entity) {
		cachingService.remove(entity);
		return "Entity removed.";
	}

	@DeleteMapping("/removeAll")
	public String removeAllEntities() {
		cachingService.removeAll();
		return "All entities removed.";
	}

	@GetMapping("/get/{id}")
	public Entity getEntity(@PathVariable int id) {
		return cachingService.get(id);
	}

	@PostMapping("/clear")
	public String clearCache() {
		cachingService.clear();
		return "Cache cleared.";
	}
}
