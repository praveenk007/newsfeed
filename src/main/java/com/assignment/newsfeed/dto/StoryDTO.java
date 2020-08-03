package com.assignment.newsfeed.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 30/07/20
 * @since 1.0.0
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoryDTO {

	private String author;

	private String title;

	private String type;

	private String source;

	private Long timestamp;

	private long id;

	private int score;
}
