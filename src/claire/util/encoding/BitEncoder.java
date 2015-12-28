package claire.util.encoding;

public class BitEncoder {
	
	public static final String toBinary(boolean[] input)
	{
		if(input == null) throw new java.lang.NullPointerException();
		String s = "";
		for(boolean b : input) {
			if(b) {
				s += "1";
			} else {
				s += "0";
			}
		}
		return s;
	}
	
	public static final String toForth(boolean[] input)
	{
		if(input == null) throw new java.lang.NullPointerException();
		String s = "";
		int b;
		int c = 0;
		for(int i = 0; i < input.length - 1; i += 2) {
			b = 0;
			if(input[i]) b += 1;
			if(input[i + 1]) b += 2;
			switch(b) {
				case 0: s += "0"; break;
				case 1: s += "1"; break;
				case 2: s += "2"; break;
				case 3: s += "3"; break;
			}
			c = i + 2;
		}
		
		if(c < input.length) {
			boolean[] n = new boolean[input.length - c];
			for(int i = c; i < input.length; i++) {
				n[i - c] = input[i];
			}
			s += excess(n);
		}
		return s;
	}
	
	public static final String toOct(boolean[] input)
	{
		if(input == null) throw new java.lang.NullPointerException();
		String s = "";
		int b;
		int c = 0;
		for(int i = 0; i < input.length - 2; i += 3) {
			b = 0;
			if(input[i]) b += 1;
			if(input[i + 1]) b += 2;
			if(input[i + 2]) b += 4;
			switch(b) {
				case 0: s+="0"; break;
				case 1: s+="1"; break;
				case 2: s+="2"; break;
				case 3: s+="3"; break;
				case 4: s+="4"; break;
				case 5: s+="5"; break;
				case 6: s+="6"; break;
				case 7: s+="7"; break;
			}
			c = i + 3;
		}
		if(c < input.length) {
			boolean[] n = new boolean[input.length - c];
			for(int i = c; i < input.length; i++) {
				n[i - c] = input[i];
			}
			s += excess(n);
		}
		return s;
	}
	
	public static final String toHex(boolean[] input)
	{
		if(input == null) throw new java.lang.NullPointerException();
		String s = "";
		int b;
		int c = 0;
		for(int i = 0; i < input.length - 3; i += 4) {
			b = 0;
			if(input[i]) b += 1;
			if(input[i + 1]) b += 2;
			if(input[i + 2]) b += 4;
			if(input[i + 3]) b += 8;
			switch(b) {
				case 0: s+="0"; break;
				case 1: s+="1"; break;
				case 2: s+="2"; break;
				case 3: s+="3"; break;
				case 4: s+="4"; break;
				case 5: s+="5"; break;
				case 6: s+="6"; break;
				case 7: s+="7"; break;
				case 8: s+="8"; break;
				case 9: s+="9"; break;
				case 10: s+="a"; break;
				case 11: s+="b"; break;
				case 12: s+="c"; break;
				case 13: s+="d"; break;
				case 14: s+="e"; break;
				case 15: s+="f"; break;
			}
			c = i + 4;
		}
		if(c < input.length) {
			boolean[] n = new boolean[input.length - c];
			for(int i = c; i < input.length; i++) {
				n[i - c] = input[i];
			}
			s += excess(n);
		}
		return s;
	}
	
	public static final String toBase32(boolean[] input)
	{
		if(input == null) throw new java.lang.NullPointerException();
		String s = "";
		int b;
		int c = 0;
		for(int i = 0; i < input.length - 4; i += 5) {
			b = 0;
			if(input[i]) b += 1;
			if(input[i + 1]) b += 2;
			if(input[i + 2]) b += 4;
			if(input[i + 3]) b += 8;
			if(input[i + 4]) b += 16;
			switch(b) {
				case 0: s+="0"; break;
				case 1: s+="1"; break;
				case 2: s+="2"; break;
				case 3: s+="3"; break;
				case 4: s+="4"; break;
				case 5: s+="5"; break;
				case 6: s+="6"; break;
				case 7: s+="7"; break;
				case 8: s+="8"; break;
				case 9: s+="9"; break;
				case 10: s+="a"; break;
				case 11: s+="b"; break;
				case 12: s+="c"; break;
				case 13: s+="d"; break;
				case 14: s+="e"; break;
				case 15: s+="f"; break;
				case 16: s+="g"; break;
				case 17: s+="h"; break;
				case 18: s+="i"; break;
				case 19: s+="j"; break;
				case 20: s+="k"; break;
				case 21: s+="l"; break;
				case 22: s+="m"; break;
				case 23: s+="n"; break;
				case 24: s+="o"; break;
				case 25: s+="p"; break;
				case 26: s+="q"; break;
				case 27: s+="r"; break;
				case 28: s+="s"; break;
				case 29: s+="t"; break;
				case 30: s+="u"; break;
				case 31: s+="v"; break;
			}
			c = (i + 5);
		}
		if(c < input.length) {
			final boolean[] n = new boolean[input.length - c];
			for(int i = c; i < input.length; i++) {
				n[i - c] = input[i];
			}
			s += excess(n);
		}
		return s;
	}
	
	public static final String toBase64(boolean[] input)
	{
		if(input == null) throw new java.lang.NullPointerException();
		String s = "";
		int b;
		int c = 0;
		for(int i = 0; i < input.length - 5; i += 6) {
			b = 0;
			if(input[i]) b += 1;
			if(input[i + 1]) b += 2;
			if(input[i + 2]) b += 4;
			if(input[i + 3]) b += 8;
			if(input[i + 4]) b += 16;
			if(input[i + 5]) b += 32;
			switch(b) {
				case 0: s+="0"; break;
				case 1: s+="1"; break;
				case 2: s+="2"; break;
				case 3: s+="3"; break;
				case 4: s+="4"; break;
				case 5: s+="5"; break;
				case 6: s+="6"; break;
				case 7: s+="7"; break;
				case 8: s+="8"; break;
				case 9: s+="9"; break;
				case 10: s+="a"; break;
				case 11: s+="b"; break;
				case 12: s+="c"; break;
				case 13: s+="d"; break;
				case 14: s+="e"; break;
				case 15: s+="f"; break;
				case 16: s+="g"; break;
				case 17: s+="h"; break;
				case 18: s+="i"; break;
				case 19: s+="j"; break;
				case 20: s+="k"; break;
				case 21: s+="l"; break;
				case 22: s+="m"; break;
				case 23: s+="n"; break;
				case 24: s+="o"; break;
				case 25: s+="p"; break;
				case 26: s+="q"; break;
				case 27: s+="r"; break;
				case 28: s+="s"; break;
				case 29: s+="t"; break;
				case 30: s+="u"; break;
				case 31: s+="v"; break;
				case 32: s+="w"; break;
				case 33: s+="x"; break;
				case 34: s+="y"; break;
				case 35: s+="z"; break;
				case 36: s+="A"; break;
				case 37: s+="B"; break;
				case 38: s+="C"; break;
				case 39: s+="D"; break;
				case 40: s+="E"; break;
				case 41: s+="F"; break;
				case 42: s+="G"; break;
				case 43: s+="H"; break;
				case 44: s+="I"; break;
				case 45: s+="J"; break;
				case 46: s+="K"; break;
				case 47: s+="L"; break;
				case 48: s+="M"; break;
				case 49: s+="N"; break;
				case 50: s+="O"; break;
				case 51: s+="P"; break;
				case 52: s+="Q"; break;
				case 53: s+="R"; break;
				case 54: s+="S"; break;
				case 55: s+="T"; break;
				case 56: s+="U"; break;
				case 57: s+="V"; break;
				case 58: s+="W"; break;
				case 59: s+="X"; break;
				case 60: s+="Y"; break;
				case 61: s+="Z"; break;
				case 62: s+="$"; break;
				case 63: s+="-"; break;
			}
			c = (i + 6);
		}
		if(c < input.length) {
			final boolean[] n = new boolean[input.length - c];
			for(int i = c; i < input.length; i++) {
				n[i - c] = input[i];
			}
			s += excess(n);
		}
		return s;
	}
	
	private static final String excess(final boolean[] input)
	{
		switch(input.length) {
			case 0: throw new java.lang.IllegalArgumentException();
			case 1: return toBinary(input);
			case 2: return toForth(input);
			case 3: return toOct(input);
			case 4: return toHex(input);
			case 5: return toBase32(input);
			case 6: return toBase64(input);
			default: throw new java.lang.IllegalArgumentException();
		}
	}

}
