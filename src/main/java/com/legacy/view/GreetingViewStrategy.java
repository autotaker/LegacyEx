package com.legacy.view;

import com.legacy.GlobalConfig;

public class GreetingViewStrategy {

	public static final String BIRTHDAY_GREETING_MESSAGE = "BIRTHDAY_GREETING_MESSAGE";

	public GreetingView create() {
		if( "1".equals(GlobalConfig.instance().get(BIRTHDAY_GREETING_MESSAGE, "0"))) {
			return new BirthdayGreetingView();
		}
		return new DefaultGreetingView();
	}

}
