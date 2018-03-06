package com.tramonti.weather.domain;

import java.util.Objects;

//TODO: refactor equals, hash, toString using Java8 Objects
//or Guava or Apache Commons
public class City {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		City city = (City) o;



		return name != null ? name.equals(city.name) : city.name == null;
	}

	@Override public int hashCode() {

		return name != null ? name.hashCode() : 0;
	}

}
