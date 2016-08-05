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
		return amt + " " + PREFIX[pos] + unit;
	}
	
	public static String toSiFP(String unit, long amt)
	{
		if(amt < 1000)
			return amt + " " + unit;
		int pos = 1;
		int prev = (int) (amt % 1000);
		amt /= 1000;
		while(amt > 1000) {
			pos++;
			prev = (int) (amt % 1000);
			amt /= 1000;
		}
		return amt + "." + prev + " " + PREFIX[pos] + unit;
	}
	
}
