package com.legacy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.legacy.controller.Controller;
public class MainServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Controller controller = new Controller();
		HttpSession session = req.getSession();
		String method = req.getMethod();
		Query query = new Query(req.getParameterMap());
		controller.dispatch(session, method, query, resp);
	}


}
