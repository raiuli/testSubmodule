package se.ltu.ssr.dataviewer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
@WebServlet("/Aiiflow")
public class AiiflowDataViewer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String data="";
		long totalDataCount=0;
		JSONObject jsonData=null;
		Enumeration<String> s=request.getParameterNames();
		System.out.println(s);
		PrintWriter out = response.getWriter();
		CygnusConnector cygnusConnector= new CygnusConnector("130.240.134.126",27017,"sth_default","sth_/_Aiiflow_testGW01_Gateway");
		cygnusConnector.createConnection();
		try {
			data=cygnusConnector.getAiiflowData();
		} catch (Exception e) {
		
			e.printStackTrace();
			out.write("Error");
		}
		try {
			jsonData = new JSONObject(data);
			totalDataCount=jsonData.getLong("TotalCount");
		} catch (JSONException e) {
			
			e.printStackTrace();
			out.write("Error");
		}
		System.out.println("!"+jsonData.toString()+"!"+ "");
		String htmlString="<!DOCTYPE html><html>"
						+ "<head>"
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
		try {
				
			JSONArray jData= jsonData.getJSONArray("Data");
			
				htmlString+="<table border=\"1\">";
				htmlString+="<tr><td colspan=\"11\"> Total Data Count==>";
				htmlString+=totalDataCount;
				htmlString+="</td></tr>";
				htmlString+="<tr>";
				htmlString+="<th>Date </th>";
				htmlString+="<th>User id</th>";		
				htmlString+="<th>Steps</th>";
				htmlString+="<th>Activity Std</th>";
				htmlString+="<th>Location</th>";
				htmlString+="<th>Alert</th>";
				htmlString+="</tr>";
				
				for(int i =0;i<jData.length();i++){
					htmlString+="<tr>";
					JSONObject jo=jData.getJSONObject(i);
					htmlString+="<td>";
					if(jo.has("timestamp"))
						htmlString+=jo.getString("timestamp");
					htmlString+="</td>";
					htmlString+="<td>";
					if(jo.has("User_id"))
						htmlString+=jo.getString("User_id");
					htmlString+="</td>";
					htmlString+="<td>";
					if(jo.has("steps"))
						htmlString+=jo.getString("steps");
					htmlString+="</td>";
					htmlString+="<td>";
					if(jo.has("activity_std"))
						htmlString+=jo.getString("activity_std");
					htmlString+="</td>";
					htmlString+="<td>";
					if(jo.has("location"))
						htmlString+=jo.getString("location");
					htmlString+="</td>";
					htmlString+="<td>";
					if(jo.has("alert"))
						htmlString+=jo.getString("alert");
					htmlString+="</td>";
					htmlString+="</tr>";
			    }
			htmlString+="</table></body></html>";
		} catch (JSONException e) {
			
			e.printStackTrace();
			out.write("Error");
		}
		 out.write(htmlString);
	}
}
