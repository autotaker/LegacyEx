package com.legacy.example;

/**
 * StaticAPIをラップしてテスト可能にするためのクラスです。
 * @author autotaker
 *
 */
public class StaticAPIWrapper {
	/**
	 * Google検索結果を返します
	 * @param word
	 * @return
	 */
	public String google(String word) {
		return StaticAPI.google(word);
	}

	/**
	 *　大陸間弾道ミサイルを発射します。
	 */
	public void launchICBM() {
		StaticAPI.launchICBM();
	}

	/**
	 * CDトレイを開きます。
	 */
	public void eject() {
		StaticAPI.eject();
	}
}
