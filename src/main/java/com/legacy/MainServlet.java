package com.legacy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
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

import com.legacy.controller.Controller;
public class MainServlet extends HttpServlet {

	public List<String> loadInitSQL() throws IOException {
		List<String> sqlList = new ArrayList<>();
		try(InputStream stream = this.getClass().getResourceAsStream("/init.sql");
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));) {
			String line;
			StringBuilder sb = new StringBuilder();
			while( (line = reader.readLine()) !=null) {
				sb.append(line);
				if(line.endsWith(";")) {
					sqlList.add(sb.toString());
					sb = new StringBuilder();
				}
			}
			String lastSql = sb.toString().trim();
			if( !lastSql.isEmpty()) {
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
			Connection conn = ds.getConnection();
			List<String> sqlList = loadInitSQL();
			try( Statement stmt = conn.createStatement();) {
				for(String sql : sqlList) {
					stmt.addBatch(sql);
				}
				stmt.executeBatch();
				conn.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Controller controller = new Controller();
		HttpSession session = req.getSession();
		String method = req.getMethod();
		Query query = new Query(req.getParameterMap());
		controller.dispatch(session, method, query, resp);
	}


}
