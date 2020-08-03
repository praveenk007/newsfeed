package com.assignment.newsfeed.config;

import com.assignment.newsfeed.service.NewsService;
import com.assignment.newsfeed.service.impl.HackerNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 30/07/20
 * @since 1.0.0
 */
@Configuration
public class BeanConfig {

	@Autowired
	private HackerNewsService newsService;

	@Bean
	public NewsService newsService() {
		return newsService;
	}
}
