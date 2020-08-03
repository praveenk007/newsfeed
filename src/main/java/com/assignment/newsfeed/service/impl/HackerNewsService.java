package com.assignment.newsfeed.service.impl;

import com.assignment.newsfeed.dto.tp.StoryDTO;
import com.assignment.newsfeed.pojos.Comment;
import com.assignment.newsfeed.pojos.Story;
import com.assignment.newsfeed.service.NewsService;
import com.assignment.newsfeed.utils.NonReactiveWebClient;
import com.assignment.newsfeed.utils.Webber;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 29/07/20
 * @since 1.0.0
 */
@Service("hackerNewsService")
public class HackerNewsService implements NewsService {

	@Autowired
	private Webber webber;

	@Autowired
	private NonReactiveWebClient nonReactiveWebClient;

	@Override
	public Mono<List<Story>> fetchStories(final int n) {
		return fetchStoryId().take(n)
				.flatMap(id -> fetchStory(id)
					.map(storyDTO ->
						Story.builder()
							.id(storyDTO.getId())
							.children(storyDTO.getChildren())
							.author(storyDTO.getAuthor())
							.timestamp(storyDTO.getTimestamp())
							.source(storyDTO.getSource())
							.title(storyDTO.getTitle())
							.type(storyDTO.getType())
							.score(storyDTO.getScore())
						.build()
		)).collectList();
	}

	@Override
	public Mono<Story> fetchStoryById(final long storyId) {
		return fetchStory(storyId)
				.map(storyDTO ->
						Story.builder()
								.id(storyDTO.getId())
								.children(storyDTO.getChildren())
								.author(storyDTO.getAuthor())
								.timestamp(storyDTO.getTimestamp())
								.source(storyDTO.getSource())
								.title(storyDTO.getTitle())
								.type(storyDTO.getType())
								.score(storyDTO.getScore())
								.build()
				);
	}

	@Override
	public List<Comment> fetchComments(final List<Long> commentIds) {
		final List<Comment> comments = new ArrayList<>();
		commentIds.forEach(commentId -> {
			final JsonNode jsonNode = nonReactiveWebClient.get("https://hacker-news.firebaseio.com/v0/item/" + commentId + ".json?print=pretty");
			final Comment comment = Comment.builder()
					.author(jsonNode.get("by") != null ? jsonNode.get("by").asText() : null)
					.text(jsonNode.get("text") != null ? jsonNode.get("text").asText() : null)
					.parent(jsonNode.get("parent") != null ? jsonNode.get("parent").asLong() : 0L)
					.id(jsonNode.get("id").asLong())
					.build();
			comments.add(comment);
			final JsonNode kids = jsonNode.get("kids");
			if(kids != null) {
				ObjectReader reader = new ObjectMapper().readerFor(new TypeReference<List<Long>>() {
				});
				try {
					comment.setComments(reader.readValue(kids));
				} catch (IOException e) {
					//TODO replace with logger
					e.printStackTrace();
				}
			}
		});
		return comments;
	}

	private Flux<Long> fetchStoryId() {
		final List<Long> ids = new ArrayList<>();
		return webber.get("https://hacker-news.firebaseio.com/v0/topstories.json")
				.map(jsonNode -> {
					ArrayNode arrayNode = (ArrayNode) jsonNode;
					arrayNode.forEach(valueNode -> ids.add(valueNode.asLong()));
					return ids;
				}).flatMapMany(Flux::fromIterable);
	}

	private Mono<StoryDTO> fetchStory(final long id) {
		return webber.get("https://hacker-news.firebaseio.com/v0/item/" + id + ".json")
				.map(jsonNode -> {
						StoryDTO storyDto = StoryDTO.builder()
							.id(jsonNode.get("id") != null ? jsonNode.asLong() : 0L)
							.author(jsonNode.get("by") != null ? jsonNode.get("by").asText() : null)
							.source(jsonNode.get("url") != null ? jsonNode.get("url").asText() : null)
							.timestamp(jsonNode.get("time") != null ? jsonNode.get("time").asLong() : null)
							.title(jsonNode.get("title") != null ? jsonNode.get("title").asText() : null)
							.type(jsonNode.get("type") != null ? jsonNode.get("title").asText() : null)
							.score(jsonNode.get("score") != null ? jsonNode.get("score").asInt() : 0)
						.build();

						JsonNode kids = jsonNode.get("kids");
						if(kids == null) {
							return storyDto;
						}
						ObjectReader reader = new ObjectMapper().readerFor(new TypeReference<List<Long>>() {
						});
						List<Long> list = null;
						try {
							list = reader.readValue(kids);
							storyDto.setChildren(list);
						} catch (IOException e) {
							//TODO replace with logger
							e.printStackTrace();
						}
						return storyDto;
				});
	}
}
