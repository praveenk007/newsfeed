package com.assignment.newsfeed.controller;

import com.assignment.newsfeed.dto.StoryDTO;
import com.assignment.newsfeed.pojos.Comment;
import com.assignment.newsfeed.pojos.Story;
import com.assignment.newsfeed.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
@RestController
@RequestMapping("/api/feed")
public class NewsFeedController {

	@Autowired
	private FeedService feedService;

	@GetMapping("/top")
	public Mono<List<StoryDTO>> fetchTop() {
		return feedService.fetchStories().map(stories -> stories.stream().map(Story::toDto).collect(Collectors.toList()));
	}

	@GetMapping("/comments/{storyId}")
	public Mono<List<Comment>> fetchComment(@PathVariable("storyId") final long storyId) {
		return feedService.fetchComments(storyId);
	}
}
