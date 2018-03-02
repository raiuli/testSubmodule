package se.ltu.ssr.testcode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class TestDownloadCSV {

	public static void main(String[] args) {
		String payload="AHMnfQFnAO8CaGQDAGQEAQA=";
		
		 String guid = "YxRfXk827kPgkmMUX15PNg==";
		 guid=payload;
	        byte[] decoded = java.util.Base64.getDecoder().decode(guid);
	        String s=toHex(decoded);
	        System.out.println(s);
	        System.out.println(toHexNiceDisplay(s));
		byte [] barr = java.util.Base64.getDecoder().decode(payload);
		byte[] valueDecoded= org.apache.commons.codec.binary.Base64.decodeBase64(payload );
		s=toHex(valueDecoded);
		System.out.println(toHexNiceDisplay(s));
		System.out.println("Decoded value is " + new String(barr));
//		TestDownloadCSV testDownloadCSV=new TestDownloadCSV();
//		testDownloadCSV.download();

	}
	
	private static final char[] DIGITS
    = {'0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

public static final String toHex(byte[] data) {
final StringBuffer sb = new StringBuffer(data.length * 2);
for (int i = 0; i < data.length; i++) {
    sb.append(DIGITS[(data[i] >>> 4) & 0x0F]);
    sb.append(DIGITS[data[i] & 0x0F]);
}
return sb.toString();
}
public static final String toHexNiceDisplay(String data) {
	String newString = "";
	for(int i =0;i<data.length();i=i+2) {
		newString=newString+" "+data.substring(i, i+2);
	}
	return newString.trim();
}
    public void download() {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(
                "https://openumea-storage.s3.amazonaws.com/2016-04-18T13:35:42/Radon_Skelleftea.csv");
 
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
 
            int responseCode = response.getStatusLine().getStatusCode();
 
            System.out.println("Request Url: " + request.getURI());
            System.out.println("Response Code: " + responseCode);
 
            InputStream is = entity.getContent();
 
            String filePath = "Radon_Skelleftea.csv";
            FileOutputStream fos = new FileOutputStream(new File(filePath));
 
            int inByte;
            while ((inByte = is.read()) != -1) {
                fos.write(inByte);
            }
 
            is.close();
            fos.close();
 
            client.close();
            System.out.println("File Download Completed!!!");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
