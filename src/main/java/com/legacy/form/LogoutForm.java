package com.legacy.form;

import java.io.PrintWriter;

import com.legacy.Context;
import com.legacy.Dict;
import com.legacy.Message;

public class LogoutForm extends AbsForm {
	public LogoutForm(Context ctx) {
		super(ctx);
		setAction("logout");
		setSubmitValue(Dict.get(ctx.getLang(), Message.LOGOUT));
	}

	@Override
	protected void writeFormImpl(PrintWriter writer) {
	}

}
