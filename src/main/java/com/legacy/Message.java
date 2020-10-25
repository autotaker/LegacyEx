package com.legacy;

/**
 * Dictのメッセージを集めたEnumです。
 * enumの名前をキーとしてDictを取得してください。
 * init.sqlにエントリーを足すと多言語対応できます。
 *
 * @author autotaker
 *
 */
public enum Message {
	LOGIN("Login"),
	LOGOUT("Logout"),
	USERNAME("Username"),
	USER_SETTINGS("User Settings"),
	LANGUAGE("Language"),
	LANG_JAPANESE("Japanese"),
	LANG_ENGLISH("English"),
	BACK("Back"),
	GLOBAL_CONFIG("Global Configurations"),
	BIRTHDAY("Birthday"),
	SAVE("Save");

	private String message;

	Message(String message) {
		this.message = message;
	}

	public String defaultMessage() {
		return this.message;
	}
}
