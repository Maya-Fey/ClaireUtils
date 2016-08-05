package claire.util.encoding;

public class SI
{
	private static final String[] PREFIX = new String[] {
		"", "K", "M", "G", "T", "P", "E", "Z", "Y"
	};
	
	public static String toSI(String unit, long amt)
	{
		int pos = 0;
		while(amt > 1000) {
			pos++;
			amt /= 1000;
		}
		return PREFIX[pos] + unit + amt;
	}
	
}
