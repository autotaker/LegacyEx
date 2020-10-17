package com.legacy.model;

public class User {

	private final String username;

	public User(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public boolean isAuthed() {
		return true;
	}

}
