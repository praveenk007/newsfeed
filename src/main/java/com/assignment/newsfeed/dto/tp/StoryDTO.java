package com.assignment.newsfeed.dto.tp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

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

	private long id;

	private String author;

	private String title;

	private String type;

	private String source;

	private Long timestamp;

	private int score;

	private List<Long> children;
}
