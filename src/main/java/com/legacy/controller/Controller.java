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
import com.legacy.action.GlobalConfigAction;
import com.legacy.action.UserSettingsAction;
import com.legacy.form.AbsForm;
import com.legacy.form.GlobalConfigEntryForm;
import com.legacy.form.LoginForm;
import com.legacy.form.LogoutForm;
import com.legacy.form.UserSettingsForm;
import com.legacy.model.AnonymousUser;
import com.legacy.model.User;
import com.legacy.view.GlobalConfigTableView;

public class Controller {
	/** ロガー */
	private static final Logger log = LoggerFactory.getLogger(Controller.class);
	private UserSettingsAction userSettingsAction = new UserSettingsAction();
	private GlobalConfigTableView globalConfigTableView = new GlobalConfigTableView();
	private GlobalConfigAction globalConfigAction = new GlobalConfigAction();

	// 数百か所から参照され、削除できない定数フィールド
	// Controllerをテストハーネスに入れることはできません。
	public static final String PROBLEMATIC_STATIC_FIELD = GlobalConfig.instance().get("PROBLEMATIC_STATIC_FIELD", "");

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
			Optional<User> newUser = new AuthAction().login(form);
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
				userSettingsAction.changeUserSettings(user, form);
				resp.sendRedirect("./?action=user_settings");
				return;
			} else {
				writeHeader(writer);
				writer.println("<h1>" + Dict.get(ctx.getLang(), "USER_SETTINGS", Messages.USER_SETTINGS) + "</h1>");
				form.writeForm(writer);
				writeBackLink(writer, ctx);
				writeFooter(writer);
				writer.flush();
				return;
			}
		} else if( "global_config".equals(action) && user.isAuthed()) {
			// サーバ設定画面
			GlobalConfigEntryForm form = new GlobalConfigEntryForm(ctx);
			if( "POST".equals(method) ) {
				form.input(query);
				globalConfigAction.update(form);
			}
			List<Map.Entry<String,String>> entries = GlobalConfig.instance().loadAll();
			writeHeader(writer);
			writer.println("<h1>" + Dict.get(ctx.getLang(), "GLOBAL_CONFIG", Messages.GLOBAL_CONFIG) + "</h1>");
			globalConfigTableView.writeTable(writer, entries);
			form.writeForm(writer);
			writeBackLink(writer, ctx);
			writeFooter(writer);
			writer.flush();
		} else {
			// それ以外の場合
			resp.sendRedirect("./");
		}
	}

	private void writeBackLink(PrintWriter writer, Context ctx) {
		writer.println("<a href=\"./\">" + Dict.get(ctx.getLang(), "BACK", Messages.BACK) + "</a>");
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
