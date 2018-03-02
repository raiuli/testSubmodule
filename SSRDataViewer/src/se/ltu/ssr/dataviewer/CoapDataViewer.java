package se.ltu.ssr.dataviewer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Servlet implementation class CoapDataViewer
 */
@WebServlet("/CoapDataViewer")
public class CoapDataViewer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CoapDataViewer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		MongoDbClient mongoDbClient = new MongoDbClient("130.240.134.129",27017,"SSRtestDB","RawData");
		mongoDbClient.createConnection();
		String json="";
		/*=mongoDbClient.getSensorList();
		JSONObject json2 = null;
		try {
			json2 = new JSONObject(json);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		System.out.println(json2.toString());*/
		//response.setContentType("text/plain");
	    PrintWriter out = response.getWriter();
	    //out.write(json2.toString());
	    
	   json=mongoDbClient.getSensorData("WasteWaterSensor_CoAP_1");
	   JSONObject json2 = null;
		try {
			json2 = new JSONObject(json);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		System.out.println("!"+json2.toString()+"!"+ "");
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
				htmlString+="<table border=\"1\">";
				htmlString+="<tr>";
				htmlString+="<th>Date </th>";
				htmlString+="<th>Temp</th>";		
				htmlString+="<th>Humidity</th>";
				htmlString+="</tr>";
				JSONArray data= json2.getJSONArray("Data");
				for(int i =0;i<data.length();i++){
					htmlString+="<tr>";
					JSONObject jo=data.getJSONObject(i);
					htmlString+="<td>";
					htmlString+=jo.getString("updateDateTime");
					htmlString+="</td>";
					JSONArray recordArray=jo.getJSONArray("records");
						for(int j =0;j<recordArray.length();j++){
							htmlString+="<td>";
							htmlString+=((JSONObject) recordArray.get(j)).getString("value");
							htmlString+="</td>";
						}
					htmlString+="</tr>";
			    }
			htmlString+="</table></body></html>";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//response.setContentType("text/plain");
	   // PrintWriter out = response.getWriter();
		// htmlString="<!DOCTYPE html><html><body><table>";
		/*	htmlString+="<table>";
			htmlString+="<tr>";
					htmlString+="<td>Jill</td>";
							htmlString+="<td>Smith</td>";		
									htmlString+="<td>50</td>";
											htmlString+="</tr></table></body></html>";
*/	    out.write(htmlString);
	}

}

