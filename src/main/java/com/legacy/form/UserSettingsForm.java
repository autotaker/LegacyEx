package com.legacy.form;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Optional;

import org.h2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.legacy.Context;
import com.legacy.Dict;
import com.legacy.Messages;
import com.legacy.Query;

public class UserSettingsForm extends AbsForm {

	/** ロガー */
	private static final Logger log = LoggerFactory.getLogger(UserSettingsForm.class);
	private static final String DEFAULT_BIRTHDAY = "";
	private String lang;
	private LocalDate birthday;

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
			{ "JA", Dict.get(context.getLang(), "LANG_JAPANESE", Messages.LANG_JAPANESE) },
			{ "EN", Dict.get(context.getLang(), "LANG_ENGLISH", Messages.LANG_ENGLISH) },
		};
		for( String[] language : languages) {
			String opt = language[0].equals(lang) ? " selected " : "";
			writer.println("<option value=\"" + language[0] + "\"" + opt + ">" + language[1] + "</option>");
		}
		writer.println("</select>");
		writer.println("</p>");
		writer.println("<p>");
		writer.println("<label>" + Dict.get(context.getLang(), "BIRTHDAY", Messages.BIRTHDAY) + ":<label>");
		String birthdayStr = DEFAULT_BIRTHDAY;
		if( birthday != null ) {
			birthdayStr = birthday.format(DateTimeFormatter.ISO_LOCAL_DATE);
		}

		writer.println("<input type=\"date\" name=\"user.birthday\" value=\"" + birthdayStr + "\">");
		writer.println("</p>");
	}

	@Override
	public void input(Query query) {
		String langStr = query.get("user.lang").orElseGet(null);
		if( Arrays.asList("JA", "EN").contains(langStr) ) {
			lang = langStr;
		}
		String birthdayStr = query.get("user.birthday").orElse(null);
		if( !StringUtils.isNullOrEmpty(birthdayStr)) {
			try {
				birthday = LocalDate.parse(birthdayStr, DateTimeFormatter.ISO_LOCAL_DATE);
			} catch (DateTimeParseException e) {
				log.warn("failed to parse date", e);
			}
		}
	}

	public Optional<String> getLang() {
		return Optional.ofNullable(lang).filter(x -> !Strings.isNullOrEmpty(x));
	}

	public Optional<LocalDate> getBirthday() {
		return Optional.ofNullable(birthday);
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
}
