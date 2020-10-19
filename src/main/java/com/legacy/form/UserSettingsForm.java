package com.legacy.form;

import java.io.PrintWriter;
import java.util.Optional;

import com.google.common.base.Strings;
import com.legacy.Context;
import com.legacy.Dict;
import com.legacy.Messages;
import com.legacy.Query;

public class UserSettingsForm extends AbsForm {

	private String lang;

	public UserSettingsForm(Context ctx) {
		super(ctx);
		setAction("user_settings");
		setSubmitValue(Dict.get(ctx.getLang(), "SAVE", Messages.SAVE));
		this.lang = ctx.getLang();
	}

	@Override
	protected void writeFormImpl(PrintWriter writer) {
		writer.println("<p>");
		writer.println("<label>" + Dict.get(context.getLang(), "LANGUAGE", Messages.LANGUAGE) + ":<label>");
		writer.println("<select name=\"user.lang\">");
		String[][] languages = new String[][] {
			{ "JP", Dict.get(context.getLang(), "LANG_JAPANESE", Messages.LANG_JAPANESE) },
			{ "EN", Dict.get(context.getLang(), "LANG_ENGLISH", Messages.LANG_ENGLISH) },
		};
		for( String[] language : languages) {
			String opt = language[0].equals(lang) ? " selected " : "";
			writer.println("<option value=\"" + language[0] + "\"" + opt + ">" + language[1] + "</option>");
		}
		writer.println("</select>");
		writer.println("</p>");
	}

	@Override
	public void input(Query query) {
		this.lang = query.get("user.lang").orElseGet(null);
	}

	public Optional<String> getLang() {
		return Optional.ofNullable(lang).filter(x -> !Strings.isNullOrEmpty(x));
	}

}
