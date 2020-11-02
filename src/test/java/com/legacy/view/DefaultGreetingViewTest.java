package com.legacy.view;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import com.legacy.model.User;

public class DefaultGreetingViewTest {
	GreetingView it;

	@Before
	public void setUp() throws Exception {
		it = new DefaultGreetingView();
	}

	@Test
	public void writeGreeting() {
		StringWriter writer = new StringWriter();

		User user = new User("test");
		it.writeGreeting(new PrintWriter(writer), user);
		assertThat(writer.toString().trim(), is("<h1>Hi test san</h1>"));
	}

}
