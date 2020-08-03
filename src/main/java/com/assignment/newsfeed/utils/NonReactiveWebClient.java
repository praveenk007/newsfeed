package com.assignment.newsfeed.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 31/07/20
 * @since 1.0.0
 */
@Service
public class NonReactiveWebClient {

	private static RestTemplate restTemplate = new RestTemplate();

	public JsonNode get(final String url) {
		return restTemplate.exchange(URI.create(url), HttpMethod.GET, null, JsonNode.class).getBody();
	}
}
