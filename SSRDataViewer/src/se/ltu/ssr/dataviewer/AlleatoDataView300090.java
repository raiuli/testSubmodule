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


@WebServlet("/Alleato300090")
public class AlleatoDataView300090 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String data="";
		long totalDataCount=0;
		JSONObject jsonData=null;
		Enumeration<String> s=request.getParameterNames();
	//	System.out.println(s);
		PrintWriter out = response.getWriter();
		CygnusConnector cygnusConnector= new CygnusConnector("130.240.134.126",27017,"sth_default","sth_/_300090 Lgh 304_Gateway");
		//cygnusConnector.createConnection();
		//long totalcount=0; 
	    JSONObject json2 = null;
	    String sensorData = null;
	    int skip=0;
 	    int limit=200;
	    if((request.getParameter("skip")==null)||(request.getParameter("limit")==null))
	    {
	    			    	
		    	try {
		    			sensorData=cygnusConnector.getAiiflowData();
		    			jsonData = new JSONObject(sensorData);
		    		    totalDataCount=jsonData.getLong("TotalCount");
				} catch (Exception e) {
					
					e.printStackTrace();
					out.write("Error");
				}
		 }else{
		    	 skip=Integer.parseInt(request.getParameter("skip"));
		 	 limit=Integer.parseInt(request.getParameter("limit"));
		 	    
		 	   
		 	  try {
		 		  	sensorData=cygnusConnector.getAiiflowData(skip,limit);
		 		  	jsonData = new JSONObject(sensorData);
		 		   totalDataCount=jsonData.getLong("TotalCount");
				} catch (Exception e) {
					
					e.printStackTrace();
					out.write("Error");
				}
	 	 
	    }	
	
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
				htmlString+="<th>id</th>";
				htmlString+="<th>timeOfData</th>";		
				htmlString+="<th>function_type_name</th>";
				htmlString+="<th>function_name</th>";
				htmlString+="<th>value</th>";
				
				htmlString+="</tr>";
				
				for(int i =0;i<jData.length();i++){
					htmlString+="<tr>";
					JSONObject jo=jData.getJSONObject(i);
					htmlString+="<td>";
					if(jo.has("id"))
						htmlString+=jo.getString("id");
					htmlString+="</td>";
					htmlString+="<td>";
					if(jo.has("timeOfData"))
					{
						htmlString+=jo.getString("timeOfData");
						long timeStamp=Long.parseLong(jo.getString("timeOfData"));
						java.util.Date time=new java.util.Date((long)timeStamp*1000);
						htmlString+=" ! "+time.toString();
						/*SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
						isoFormat.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));
						Date date = null;
						try {
							 date= new java.util.Date((long)timeStamp*1000);
						} catch (Exception e) {
							
							e.printStackTrace();
						}
						htmlString+="!"+date.toString();*/
					}	
						
					htmlString+="</td>";
					htmlString+="<td>";
					if(jo.has("function_type_name"))
						htmlString+=jo.getString("function_type_name");
					htmlString+="</td>";
					htmlString+="<td>";
					if(jo.has("function_name"))
						htmlString+=jo.getString("function_name");
					htmlString+="</td>";
					htmlString+="<td>";
					if(jo.has("value"))
						htmlString+=jo.getString("value");
					htmlString+="</td>";
					
					htmlString+="</tr>";
			    }
				//create pagenation
				htmlString+="<tr><td colspan=\"5\">";
				long pageCounter=(long) Math.floor(totalDataCount/limit);
				int pageindex=0;
				long offset=0;
				while(pageindex<=10) {
					
					htmlString+="<a href=\"Alleato300090?skip="+offset+"&limit="+limit+"\">|"+pageindex+"|</a>";
					offset=offset+limit;
					pageindex++;
				}
				//htmlString+=totalDataCount;
				htmlString+="</td></tr>";
			htmlString+="</table></body></html>";
		} catch (JSONException e) {
			
			e.printStackTrace();
			out.write("Error");
		}
		 out.write(htmlString);
	}
}
