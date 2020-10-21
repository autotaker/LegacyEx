package com.legacy.model;

import java.time.LocalDate;

public class User {

	private final String username;
	private String lang = "JA";
	private LocalDate birthday;

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

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}


}
