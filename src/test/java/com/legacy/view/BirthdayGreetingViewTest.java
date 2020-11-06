package com.legacy.view;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.legacy.model.User;

public class BirthdayGreetingViewTest {
	BirthdayGreetingView it;
	private StringWriter writer;
	private User user;

	@Before
	public void setUp() throws Exception {
		it = new BirthdayGreetingView();
		writer = new StringWriter();
		user = new User("test");
		user.setBirthday(LocalDate.of(1984, 11, 2));
	}

	@Test
	public void writeGreeting_WriteHappyBirthday_IfDateIsUsersBirthday() {
		it.writeGreeting(new PrintWriter(writer), user, LocalDate.of(2020, 11, 2));

		assertThat(writer.toString().trim(), is("<h1>Happy Birthday test san</h1>"));
	}

	@Test
	public void writeGreeting_WriteNiceday_IfDateIsNotUsersBirthday() {
		it.writeGreeting(new PrintWriter(writer), user, LocalDate.of(2020, 11, 3));

		assertThat(writer.toString().trim(), is("<h1>Nice day test san</h1>"));
	}

	@Test
	public void writeGreeting_WriteNiceday_IfDateIsNotUsersBirthday_Month() {
		it.writeGreeting(new PrintWriter(writer), user, LocalDate.of(2020, 10, 3));

		assertThat(writer.toString().trim(), is("<h1>Nice day test san</h1>"));
	}

	@Test
	public void writeGreeting_WriteNiceday_IfDateIsNull() {
		user.setBirthday(null);
		it.writeGreeting(new PrintWriter(writer), user, LocalDate.of(2020, 11, 6));

		assertThat(writer.toString().trim(), is("<h1>Nice day test san</h1>"));
	}
}
