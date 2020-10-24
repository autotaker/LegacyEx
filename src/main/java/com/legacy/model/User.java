package com.legacy.model;

import java.time.LocalDate;

/**
 * ユーザを表すオブジェクトです。 <br>
 * このオブジェクトはセッションに格納されるため、変更はセッション内で有効です。
 *
 * @author autotaker
 *
 */
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

	/**
	 * ログイン済みユーザか判定します。
	 * @return
	 */
	public boolean isAuthed() {
		return true;
	}

	/**
	 * ユーザの表示言語を返します。
	 * @return
	 */
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * ユーザの生年月日を返します
	 * @return
	 */
	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}


}
