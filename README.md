# Java Caching Service

The **Java Caching Service** is a lightweight caching system with configurable capacity and database fallback. This service enables efficient data retrieval and storage by keeping frequently accessed data in memory, while evicting the least-used elements to the database when the cache exceeds its configured size.

---

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)

---

## Features

- Configurable maximum number of elements in the cache.
- Least-Recently-Used (LRU) eviction policy to maintain the cache size.
- Database fallback for storing evicted elements.
- Lightweight APIs for managing and accessing cache data:
  - Add, remove, and retrieve entities from the cache and database.
  - Clear all entries in the cache or both the cache and database.
- Separation of cache and database operations for flexibility.

---

## Technologies Used

- **Java** – Core programming language for the caching service.
- **Spring Boot

  