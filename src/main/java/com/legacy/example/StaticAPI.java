package com.legacy.example;

public class StaticAPI {

	/**
	 * Google検索結果を返します
	 * @param word
	 * @return
	 */
	public static String google(String word) {
		throw new RuntimeException("Sorry, Not Implemented");
	}

	/**
	 *　大陸間弾道ミサイルを発射します。
	 */
	public static void launchICBM() {
		throw new RuntimeException("It's too dangeous!");
	}

	/**
	 * CDトレイを開きます。
	 */
	public static void eject() {
		throw new RuntimeException("Your Computer doesn't have CD tray.");
	}
}
