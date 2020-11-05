package com.legacy.view;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.legacy.GlobalConfig;

@RunWith(MockitoJUnitRunner.class)
public class GreetingViewStrategyTest {
	GreetingViewStrategy it;

	@Mock
	private GlobalConfig config;

	@Before
	public void setUp() throws Exception {
		it = new GreetingViewStrategy();
		GlobalConfig.setInstance(config);
	}

	@Test
	public void create_ReturnDefaultGreetingView_If_BIRTHDAY_GREETING_MESSAGE_Is0 () {
		when(config.get(GreetingViewStrategy.BIRTHDAY_GREETING_MESSAGE, "0")).thenReturn("0");
		assertThat(it.create(), is(instanceOf(DefaultGreetingView.class)));
	}

	@Test
	public void create_ReturnBirthdayGreetingView_If_BIRTHDAY_GREETING_MESSAGE_Is1 () {
		when(config.get(GreetingViewStrategy.BIRTHDAY_GREETING_MESSAGE, "0")).thenReturn("1");
		assertThat(it.create(), is(instanceOf(BirthdayGreetingView.class)));
	}

	@After
	public void tearDown() throws Exception {
		// static fieldを変更したら必ず後始末を行いましょう。
		GlobalConfig.setInstance(null);
	}
}
