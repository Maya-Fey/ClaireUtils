package claire.util.encoding;

public class BitEncoder {
	
	public static final char[] toBinary(boolean[] input)
	{
		char[] chars = new char[input.length];
		for(int i = 0; i < chars.length; i++)
			if(input[i])
				chars[i] = '1';
			else
				chars[i] = '0';
		return chars;
	}
	
	public static final char[] toForth(boolean[] input)
	{
		char[] chars = new char[(input.length / 2) + (input.length & 1)];
		int max = input.length & 0x7FFFFFFE;
		int i = 0;
		int j = 0;
		while(j < max) {
			int k = 0;
			if(input[j++])
				k += 2;
			if(input[j++])
				k += 1;
			chars[i++] = Base64.ALPHABET[k];
		}
		if(j < input.length) {
			if(input[j])
				chars[i] = '1';
			else
				chars[i] = '0';
		}
		return chars;
	}
	
	public static final char[] toOct(boolean[] input)
	{
		int rem = input.length % 3;
		char[] chars = new char[(input.length / 3) + (rem != 0 ? 1 : 0)];
		int max = input.length - rem;
		int i = 0;
		int j = 0;
		while(j < max) {
			int k = 0;
			if(input[j++])
				k += 4;
			if(input[j++])
				k += 2;
			if(input[j++])
				k += 1;
			chars[i++] = Base64.ALPHABET[k];
		}
		if(j < input.length) {
			int k = 0;
			if(input[j++])
				k++;
			while(j < input.length) {
				k <<= 1;
				if(input[j++])
					k++;
			}
			chars[i] = Base64.ALPHABET[k];
		}
		return chars;
	}
	
	public static final char[] toHex(boolean[] input)
	{
		char[] chars = new char[(input.length / 4) + (input.length & 3)];
		int max = input.length & 0x7FFFFFFC;
		int i = 0;
		int j = 0;
		while(j < max) {
			int k = 0;
			if(input[j++])
				k += 8;
			if(input[j++])
				k += 4;
			if(input[j++])
				k += 2;
			if(input[j++])
				k += 1;
			chars[i++] = Base64.ALPHABET[k];
		}
		if(j < input.length) {
			if(input[j])
				chars[i] = '1';
			else
				chars[i] = '0';
		}
		if(j < input.length) {
			int k = 0;
			if(input[j++])
				k++;
			while(j < input.length) {
				k <<= 1;
				if(input[j++])
					k++;
			}
			chars[i] = Base64.ALPHABET[k];
		}
		return chars;
	}
	
	public static final char[] toBase32(boolean[] input)
	{
		int rem = input.length % 5;
		char[] chars = new char[(input.length / 5) + (rem != 0 ? 1 : 0)];
		int max = input.length - rem;
		int i = 0;
		int j = 0;
		while(j < max) {
			int k = 0;
			if(input[j++])
				k += 16;
			if(input[j++])
				k += 8;
			if(input[j++])
				k += 4;
			if(input[j++])
				k += 2;
			if(input[j++])
				k += 1;
			chars[i++] = Base64.ALPHABET[k];
		}
		if(j < input.length) {
			int k = 0;
			if(input[j++])
				k++;
			while(j < input.length) {
				k <<= 1;
				if(input[j++])
					k++;
			}
			chars[i] = Base64.ALPHABET[k];
		}
		return chars;
	}
	
	public static final char[] toBase64(boolean[] input)
	{
		int rem = input.length % 6;
		char[] chars = new char[(input.length / 6) + (rem != 0 ? 1 : 0)];
		int max = input.length - rem;
		int i = 0;
		int j = 0;
		while(j < max) {
			int k = 0;
			if(input[j++])
				k += 32;
			if(input[j++])
				k += 16;
			if(input[j++])
				k += 8;
			if(input[j++])
				k += 4;
			if(input[j++])
				k += 2;
			if(input[j++])
				k += 1;
			chars[i++] = Base64.ALPHABET[k];
		}
		if(j < input.length) {
			int k = 0;
			if(input[j++])
				k++;
			while(j < input.length) {
				k <<= 1;
				if(input[j++])
					k++;
			}
			chars[i] = Base64.ALPHABET[k];
		}
		return chars;
	}

}
