package se.ltu.ssr.dataviewer;

import java.util.Enumeration;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SSCDataInsert")
public class SSCDataInsert extends HttpServlet{
	private static final long serialVersionUID = 1L;
	protected  void doPost(HttpServletRequest req, HttpServletResponse resp) {
		Enumeration<String> s=req.getParameterNames();
		System.out.println(s);
	}
}
