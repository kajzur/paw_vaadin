package com.example.trelloplus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/rest/*", asyncSupported = true)
public class RESTExample extends HttpServlet {

	public RESTExample() {

	}
	private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/plain");

		Enumeration<String> parameterNames = req.getParameterNames();

		while (parameterNames.hasMoreElements()) {

			String paramName = parameterNames.nextElement();
			out.write(paramName+": ");
			

			String[] paramValues = req.getParameterValues(paramName);
			for (int i = 0; i < paramValues.length; i++) {
				String paramValue = paramValues[i];
				out.write("\t" + paramValue);
				out.write("\n");
			}

		}

		out.close();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
		super.doPost(req, resp);
	}
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));

		String data = br.readLine();
		resp.getWriter().write(data);
		
	}
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
		super.doOptions(req, resp);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
		super.doDelete(req, resp);
	}
	
}
