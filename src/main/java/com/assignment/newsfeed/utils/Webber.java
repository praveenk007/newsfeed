package com.assignment.newsfeed.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 29/07/20
 * @since 1.0.0
 */
@Service
public class Webber {

	private static final WebClient client       = WebClient.create();

	public Mono<JsonNode> get(final String url) {
		return client.get().uri(URI.create(url)).exchange()
				.flatMap(response -> {
					return response.bodyToMono(JsonNode.class);
				})
				.doOnError(e -> {
					System.out.println(e);
				});
	}
}
