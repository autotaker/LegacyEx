package com.legacy.example;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

@MockitoSettings
class StaticAPIUserTest {

	MockedStatic<StaticAPI> staticAPI;

	@InjectMocks
	StaticAPIUser it;

	@BeforeEach
	void setUp() throws Exception {
		staticAPI = Mockito.mockStatic(StaticAPI.class, Answers.RETURNS_DEFAULTS);
	}

	@AfterEach
	void tearDown() throws Exception {
		staticAPI.close();
	}

	@Test
	void doit_call_google_java() {
		it.doit();
		staticAPI.verify(() -> StaticAPI.google("java"), times(1));
	}

	@Test
	void doit_call_launchICBM() {
		it.doit();
		staticAPI.verify(StaticAPI::launchICBM, times(1));
	}

	@Test
	void doit_call_eject() {
		it.doit();
		staticAPI.verify(StaticAPI::eject, times(1));
	}

}
