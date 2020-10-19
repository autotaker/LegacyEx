package com.legacy.form;

import java.io.PrintWriter;

import com.legacy.Context;
import com.legacy.Query;

public abstract class AbsForm {
	private String action = "*";
	private String submitValue = "Submit";
	protected Context context;

	public AbsForm(Context ctx) {
		this.context = ctx;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public String getSubmitValue() {
		return submitValue;
	}


	public void setSubmitValue(String submitValue) {
		this.submitValue = submitValue;
	}


	public void writeForm(PrintWriter writer) {
		writer.println("<form method=\"POST\" action=\"./\">");
		writer.println("<input type=\"hidden\" name=\"action\" value=\"" + action +"\">");
		writeFormImpl(writer);
		writer.println("<p>");
		writer.println("<input type=\"submit\" value=\"" + submitValue + "\">");
		writer.println("</p>");
		writer.println("</form>");
	}


	protected abstract void writeFormImpl(PrintWriter writer);


	public void input(Query query) {

	}
}
