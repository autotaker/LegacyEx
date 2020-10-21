package com.legacy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GlobalConfig {
	private static final String UPDATE_ONE_SQL = "MERGE INTO GLOBAL_CONFIG KEY(config_key) VALUES (?, ?)";
	private static final String SELECT_MANY_SQL = "SELECT config_key, config_value FROM GLOBAL_CONFIG ORDER BY config_key ASC";
	private static final String SELECT_ONE_SQL = "SELECT config_key, config_value FROM GLOBAL_CONFIG WHERE config_key = ?";
	private static GlobalConfig _instance;
	private DataSource dataSource;
	private Map<String, CacheEntry> cache;

	/** ロガー */
	private static final Logger log = LoggerFactory.getLogger(GlobalConfig.CacheEntry.class);
	private static class CacheEntry {
		String value;
		Date expire;

		public CacheEntry(String value) {
			this.value = value;
			this.expire = new Date(System.currentTimeMillis() + 60 * 1000);
		}
		public boolean expired() {
			return new Date().after(this.expire);
		}
	}

	private GlobalConfig() {
		try {
			InitialContext ctx = new InitialContext();
			dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
			cache = new ConcurrentHashMap<>();

		}catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	public static GlobalConfig instance() {
		if (_instance == null) {
			_instance = new GlobalConfig();
		}
		return _instance;
	}

	public String get(String key, String defaultValue)  {
		CacheEntry entry = cache.get(key);
		if( entry != null && !entry.expired() ) {
			log.debug("cache hit: key = {}", key);
			return entry.value;
		}
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL);) {
			ps.setString(1, key);
			try (ResultSet rs = ps.executeQuery();) {
				if (!rs.next()) {
					return defaultValue;
				}
				String value = rs.getString("config_value");
				if (value == null) {
					return defaultValue;
				} else {
					log.debug("cache put: key = {}, value = {}", key, value);
					cache.put(key, new CacheEntry(value));
					return value;
				}
			}
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Map.Entry<String, String>> loadAll() {
		try (Connection conn = dataSource.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SELECT_MANY_SQL);) {
			List<Map.Entry<String, String>> result = new ArrayList<>();
			while(rs.next()) {
				String value = rs.getString("config_value");
				String key = rs.getString("config_key");
				result.add(new AbstractMap.SimpleEntry<>(key, value));
			}
			return result;
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void set(String key, String value) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(UPDATE_ONE_SQL)) {
			ps.setString(1, key);
			ps.setString(2, value);
			ps.execute();
			conn.commit();
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
