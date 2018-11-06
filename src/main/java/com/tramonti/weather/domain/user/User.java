package com.tramonti.weather.domain.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode
@Document(collection = "users")
public class User {
	@Id
	@NotEmpty(message = "user.id cannot be empty")
	private String id;
	@NotEmpty(message = "user.name cannot be empty")
	private String username;
	@NotEmpty(message = "user.password cannot be empty")
	private String password;
}
