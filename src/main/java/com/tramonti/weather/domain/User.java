package com.tramonti.weather.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

//TODO: define toString, constructors etc, getters setter using Lombok
@Data
@Document(collection = "users")
public class User {
	private String username;
	private String password;
}
