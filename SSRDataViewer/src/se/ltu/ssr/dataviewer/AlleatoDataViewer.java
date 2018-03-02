package se.ltu.ssr.dataviewer;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
@WebServlet("/Alleato")
public class AlleatoDataViewer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String data="";
		long totalDataCount=0;
		JSONObject jsonData=null;
		Enumeration<String> s=request.getParameterNames();
	//	System.out.println(s);
		PrintWriter out = response.getWriter();
			String htmlString="<!DOCTYPE html><html>"
						+ "<head>"
			//			+"<link rel=\"stylesheet\" type=\"text/css\" href=\"https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css\">"
			//<script src="https://code.jquery.com/jquery-1.12.4.js"></script>

			//<script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
							+ "<style>"
								+ "table {"	
								+ "   border-collapse: collapse;"
								+ "   width: 100%;"
								+ "}"

								+ "th, td {"
								+ "    text-align: left;"
								+ "    padding: 8px;"
								+ "}"
								+ "tr:nth-child(even){background-color: #f2f2f2}"
							+ "</style>"
						+ "</head>"
				+ "<body>";
//			htmlString+="<table border=\"1\"><tr><td align=\"center\"><form action=\"servlet1\" method=\"post\">"  
//					+ "Name: </td> <td align=\"center\"> <input type=\"text\" name=\"user_name\"/><br/><br/></td></tr>"  
//					+ "<tr><td align=\"center\">Password:</td> <td align=\"center\"><input type=\"password\" name=\"user_pass\"/><br/><br/></td></tr>"  
//					+ "<tr><td colspan=\"11\" align=\"center\"><input type=\"submit\" value=\"login\"/></td></tr>"  
//					+ "</form>";  

			
				htmlString+="<table border=\"1\">";
				htmlString+="<tr><td align=\"center\">";
				htmlString+="<h1\">Aleato Data in Fiware<h1/>";
				htmlString+="</td></tr>";
				htmlString+="<tr><td colspan=\"11\">";
				htmlString+="<h1\">House Numbers<h1/>";
				htmlString+="</td></tr>";
				
				htmlString+="<tr><td colspan=\"11\">";
				htmlString+="<a href=\"http://130.240.134.129:8080/SSRDataViewer/Alleato300090\">300090 Lgh 304</a>";
				htmlString+="</td></tr>";
				htmlString+="<tr><td colspan=\"11\">";
				htmlString+="<a href=\"http://130.240.134.129:8080/SSRDataViewer/Alleato300095\">300095 Lgh 308</a>";
				htmlString+="</td></tr>";
				htmlString+="<tr><td colspan=\"11\">";
				htmlString+="<a href=\"http://130.240.134.129:8080/SSRDataViewer/Alleato300102\">300102 Lgh 404</a>";
				htmlString+="</td></tr>";
				
				
				htmlString+="</table></body></html>";
		
		 out.write(htmlString);
	}
}
