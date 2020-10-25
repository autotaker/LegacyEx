package com.legacy.action;

import java.util.Optional;

import com.google.common.base.Strings;
import com.legacy.GlobalConfig;
import com.legacy.form.LoginForm;
import com.legacy.model.User;

public class AuthAction {

	public Optional<User> login(LoginForm form) {
		return form.getUsername().flatMap(username -> login(username));
	}

	public Optional<User> login(String username) {
		if( Strings.isNullOrEmpty(username) || "anonymous".equals(username)) {
			return Optional.empty();
		}
		User user = new User(username);
		user.setLang(GlobalConfig.instance().get("DEFAULT_LANGUAGE", "JA"));
		return Optional.of(new User(username));
	}

}
