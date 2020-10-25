package com.legacy.form;

import java.io.PrintWriter;
import java.util.Optional;

import com.legacy.Context;
import com.legacy.Dict;
import com.legacy.Message;
import com.legacy.Query;

public class GlobalConfigEntryForm extends AbsForm {
	private String key;
	public Optional<String> getKey() {
		return Optional.ofNullable(key);
	}

	public void setKey(String key) {
		this.key = key;
	}

	private String value;

	public Optional<String> getValue() {
		return Optional.ofNullable(value);
	}

	public void setValue(String value) {
		this.value = value;
	}

	public GlobalConfigEntryForm(Context ctx) {
		super(ctx);
		setAction("global_config");
		setSubmitValue(Dict.get(ctx.getLang(), Message.SAVE));
	}

	@Override
	public void input(Query query) {
		this.key = query.get("config.key").orElse(null);
		this.value = query.get("config.value").orElse(null);
	}

	@Override
	protected void writeFormImpl(PrintWriter writer) {
		writer.println("<p>");
		writer.println("<label>Key:</label>");
		if( key != null ) {
			writer.println("<input name=\"config.key\" value=\"" + key + "\">");
		}else {
			writer.println("<input name=\"config.key\">");
		}
		writer.println("</p>");
		writer.println("<p>");
		writer.println("<label>Value:</label>");
		if( value != null ) {
			writer.println("<input name=\"config.value\" value=\"" + value + "\">");
		}else {
			writer.println("<input name=\"config.value\">");
		}
		writer.println("</p>");
	}

}
