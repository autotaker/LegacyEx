package com.legacy.example;

public class StaticAPIUserRefactored {
	private final StaticAPIWrapper staticAPI;

	public StaticAPIUserRefactored() {
		this(new StaticAPIWrapper());
	}

	public StaticAPIUserRefactored(StaticAPIWrapper wrapper) {
		this.staticAPI = wrapper;
	}


	public void doit() {
		System.out.println("Google Search");
		System.out.println(staticAPI.google("java"));

		System.out.println("Launch ICBM");
		staticAPI.launchICBM();

		System.out.println("Eject CD");
		staticAPI.eject();
	}

}
