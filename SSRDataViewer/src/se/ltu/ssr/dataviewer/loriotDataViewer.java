package se.ltu.ssr.dataviewer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/lnsDataViewer")
public class loriotDataViewer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(loriotDataViewer.class.getName());
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String data="";
		long totalDataCount=0;
		JSONObject jsonData=null;
		Enumeration<String> s=request.getParameterNames();
		System.out.println(s);
		PrintWriter out = response.getWriter();
		CygnusConnector cygnusConnector= new CygnusConnector("130.240.134.126",27017,"sth_default","sth_/_lns_01_LoraNS");
		cygnusConnector.createConnection();
		try {
			data=cygnusConnector.getLoriotData();
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
		//System.out.println("!"+jsonData.toString()+"!"+ "");
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
			    //Iterator<?> keys = ((JSONObject) jData.get(0)).keys();
				htmlString+="<table border=\"1\" width=\"%\">";
				//htmlString+="<tr><td colspan=\"12\"> Total Data Count==>";
				//htmlString+=totalDataCount;
				//htmlString+="</td></tr>";
				htmlString+="<tr>";
				//htmlString+="<th>recvTime </th>";
				htmlString+="<th>Timestamp</th>";		
				htmlString+="<th>devEui</th>";
				htmlString+="<th>appEui</th>";
				htmlString+="<th>devAddr</th>";
				htmlString+="<th>clusterId</th>";
				htmlString+="<th>msgId</th>";
				htmlString+="<th>fcntDown</th>";
				htmlString+="<th>fcntUp</th>";
				htmlString+="<th>fport</th>";
				htmlString+="<th>maxretry</th>";
				htmlString+="<th>ttl</th>";
				htmlString+="<th>payload</th>";
				htmlString+="<th>Temperature</th>";
				htmlString+="<th>Humidity</th>";
				htmlString+="<th>Light</th>";
				htmlString+="<th>Battery level</th>";
				htmlString+="<th>X-Axis</th>";
				htmlString+="<th>Y-Axis</th>";
				htmlString+="<th>Z-Axis</th>";
				htmlString+="<th>Acc motion</th>";
				htmlString+="<th>Motion</th>";
				htmlString+="<th>Co2</th>";
				
				
				
				/*while(keys.hasNext()) {

					String key=(String) keys.next();
					LOGGER.info(key.toString());
					htmlString+="<th>"+key+"</th>";
				}*/
				htmlString+="</tr>";
				
				
				
				for(int i =0;i<jData.length();i++){
					htmlString+="<tr>";
					JSONObject jo=jData.getJSONObject(i);
					String ss=jo.toString();
					LOGGER.info(ss);
					/*htmlString+="<td>";
					if(jo.has("recvTime"))
						htmlString+=jo.("recvTime");
					htmlString+="</td>";*/
					htmlString+="<td>";
					if(jo.has("Timestamp"))
						htmlString+=jo.getString("Timestamp");
					htmlString+="</td>";
					htmlString+="<td>";
					if(jo.has("devEui"))
						htmlString+=jo.getString("devEui");
					htmlString+="</td>";
					htmlString+="<td>";
					if(jo.has("appEui"))
						htmlString+=jo.getString("appEui");
					htmlString+="</td>";
					htmlString+="<td>";
					if(jo.has("devAddr"))
						htmlString+=jo.getString("devAddr");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("clusterId"))
						htmlString+=jo.getString("clusterId");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("msgId"))
						htmlString+=jo.getString("msgId");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("fcntDown"))
						htmlString+=jo.getString("fcntDown");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("fcntUp"))
						htmlString+=jo.getString("fcntUp");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("fport"))
						htmlString+=jo.getString("fport");
					htmlString+="</td>";
					
					
					
					htmlString+="<td>";
					if(jo.has("maxretry"))
						htmlString+=jo.getString("maxretry");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("ttl"))
						htmlString+=jo.getString("ttl");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("payload"))
						htmlString+=jo.getString("payload");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("Temperature"))
						htmlString+=jo.getString("Temperature");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("Humidity"))
						htmlString+=jo.getString("Humidity");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("Light"))
						htmlString+=jo.getString("Light");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("Battery level"))
						htmlString+=jo.getString("Battery level");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("Acceleration X-Axis"))
						htmlString+=jo.getString("Acceleration X-Axis");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("Acceleration Y-Axis"))
						htmlString+=jo.getString("Acceleration Y-Axis");
					htmlString+="</td>";
					
					
					htmlString+="<td>";
					if(jo.has("Acceleration Z-Axis"))
						htmlString+=jo.getString("Acceleration Z-Axis");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("Acc motion"))
						htmlString+=jo.getString("Acc motion");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("Motion"))
						htmlString+=jo.getString("Motion");
					htmlString+="</td>";
					
					htmlString+="<td>";
					if(jo.has("Co2"))
						htmlString+=jo.getString("Co2");
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
