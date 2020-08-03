package com.assignment.newsfeed.pojos;

import com.assignment.newsfeed.dto.StoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Story implements Comparable<Story> {

	private String author;

	private String title;

	private String type;

	private String source;

	private Long timestamp;

	private List<Long> children;

	private long id;

	private int score;

	public StoryDTO toDto() {
		return StoryDTO.builder()
				.author(getAuthor())
				.id(getId())
				.score(getScore())
				.timestamp(getTimestamp())
				.title(getTitle())
				.source(getSource())
				.type(getType())
			.build();
	}

	@Override
	public int compareTo(final Story o) {
		if(this.getScore() > o.getScore()) {
			return -1;
		} else if(this.getScore() < o.getScore()) {
			return 1;
		}
		return 0;
	}
}
