package com.assignment.newsfeed.service.impl;

import com.assignment.newsfeed.dto.tp.CommentDTO;
import com.assignment.newsfeed.dto.tp.StoryDTO;
import com.assignment.newsfeed.dto.tp.UserDTO;
import com.assignment.newsfeed.pojos.Comment;
import com.assignment.newsfeed.pojos.Story;
import com.assignment.newsfeed.pojos.User;
import com.assignment.newsfeed.service.CacheService;
import com.assignment.newsfeed.service.FeedService;
import com.assignment.newsfeed.service.NewsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
public class FeedServiceImpl implements FeedService {

	@Autowired
	private NewsService newsService;

	@Autowired
	private CacheService<ArrayNode> listCacheService;

	@Autowired
	private CacheService<Story> storyCacheService;

	@Autowired
	private CacheService<User> userCacheService;

	@Override
	public Mono<List<Story>> fetchStories() {
		return listCacheService.get("top_stories", ArrayNode.class)
				.map(array -> {
					List<Story> stories = new ArrayList<>();
					array.forEach(jsonNode -> {
						try {
							stories.add(new ObjectMapper().treeToValue(jsonNode, Story.class));
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
					});
					return stories;
				})
				.switchIfEmpty(
						fetchAndSaveTopStories()
				);
	}

	private Mono<List<Story>> fetchAndSaveTopStories() {
		return newsService.fetchStories(10)
				.map(stories -> {
					final List<Story> sortedStories = stories.stream().map(StoryDTO::toStory).sorted(Story::compareTo).collect(Collectors.toList());
					final ObjectMapper objectMapper = new ObjectMapper();
					ArrayNode arrayNode = objectMapper.createArrayNode();
					sortedStories.forEach(story -> {
						arrayNode.add(objectMapper.convertValue(story, JsonNode.class));
					});
					listCacheService.set("top_stories", arrayNode, Duration.of(10, ChronoUnit.MINUTES), ArrayNode.class).subscribe();
					return sortedStories;
				});
	}

	@Override
	public Mono<List<Comment>> fetchComments(final long storyId) {
		return listCacheService.get("comments_" + storyId, ArrayNode.class)
				.map(array -> {
						List<Comment> comments = new ArrayList<>();
						array.forEach(jsonNode -> {
							try {
								comments.add(new ObjectMapper().treeToValue(jsonNode, Comment.class));
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
						});
						return comments;
					}
				)
				.switchIfEmpty(saveStoryAndFetchCommentsFromService(storyId));
	}

	private Mono<List<Comment>> saveStoryAndFetchCommentsFromService(final long storyId) {
		return storyCacheService.get("story_" + storyId, Story.class)
				.flatMap(story -> getCommentsFromServiceAndSaveInCache(story)
					.flatMap(comment -> getUser(comment.getAuthor()).map(user -> {
						comment.setProfileAge(user.getProfileAge());
						return comment;
					})).collectList())
				.switchIfEmpty(fetchStoryAndCommentsFromService(storyId).collectList());
	}

	private Mono<User> getUser(final String userId) {
		return userCacheService.get("user_" + userId, User.class)
				.switchIfEmpty(getUserFromServiceAndSaveInCache(userId));

	}

	private Mono<User> getUserFromServiceAndSaveInCache(final String userId) {
		final UserDTO userDTO = newsService.fetchUser(userId);
		final User user = userDTO.toUser();
		userCacheService.set("user_" + userId, user, User.class).subscribe();
		return Mono.just(user);
	}

	private Flux<Comment> getCommentsFromServiceAndSaveInCache(final Story story) {
		final List<Comment> commentsFromService = newsService
				.fetchComments(story.getChildren())
				.stream()
				.map(CommentDTO::toComment)
				.sorted(Comment::compareTo)
				.collect(Collectors.toList());
		final ObjectMapper objectMapper = new ObjectMapper();
		ArrayNode arrayNode = objectMapper.createArrayNode();
		commentsFromService.forEach(comment -> arrayNode.add(objectMapper.convertValue(comment, JsonNode.class)));
		listCacheService.set("comments_" + story.getId(), arrayNode, ArrayNode.class).subscribe();
		return Flux.fromIterable(commentsFromService);
	}

	private Flux<Comment> fetchStoryAndCommentsFromService(final long storyId) {
		return newsService.fetchStoryById(storyId).flatMapMany(storyDTO -> {
			final Story story = storyDTO.toStory();
			storyCacheService.set("story_" + storyId, story, Story.class).subscribe();
			return getCommentsFromServiceAndSaveInCache(story)
					.flatMap(comment -> getUser(comment.getAuthor()).map(user -> {
						comment.setProfileAge(user.getProfileAge());
						return comment;
					}));
		});
	}
}