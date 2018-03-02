package se.ltu.ssr.lora.common;

import java.util.Base64;

public class  PayloadtoHexConverter {
	private   char[] DIGITS
    = {'0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	public   String toHex(String payload) {
		byte[] data =Base64.getDecoder().decode(payload);
		final StringBuffer sb = new StringBuffer(data.length * 2);
		for (int i = 0; i < data.length; i++) {
			sb.append(DIGITS[(data[i] >>> 4) & 0x0F]);
			sb.append(DIGITS[data[i] & 0x0F]);
		}
		return sb.toString();

	}
	public   String toHexNiceDisplay(String data) {
		data=toHex(data);
		String newString = "";
		for(int i =0;i<data.length();i=i+2) {
			newString=newString+" "+data.substring(i, i+2);
		}
		return newString.trim();
	}
	
}
