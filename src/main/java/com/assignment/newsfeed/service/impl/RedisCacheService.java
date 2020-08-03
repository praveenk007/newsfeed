package com.assignment.newsfeed.service.impl;

import com.assignment.newsfeed.service.CacheService;
import com.assignment.newsfeed.service.RedisCacheOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 13/07/20
 * @since 1.0.0
 */
@Service("redisCache")
public class RedisCacheService<T> implements CacheService<T> {

	@Autowired
	private RedisCacheOperation<T> cacheOperation;

	@Override
	public Mono<Boolean> set(final String key, final T value, final Duration ttl, final Class<T> clazz) {
		return cacheOperation.getOperation(clazz).opsForValue().set(key, value, ttl);
	}

	@Override
	public Mono<Boolean> set(final String key, final T value, final Class<T> clazz) {
		return cacheOperation.getOperation(clazz).opsForValue().set(key, value);
	}

	@Override
	public Mono<T> get(final String key, final Class<T> clazz) {
		return cacheOperation.getOperation(clazz).opsForValue().get(key);
	}
}
