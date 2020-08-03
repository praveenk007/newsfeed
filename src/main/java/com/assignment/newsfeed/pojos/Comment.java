package com.assignment.newsfeed.pojos;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Comparable<Comment> {

	private long id;

	private String author;

	private String text;

	private long parent;

	private List<Long> comments;

	private long timestamp;

	@Override
	public int compareTo(Comment o) {
		if(this.getComments() == null) {
			return 1;
		}
		if(o.getComments() == null) {
			return -1;
		}
		if(this.getComments().size() > o.getComments().size()) {
			return -1;
		} else if(this.getComments().size() < o.getComments().size()) {
			return 1;
		}
		return 0;
	}
}
