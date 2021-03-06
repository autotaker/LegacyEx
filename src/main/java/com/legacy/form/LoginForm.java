package com.legacy.form;

import java.io.PrintWriter;
import java.util.Optional;
import java.util.regex.Pattern;

import com.legacy.Context;
import com.legacy.Dict;
import com.legacy.Message;
import com.legacy.Query;

public class LoginForm extends AbsForm {
	private String username;

	public LoginForm(Context ctx) {
		super(ctx);
		setAction("login");
		setSubmitValue(Dict.get(ctx.getLang(), Message.LOGIN));
	}

	@Override
	protected void writeFormImpl(PrintWriter writer) {
		writer.println("<p>");
		writer.println("<label>" + Dict.get(context.getLang(), Message.USERNAME) + ":</label>");
		writer.println("<input name=\"user.name\">");
		writer.println("</p>");
	}

	public Optional<String> getUsername() {
		return Optional.ofNullable(username);
	}

	@Override
	public void input(Query query) {
		query.get("user.name").filter(x -> Pattern.matches("^[a-zA-Z0-9]+$", x)).ifPresent(rawUsername -> {
			username = rawUsername;
		});
	}
}
