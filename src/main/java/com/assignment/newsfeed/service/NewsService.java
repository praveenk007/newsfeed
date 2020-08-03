package com.assignment.newsfeed.service;

import com.assignment.newsfeed.pojos.Comment;
import com.assignment.newsfeed.pojos.Story;
import reactor.core.publisher.Mono;

import java.util.List;

public interface NewsService {

	Mono<List<Story>> fetchStories(int n);

	Mono<Story> fetchStoryById(long storyId);

	List<Comment> fetchComments(List<Long> commentIds);
}
