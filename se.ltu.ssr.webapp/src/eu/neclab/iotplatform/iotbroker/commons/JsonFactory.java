package eu.neclab.iotplatform.iotbroker.commons;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * Tools to convert objects to JSON and back. The method specifications
 * are self-explanatory.
 */
public class JsonFactory {

//	private static Logger logger = Logger.getLogger(JsonFactory.class);

	public String convertJsonToString(Object source, Class<?> type) {

		String response = null;

		ObjectMapper mapper = new ObjectMapper();
		SerializationConfig config = mapper.getSerializationConfig();
		config.setSerializationInclusion(Inclusion.NON_NULL);
		try {
			response = mapper.writeValueAsString(source);
			System.out.println("Json Body"+ response);
		} catch (Exception e) {
			System.out.println("JsonGenerationException"+ e);
		} 



		return response;

	}

	public Object convertStringToJsonObject(String xml, Class<?> type) {

		Object response = null;
		ObjectMapper mapper = new ObjectMapper();
		SerializationConfig config = mapper.getSerializationConfig();
		config.setSerializationInclusion(Inclusion.NON_NULL);
		try {
			response = mapper.readValue(xml, type);
		} catch (Exception e) {
			System.out.println("JsonGenerationException"+ e);
		} 


		return response;

	}

}

