package com.legacy.action;

import java.util.Optional;

import com.google.common.base.Strings;
import com.legacy.model.User;

public class AuthAction {

	public Optional<User> login(String username) {
		if( Strings.isNullOrEmpty(username) || "anonymous".equals(username)) {
			return Optional.empty();
		}
		return Optional.of(new User(username));
	}

}
