package com.legacy.view;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GlobalConfigTableView {

	public void writeTable(PrintWriter writer, List<Entry<String, String>> entries) {
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
	}

}
