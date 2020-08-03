package com.assignment.newsfeed.service;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 13/07/20
 * @since 1.0.0
 */
@Service
public class RedisCacheOperation<T> {

	private ReactiveRedisConnectionFactory redisConnectionFactory;

	public RedisCacheOperation(ReactiveRedisConnectionFactory redisConnectionFactory) {
		this.redisConnectionFactory = redisConnectionFactory;
	}

	public ReactiveRedisOperations<String, T> getOperation(final Class<T> clazz) {
		final Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(clazz);
		final RedisSerializationContext.RedisSerializationContextBuilder<String, T> builder =
				RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
		final RedisSerializationContext<String, T> context = builder.value(serializer).build();
		return new ReactiveRedisTemplate<>(redisConnectionFactory, context);
	}
}
