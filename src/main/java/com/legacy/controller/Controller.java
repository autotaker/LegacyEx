package com.legacy.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.legacy.Context;
import com.legacy.Dict;
import com.legacy.Messages;
import com.legacy.Query;
import com.legacy.action.AuthAction;
import com.legacy.form.AbsForm;
import com.legacy.form.LoginForm;
import com.legacy.form.LogoutForm;
import com.legacy.form.UserSettingsForm;
import com.legacy.model.AnonymousUser;
import com.legacy.model.User;

public class Controller {

	public void dispatch(HttpSession session, String method, Query query, HttpServletResponse resp) throws IOException {
		String action = query.getAction();
		resp.setContentType("text/html; charset=UTF-8");
		PrintWriter writer = resp.getWriter();
		Context ctx = new Context();
		final User user = Optional.ofNullable((User) session.getAttribute("user"))
				.orElseGet(() -> new AnonymousUser());
		ctx.setLang(user.getLang());

		if ("*".equals(action)) {
			writeHeader(writer);
			String greeting = Dict.get(ctx.getLang(), "GREETING", Messages.GREETING, user.getUsername());
			writer.println("<h1>" + greeting + "</h1>");
			if (user.isAuthed()) {
				AbsForm form = new LogoutForm(ctx);
				form.writeForm(writer);
				writer.println("<a href=\"?action=user_settings\">"
						+ Dict.get(ctx.getLang(), "USER_SETTINGS", Messages.USER_SETTINGS)
						+ "</a>");
			} else {
				writer.println("<p>");
				writer.println("<a href=\"?action=login\">"
						+ Dict.get(ctx.getLang(), "LOGIN", Messages.LOGIN)
						+ "</a>");
				writer.println("</p>");
			}
			writeFooter(writer);
			writer.flush();
			return;
		} else if ("login".equals(action) && !user.isAuthed()) {
			LoginForm form = new LoginForm(ctx);
			form.input(query);
			Optional<User> newUser = form.getUsername()
					.flatMap(name -> new AuthAction().login(name));
			if (newUser.isPresent()) {
				session.setAttribute("user", newUser.get());
				resp.sendRedirect("./");
				return;
			} else {
				writeHeader(writer);
				form.writeForm(writer);
				writeFooter(writer);
				writer.flush();
				return;

			}
		} else if ("logout".equals(action) && "POST".equals(method)) {
			session.invalidate();
			resp.sendRedirect("./");
			return;
		} else if ("user_settings".equals(action) && user.isAuthed()) {
			UserSettingsForm form = new UserSettingsForm(ctx);
			if ("POST".equals(method)) {
				form.input(query);
				Optional<String> langOpt = form.getLang();
				langOpt.ifPresent(lang -> {
					user.setLang(lang);
				});
				resp.sendRedirect("./?action=user_settings");
				return;
			} else {
				writeHeader(writer);
				form.writeForm(writer);
				writer.println("<a href=\"./\">" + Dict.get(ctx.getLang(), "BACK", Messages.BACK) + "</a>");
				writeFooter(writer);
				writer.flush();
				return;
			}
		} else {
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
		writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		writer.println("<title>Hello</title>");
		writer.println("</head>");
		writer.println("<body>");
	}

}
