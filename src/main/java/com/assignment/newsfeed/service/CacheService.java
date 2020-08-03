package com.assignment.newsfeed.service;

import reactor.core.publisher.Mono;

import java.time.Duration;

public interface CacheService<T> {

	Mono<Boolean> set(String key, T value, Duration ttl, Class<T> clazz);

	Mono<Boolean> set(String key, T value, Class<T> clazz);

	Mono<T> get(String key, Class<T> clazz);
}
