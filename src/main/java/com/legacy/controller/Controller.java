package com.legacy.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.legacy.Context;
import com.legacy.Dict;
import com.legacy.GlobalConfig;
import com.legacy.Messages;
import com.legacy.Query;
import com.legacy.action.AuthAction;
import com.legacy.form.AbsForm;
import com.legacy.form.GlobalConfigEntryForm;
import com.legacy.form.LoginForm;
import com.legacy.form.LogoutForm;
import com.legacy.form.UserSettingsForm;
import com.legacy.model.AnonymousUser;
import com.legacy.model.User;

public class Controller {
	/** ロガー */
	private static final Logger log = LoggerFactory.getLogger(Controller.class);

	public void dispatch(HttpSession session, String method, Query query, HttpServletResponse resp) throws IOException {
		String action = query.getAction();
		resp.setContentType("text/html; charset=UTF-8");
		PrintWriter writer = resp.getWriter();
		Context ctx = new Context();
		final User user = Optional.ofNullable((User) session.getAttribute("user"))
				.orElseGet(() -> new AnonymousUser());
		if( user.isAuthed() ) {
			ctx.setLang(user.getLang());
		} else {
			ctx.setLang(GlobalConfig.instance().get("DEFAULT_LANGUAGE", "JA"));
		}

		log.debug("dispatch action = {} method = {}", action, method);
		if ("*".equals(action)) {
			// ホーム画面
			writeHeader(writer);
			String greeting = "Hi " + user.getUsername() + " san";
			writer.println("<h1>" + greeting + "</h1>");
			if (user.isAuthed()) {
				AbsForm form = new LogoutForm(ctx);
				form.writeForm(writer);
				writer.println("<a href=\"?action=user_settings\">"
						+ Dict.get(ctx.getLang(), "USER_SETTINGS", Messages.USER_SETTINGS)
						+ "</a>");
				writer.println("<a href=\"?action=global_config\">"
						+ Dict.get(ctx.getLang(), "GLOBAL_CONFIG", Messages.GLOBAL_CONFIG)
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
			// ログイン画面
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
			//　ログアウト（画面はなし）
			session.invalidate();
			resp.sendRedirect("./");
			return;
		} else if ("user_settings".equals(action) && user.isAuthed()) {
			// ユーザ設定画面
			UserSettingsForm form = new UserSettingsForm(ctx);
			form.setBirthday(user.getBirthday());
			if ("POST".equals(method)) {
				form.input(query);
				form.getLang().ifPresent(lang -> {
					user.setLang(lang);
				});
				form.getBirthday().ifPresent(birthday -> {
					user.setBirthday(birthday);
				});
				resp.sendRedirect("./?action=user_settings");
				return;
			} else {
				writeHeader(writer);
				writer.println("<h1>" + Dict.get(ctx.getLang(), "USER_SETTINGS", Messages.USER_SETTINGS) + "</h1>");
				form.writeForm(writer);
				writer.println("<a href=\"./\">" + Dict.get(ctx.getLang(), "BACK", Messages.BACK) + "</a>");
				writeFooter(writer);
				writer.flush();
				return;
			}
		} else if( "global_config".equals(action) && user.isAuthed()) {
			// サーバ設定画面
			GlobalConfigEntryForm form = new GlobalConfigEntryForm(ctx);
			if( "POST".equals(method) ) {
				form.input(query);
				GlobalConfig.instance().set(form.getKey(), form.getValue());
			}
			List<Map.Entry<String,String>> entries = GlobalConfig.instance().loadAll();
			writeHeader(writer);
				writer.println("<h1>" + Dict.get(ctx.getLang(), "GLOBAL_CONFIG", Messages.GLOBAL_CONFIG) + "</h1>");
			writer.println("<table>");
			writer.println("<thead>");
			writer.println("<tr>");
			writer.println("<th>Key</th>");
			writer.println("<th>Value</th>");
			writer.println("</tr>");
			writer.println("</thead>");
			writer.println("<tbody>");
			for( Map.Entry<String, String> entry : entries) {
				writer.println("<tr>");
				writer.println("<td>" + entry.getKey() + "</td>");
				writer.println("<td>" + entry.getValue() + "</td>");
				writer.println("</tr>");
			}
			writer.println("</tbody>");
			writer.println("</table>");
			form.writeForm(writer);
			writer.println("<a href=\"./\">" + Dict.get(ctx.getLang(), "BACK", Messages.BACK) + "</a>");
			writeFooter(writer);
			writer.flush();
		} else {
			// それ以外の場合
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
