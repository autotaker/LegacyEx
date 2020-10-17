package com.legacy.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.legacy.Query;
import com.legacy.action.AuthAction;
import com.legacy.model.AnonymousUser;
import com.legacy.model.User;

public class Controller {

	public void dispatch(HttpSession session, String method, Query query, HttpServletResponse resp) throws IOException {
		String action = query.getAction();
		PrintWriter writer = resp.getWriter();
		User user = (User)session.getAttribute("user");
		if( user == null ) {
			user = new AnonymousUser();
		}

		if( "*".equals(action)) {
			resp.setContentType("text/html; charset=UTF-8");
			writeHeader(writer);
			String greeting = "Hi " + user.getUsername() + " san!";
			writer.println("<h1>" + greeting + "</h1>");
			if( ! user.isAuthed() ) {
				writer.println("<p>");
				writer.println("<a href=\"?action=login\">login</a>");
				writer.println("</p>");
			} else {
				writer.println("<form method=\"POST\" action=\"?action=logout\">");
				writer.println("<p>");
				writer.println("<input type=\"submit\" value=\"Logout\">");
				writer.println("</p>");
				writer.println("</form>");
			}
			writeFooter(writer);
			writer.flush();
		} else if( "login".equals(action)) {
			Optional<String> username = query.get("user.name");
			if( user.isAuthed() ) {
				resp.sendRedirect("./");
			} else if( username.isPresent() && "POST".equals(method)) {
				user = new AuthAction().login(username.get());
				session.setAttribute("user", user);
				resp.sendRedirect("./");
			} else {
				resp.setContentType("text/html; charset=UTF-8");
				writeHeader(writer);
				writer.println("<form method=\"POST\">");
				writer.println("<p>");
				writer.println("<label>Username:</label>");
				writer.println("<input name=\"user.name\">");
				writer.println("<input type=\"hidden\" name=\"action\" value=\"login\">");
				writer.println("</p>");
				writer.println("<input type=\"submit\" value=\"Login\">");
				writer.println("</form>");
				writeFooter(writer);
				writer.flush();
			}
		} else if( "logout".equals(action) && "POST".equals(method)) {
			session.invalidate();
			resp.sendRedirect("./");
		}
	}

	private void writeFooter(PrintWriter writer) {
		writer.println("</body>");
		writer.println("</html>");
	}

	private void writeHeader(PrintWriter writer) {
		writer.println("<!DOCTYPE html>");
		writer.println("<html>");
		writer.println("<head>");
		writer.println("<title>Hello</title>");
		writer.println("</head>");
		writer.println("<body>");
	}

}
