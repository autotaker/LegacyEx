package com.legacy.view;

import java.io.PrintWriter;

import com.legacy.model.User;

public interface GreetingView {

	void writeGreeting(PrintWriter writer, User user);

}