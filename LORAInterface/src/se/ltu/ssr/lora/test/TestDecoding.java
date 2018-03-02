package se.ltu.ssr.lora.test;

import org.json.JSONArray;

import se.ltu.ssr.lora.common.ElsysDecoder;

public class TestDecoding {

	
	public static void main(String[] args) {
		int ff=Integer.parseInt("F0C9", 16);
		//ff = (~ff) + 1;
		System.out.println(ff);
		short f=(short) ff;
		System.out.println(f);
		int ff1=Integer.parseInt("00C9", 16);
		ff = (~ff) + 1;
		System.out.println(ff1);
		
		 ff=Integer.parseInt("C9", 16);
		System.out.println(ff);
		 ff1=Integer.parseInt("C9", 16);
		System.out.println(ff1);
		
		
		 ff=Integer.parseInt("7FFF", 16);
		  System.out.println(ff);
		 ff1=Integer.parseInt("0DFA", 16);
	     System.out.println(ff1);
	    String to_decode="0100C9022F040008070DFA";
	 	//to_decode="0100CC022B03FE014004000A070DE30F00";
	 	//to_decode="0100e202290400270506060308070d62";
		ElsysDecoder elsysDecoder= new ElsysDecoder();
		JSONArray result=elsysDecoder.decoder(elsysDecoder.hexStringToByteArray(to_decode));
		System.out.println(result.toString());
	}
	

}
