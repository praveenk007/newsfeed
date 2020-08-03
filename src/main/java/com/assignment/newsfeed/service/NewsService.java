package com.assignment.newsfeed.service;

import com.assignment.newsfeed.dto.tp.CommentDTO;
import com.assignment.newsfeed.dto.tp.StoryDTO;
import com.assignment.newsfeed.dto.tp.UserDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface NewsService {

	Mono<List<StoryDTO>> fetchStories(int n);

	Mono<StoryDTO> fetchStoryById(long storyId);

	UserDTO fetchUser(String userId);

	List<CommentDTO> fetchComments(List<Long> commentIds);
}
