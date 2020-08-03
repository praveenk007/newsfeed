package com.assignment.newsfeed.service;

import com.assignment.newsfeed.pojos.Comment;
import com.assignment.newsfeed.pojos.Story;
import reactor.core.publisher.Mono;

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
public interface FeedService {

	Mono<List<Story>> fetchStories();

	Mono<List<Comment>> fetchComments(long storyId);
}
