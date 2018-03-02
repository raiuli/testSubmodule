package se.ltu.ssr.dataviewer;

import java.io.BufferedReader;
import java.util.Enumeration;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/demoDevice/*")
public class demoDevice extends HttpServlet{
	private static final long serialVersionUID = 1L;
	protected  void doPost(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("demodevice"+req.getPathInfo());
		
		 StringBuffer jb = new StringBuffer();
		  String line = null;
		  try {
		    BufferedReader reader = req.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		  } catch (Exception e) { /*report an error*/ }
		  System.out.println("demodevice"+jb);
		  
		  
	}
}
