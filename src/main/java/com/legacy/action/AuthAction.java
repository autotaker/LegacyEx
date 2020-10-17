package com.legacy.action;

import com.legacy.model.AnonymousUser;
import com.legacy.model.User;

public class AuthAction {

	public User login(String username) {
		if("anonymous".equals(username)) {
			return new AnonymousUser();
		}
		return new User(username);
	}

}
