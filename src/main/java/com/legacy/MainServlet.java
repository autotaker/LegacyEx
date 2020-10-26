package com.legacy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.legacy.controller.Controller;

public class MainServlet extends HttpServlet {
	/** ロガー */
	private static final Logger log = LoggerFactory.getLogger(MainServlet.class);

	public List<String> loadInitSQL() throws IOException {
		List<String> sqlList = new ArrayList<>();
		try (InputStream stream = this.getClass().getResourceAsStream("/init.sql");
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));) {
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				if (line.endsWith(";")) {
					sqlList.add(sb.toString());
					sb = new StringBuilder();
				} else {
					sb.append('\n');
				}
			}
			String lastSql = sb.toString().trim();
			if (!lastSql.isEmpty()) {
				sqlList.add(lastSql + ';');
			}
			return sqlList;
		}
	}

	@Override
	public void init() throws ServletException {
		try {
			Context ctx;
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
			List<String> sqlList = loadInitSQL();
			try (Connection conn = ds.getConnection();
					Statement stmt = conn.createStatement();) {
				for (String sql : sqlList) {
					try {
						stmt.execute(sql);
						conn.commit();
					} catch (SQLException e) {
						log.error("failed to execute sql", e);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Controller controller = new Controller();
		HttpSession session = req.getSession();
		String method = req.getMethod();
		Query query = new Query(req.getParameterMap());
		try {
			controller.dispatch(session, method, query, resp);
		} catch(Throwable e) {
			log.error("Service Exception", e);
		}
	}

}
