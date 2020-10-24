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

/**
 * 多言語対応を行うクラスです。
 * @author autotaker
 *
 */
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

	/**
	 * {@code key}と{@code lang}に対応するDICTテーブルのエントリーを返します。 <br>
	 * 取得ができなかった場合は{@code defaultMessage}を返します。
	 * @see com.legacy.Messages
	 *
	 * @param lang
	 * @param key
	 * @param defaultMessage Messagesクラスの定数を使用してください。
	 * @return
	 */
	public static String get(String lang, String key, String defaultMessage) {
		String msg = globalDict.get(key + ":" + lang);
		if (msg == null) {
			return defaultMessage;
		} else {
			return msg;
		}
	}

	/**
	 * {@code key}と{@code lang}に対応するDICTテーブルのエントリーを返します。<br>
	 * エントリーはプレースホルダー({0},{1})付きである必要があり、
	 * プレースホルダーに引数で渡したオブジェクトが置換されます。
	 * @see com.legacy.Messages
	 *
	 * @param lang
	 * @param key
	 * @param defaultMessage Messagesクラスの定数を使用してください。
	 * @param args プレースホルダーのオブジェクト
	 * @return
	 *
	 */
	public static String get(String lang, String key, String defaultMessage, Object... args) {
		String message = get(lang, key, defaultMessage);
		return MessageFormat.format(message, args);

	}

}
