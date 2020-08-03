package com.assignment.newsfeed.dto.tp;

import com.assignment.newsfeed.pojos.Comment;
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
 * created on 04/08/20
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

	private long id;

	private String author;

	private String text;

	private long parent;

	private List<Long> comments;

	private long timestamp;

	public Comment toComment() {
		return Comment.builder()
			.id(getId())
			.text(getText())
			.author(getAuthor())
			.parent(getParent())
			.timestamp(getTimestamp())
			.comments(getComments())
			.build();
	}
}
