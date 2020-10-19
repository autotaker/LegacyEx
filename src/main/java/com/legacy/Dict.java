package com.legacy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Dict {
	private static Map<String, String> globalDict;

	static {
		globalDict = new HashMap<>();
		try {
			Context ctx;
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
			try (Connection conn = ds.getConnection();
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT dict_lang, dict_key, dict_value FROM DICT");) {
				while (rs.next()) {
					String lang = rs.getString("dict_lang");
					String key = rs.getString("dict_key");
					String value = rs.getString("dict_value");
					globalDict.put(key + ":" + lang, value);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static String get(String lang, String key, String defaultMessage) {
		String msg = globalDict.get(key + ":" + lang);
		if (msg == null) {
			return defaultMessage;
		} else {
			return msg;
		}
	}

	public static String get(String lang, String key, String defaultMessage, Object... args) {
		String message = get(lang, key, defaultMessage);
		return MessageFormat.format(message, args);

	}

}
