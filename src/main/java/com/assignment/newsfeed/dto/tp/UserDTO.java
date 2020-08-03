package com.assignment.newsfeed.dto.tp;

import com.assignment.newsfeed.pojos.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

	private String userId;

	private String about;

	private long createdAt;

	public int getProfileAge() {
		return Long.valueOf(System.currentTimeMillis() - new Date(getCreatedAt()).getTime()).intValue();
	}

	public User toUser() {
		return User.builder()
				.profileAge(getProfileAge())
				.userId(getUserId())
				.build();
	}
}
