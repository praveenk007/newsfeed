package com.assignment.newsfeed.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 12/07/20
 * @since 1.0.0
 */
@Configuration
public class RedisBeans {

	@Value("${cache.redis.host}")
	private String host;

	@Value("${cache.redis.port}")
	private int port;

	@Bean
	LettuceConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
	}
}
