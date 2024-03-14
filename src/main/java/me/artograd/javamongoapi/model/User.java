package me.artograd.javamongoapi.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class User {
	private List<UserAttribute> attributes;

	public User(List<UserAttribute> attributes) {
		super();
		this.attributes = attributes;
	}

	public List<UserAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<UserAttribute> attributes) {
		this.attributes = attributes;
	}
}
