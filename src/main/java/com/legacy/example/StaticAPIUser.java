package com.legacy.example;

public class StaticAPIUser {
	public void doit() {
		System.out.println("Google Search");
		System.out.println(StaticAPI.google("java"));

		System.out.println("Launch ICBM");
		StaticAPI.launchICBM();

		System.out.println("Eject CD");
		StaticAPI.eject();
	}

}
