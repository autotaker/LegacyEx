package com.legacy.model;

public class User {

	private final String username;
	private String lang = "JP";

	public User(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public boolean isAuthed() {
		return true;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}


}
