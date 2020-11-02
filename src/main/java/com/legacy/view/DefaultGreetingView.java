package com.legacy.view;

import java.io.PrintWriter;

import com.legacy.model.User;

public class DefaultGreetingView implements GreetingView {

	/* (non-Javadoc)
	 * @see com.legacy.view.GreetingView#writeGreeting(java.io.PrintWriter, com.legacy.model.User)
	 */
	@Override
	public void writeGreeting(PrintWriter writer, User user) {
		String greeting = "Hi " + user.getUsername() + " san";
		writer.println("<h1>" + greeting + "</h1>");
	}

}
