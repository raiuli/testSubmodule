package se.ltu.ssr.lora.test;

public class ConvertByteToBinaryString_Demo {

	public static void main(String[] args)  {
		
		System.out.println("0022 - ConvertByteToBinaryString_Demo");
		System.out.println();
		
		
		byte toBeConverted = 81; // values between 0 and 127
		
		System.out.println(String.format("V1 - ByteValue(%s) => BinaryString(%s)", 
				toBeConverted, byteToBinaryStringV1(toBeConverted)));
		
		System.out.println(String.format("V2 - ByteValue(%s) => BinaryString(%s)", 
				toBeConverted, byteToBinaryStringV2(toBeConverted)));
		
		System.out.println(String.format("V3 - ByteValue(%s) => BinaryString(%s)", 
				toBeConverted, byteToBinaryStringV3(toBeConverted)));
	}
	
	static String byteToBinaryStringV1 (byte toBeConverted) {
		
		StringBuilder result = new StringBuilder();
		
		int counter = Byte.SIZE; // 8 bits - another way: Byte.BYTES * 8;
		int mask = 0b1000_0000; // binary for 1000 0000
		
		while (counter > 0) {
			
			char c = (toBeConverted & mask) == mask ? '1' : '0';
			result.append(c);
			toBeConverted <<= 1; // shift from right to left 1 bit. Example: 0010 -> 0100 -> 1000
			counter--;
		}
		
		return result.toString();
	}
	
	static String byteToBinaryStringV2 (byte toBeConverted) {
		
		String result = "";
		
		int counter = Byte.SIZE;
		int mask = 1; // binary 0000 0001
		
		while (counter > 0) {
			
			char c = (toBeConverted & mask) == mask ? '1' : '0';
			result = c + result;
			toBeConverted >>= 1; // shift from left to right 1 bit. Example: 0010 -> 0001 -> 0000
			counter--;
		}
		
		return result;
	}
	
	static String byteToBinaryStringV3 (byte toBeConverted) {
		
		StringBuilder result = new StringBuilder();
		
		// Shift 0000 0001 from right to left 7 times.
		int mask = (1 << (Byte.SIZE - 1)); // equivalent to 1000 0000
		
		do {
			
			char c = (toBeConverted & mask) != 0 ? '1' : '0';
			result.append(c);
		}
		while ((mask >>= 1) > 0);
		
		return result.toString();
	}
	
}
