package com.legacy;

import java.util.Map;
import java.util.Optional;

/**
 * HTTP Requestのパラメータを保持するクラス
 * @author autotaker
 *
 */
public class Query {

	private final Map<String, String[]> parameterMap;

	public Query(Map<String, String[]> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public Optional<String> get(String key) {
		String[] keys = parameterMap.get(key);
		String value = null;
		if( keys != null && keys.length >= 1 ) {
			value = keys[0];
		}
		return Optional.ofNullable(value);
	}

	public Optional<Integer> getInt(String key) {
		return get(key).flatMap(val -> {
			try {
				return Optional.of(Integer.valueOf(val));
			} catch(NumberFormatException e) {
				return Optional.empty();
			}
		});
	}

	public String getAction() {
		return get("action").orElse("*");
	}

}
