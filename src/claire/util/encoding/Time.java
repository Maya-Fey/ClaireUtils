package claire.util.encoding;

public final class Time {
	
	private static final long[] UNITS = new long[]
		{
			1, 		   //Seconds
			60, 	   //Minutes
			3600, 	   //Hours
			86400, 	   //Days
			604800,    //Weeks
			2592000,   //Months
			31536000,  //Years
			315360000, //Decades
		};
	
	private static final String[] STRINGS = new String[]
		{
			"Second",
			"Minute",
			"Hour",
			"Day",
			"Week",
			"Month",
			"Year",
			"Decade"
		};

	public static String getTimeAmount(long time, int levels, boolean months, boolean weeks)
	{
		String f = "";
		while(time < UNITS[levels - 1])
			levels--;
		int unit = 0;
		while(time >= UNITS[unit + 1])
			unit = incUnit(unit, months, weeks);
		while(levels > 0)
		{	
			int q = (int) (time / UNITS[unit]);
			if(q == 1)
				f += q + " " + STRINGS[unit];
			else
				f += q + " " + STRINGS[unit] + "s";
			if(levels > 2)
				f += ", ";
			else if(levels == 2)
				f += " and ";
			time %= UNITS[unit];
			levels--;
			unit--;
		}
		return f;
	}
	
	public static int incUnit(int unit, boolean months, boolean weeks)
	{
		if(unit > 3 && !weeks)
			unit++;
		if(unit > 4 && !months)
			unit++;
		return unit + 1;
	}
	
}
