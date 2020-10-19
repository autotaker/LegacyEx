package com.legacy.form;

import java.io.PrintWriter;
import java.util.Optional;

import com.legacy.Context;
import com.legacy.Dict;
import com.legacy.Messages;
import com.legacy.Query;

public class LoginForm extends AbsForm {
	private String username;

	public LoginForm(Context ctx) {
		super(ctx);
		setAction("login");
		setSubmitValue(Dict.get(ctx.getLang(), "LOGIN", Messages.LOGIN));
	}

	@Override
	protected void writeFormImpl(PrintWriter writer) {
		writer.println("<p>");
		writer.println("<label>" + Dict.get(context.getLang(), "USERNAME", Messages.USERNAME)+ ":</label>");
		writer.println("<input name=\"user.name\">");
		writer.println("</p>");
	}

	public Optional<String> getUsername() {
		return Optional.ofNullable(username);
	}

	@Override
	public void input(Query query) {
		username = query.get("user.name").orElse(null);
		super.input(query);
	}
}
