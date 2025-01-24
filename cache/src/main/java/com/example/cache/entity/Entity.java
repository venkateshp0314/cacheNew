package com.example.cache.entity;

public class Entity {

	private final int id;
	private final String data;

	public Entity(int id, String data) {
		this.id = id;
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public String getData() {
		return data;
	}

	@Override
	public String toString() {
		return "Entity{" + "id=" + id + ", data='" + data + '}';
	}

}
