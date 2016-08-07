package claire.util.encoding;

import claire.util.logging.Log;

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
	
	public static final int test()
	{
		int e = 0;
		try {
			int[] in = new int[] {
				500, 5000, 4997, 538209483
			};
			String[] out = new String[] {
				"500 LOL", "5 KLOL", "4 KLOL", "538 MLOL"	
			};
			for(int i = 0; i < in.length; i++)
				if(!out[i].equals(toSI("LOL", in[i]))) {
					Log.err.println("SI equality test failed for " + in[i] + " LOL ==> " + out[i]);
					e++;
				}
			int[] in2 = new int[] {
				500, 5000, 4997, 538209483
			};
			String[] out2 = new String[] {
				"500 LOL", "5.0 KLOL", "4.997 KLOL", "538.209 MLOL"	
			};
			for(int i = 0; i < in2.length; i++)
				if(!out2[i].equals(toSiFP("LOL", in2[i]))) {
					Log.err.println("SI equality test failed for " + in2[i] + " LOL ==> " + out2[i]);
					e++;
				}
		} catch(Exception ex) {
			Log.err.println(ex.getClass().getSimpleName() + ": " + ex.getMessage() + " encountered while testing SI.java");
			Log.err.println((Object[]) ex.getStackTrace());
			return e + 1;
		}
		return e;
	}
	
}
