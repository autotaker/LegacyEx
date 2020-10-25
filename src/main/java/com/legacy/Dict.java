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
	 * {@code message.toString()}と{@code lang}に対応するDICTテーブルのエントリーを返します。 <br>
	 * 取得ができなかった場合は{@code message.defaultMessage()}を返します。
	 * @see com.legacy.Message
	 *
	 * @param lang
	 * @param message
	 * @return
	 */
	public static String get(String lang, Message message) {
		String msg = globalDict.get(message.toString() + ":" + lang);
		if (msg == null) {
			return message.defaultMessage();
		} else {
			return msg;
		}
	}

	/**
	 * {@code message.toString()}と{@code lang}に対応するDICTテーブルのエントリーを返します。 <br>
	 * 取得ができなかった場合は{@code message.defaultMessage()}を返します。
	 * エントリーはプレースホルダー({0},{1})付きである必要があり、
	 * プレースホルダーに引数で渡したオブジェクトが置換されます。
	 * @see com.legacy.Message
	 *
	 * @param lang
	 * @param message
	 * @param args プレースホルダーのオブジェクト
	 * @return
	 *
	 */
	public static String get(String lang, Message message, Object... args) {
		return MessageFormat.format(get(lang,  message), args);
	}

}
