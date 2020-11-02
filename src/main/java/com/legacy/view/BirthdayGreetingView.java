package com.legacy.view;

import java.io.PrintWriter;
import java.time.LocalDate;

import com.legacy.model.User;

public class BirthdayGreetingView implements GreetingView {

	@Override
	public void writeGreeting(PrintWriter writer, User user) {
		this.writeGreeting(writer, user, LocalDate.now());

	}

	public void writeGreeting(PrintWriter writer, User user, LocalDate now) {
		String greeting;
		LocalDate birthday = user.getBirthday();
		if (birthday.getMonth() == now.getMonth() && birthday.getDayOfMonth() == now.getDayOfMonth()) {
			greeting = "Happy Birthday test san";
		} else {
			greeting = "Nice day test san";
		}
		writer.println("<h1>" + greeting + "</h1>");

	}

}
