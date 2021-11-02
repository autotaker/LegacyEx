package com.legacy.example;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;


@MockitoSettings
public class StaticAPIUserRefactoredTest {
	@Mock
	StaticAPIWrapper staticAPI;

	@InjectMocks
	StaticAPIUserRefactored it;

	@BeforeEach
	public void setUp() throws Exception {
	//	it = new StaticAPIUserRefactored(staticAPI);
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
