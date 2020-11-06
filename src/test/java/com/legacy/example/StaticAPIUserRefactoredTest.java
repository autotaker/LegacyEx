package com.legacy.example;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StaticAPIUserRefactoredTest {
	@Mock
	StaticAPIWrapper staticAPI;

	StaticAPIUserRefactored it;

	@Before
	public void setUp() throws Exception {
		it = new StaticAPIUserRefactored(staticAPI);
	}

	@Test
	public void doit_call_google_java() {
		it.doit();
		verify(staticAPI).google("java");
	}

	@Test
	public void doit_call_launchICBM() {
		it.doit();
		verify(staticAPI).launchICBM();
	}

	@Test
	public void doit_call_eject() {
		it.doit();
		verify(staticAPI).eject();
	}
}
