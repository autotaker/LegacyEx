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
import com.legacy.Message;
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
	private Context ctx;
	private PrintWriter writer;

	// 数百か所から参照され、削除できない定数フィールド
	// Controllerをテストハーネスに入れることはできません。
	public static final String PROBLEMATIC_STATIC_FIELD = GlobalConfig.instance().get("PROBLEMATIC_STATIC_FIELD", "");

	public void dispatch(HttpSession session, String method, Query query, HttpServletResponse resp) throws IOException {
		String action = query.getAction();
		resp.setContentType("text/html; charset=UTF-8");
		writer = resp.getWriter();
		ctx = new Context();

		User user = Optional.ofNullable((User) session.getAttribute("user"))
				.orElseGet(() -> new AnonymousUser());
		if( user.isAuthed() ) {
			ctx.setLang(user.getLang());
		} else {
			ctx.setLang(GlobalConfig.instance().get("DEFAULT_LANGUAGE", "JA"));
		}

		log.debug("dispatch action = {} method = {}", action, method);
		if ("*".equals(action)) {
			// ホーム画面
			writeHeader(Message.HOME);
			String greeting = "Hi " + user.getUsername() + " san";
			writer.println("<h1>" + greeting + "</h1>");
			if (user.isAuthed()) {
				AbsForm form = new LogoutForm(ctx);
				form.writeForm(writer);
				writeActionLink("user_settings", Message.USER_SETTINGS);
				writeActionLink("global_config", Message.GLOBAL_CONFIG);
			} else {
				writer.println("<p>");
				writeActionLink("login", Message.LOGIN);
				writer.println("</p>");
			}
			writeFooter();
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
				writeHeader(Message.LOGIN);
				form.writeForm(writer);
				writeFooter();
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
				writeHeader(Message.USER_SETTINGS);
				form.writeForm(writer);
				writeBackLink();
				writeFooter();
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
			writeHeader(Message.GLOBAL_CONFIG);
			globalConfigTableView.writeTable(writer, entries);
			form.writeForm(writer);
			writeBackLink();
			writeFooter();
			writer.flush();
		} else {
			// それ以外の場合
			resp.sendRedirect("./");
		}
	}

	private void writeActionLink(String actionName, Message actionTitle) {
		writer.println("<a href=\"?action=" + actionName + "\">"
				+ Dict.get(ctx.getLang(), actionTitle)
				+ "</a>");
	}

	private void writeBackLink() {
		writer.println("<a href=\"./\">" + Dict.get(ctx.getLang(),  Message.BACK) + "</a>");
	}

	private void writeFooter() {
		writer.println("</body>");
		writer.println("</html>");
	}

	private void writeHeader(Message title) {
		writer.println("<!DOCTYPE html>");
		writer.println("<html>");
		writer.println("<head>");
		writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		writer.println("<title>" + Dict.get(ctx.getLang(), title) + "</title>");
		writer.println("</head>");
		writer.println("<body>");
		writer.println("<h1>" + Dict.get(ctx.getLang(),  title) + "</h1>");
	}

}
