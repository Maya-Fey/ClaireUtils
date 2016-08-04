package claire.util.encoding;

/**
 * A class that collects arguments given to a command-line program.
 * <br>
 * <br>
 * There are two kinds of arguments:
 * <ul>
 * 	<li>Switch arguments, which begin with a '-' and are only on or off</li>
 * 	<li>Value arguments, which begin with a '--' and contain a string value</li>
 * </ul>
 */
public class Arguments {
	
	/*
	 * No need for a fancy map here, the amount of arguments is generally low and 
	 * this code is generally only run at startup.
	 */
	private final String[] switches;
	private final String[] index;
	private final String[] words;
	
	/**
	 * Creates an arguments object from a String array containing the arguments from a start index
	 * <br>
	 * Accepts:
	 * <ul>
	 * 	<li>A string array</li>
	 * 	<li>A start value less than the length of args and greater than zero</li>
	 * </ul>
	 * Errors:
	 * <br>
	 * Throws a NullPointerException if <code>args</code> is <code>null</code>
	 * <br>
	 * If an out-of bounds start is passed than a ArrayIndexOutOfBoundsException will be thrown
	 */
	public Arguments(String[] args, int start)
	{
		//Prescan
		int word = 0;
		int swit = 0;
		for(int i = start; i < args.length; i++)
		{
			String s = args[i];
			if(s.charAt(0) == '-' && s.length() > 1)
			{
				if(s.charAt(1) == '-')
					word++;
				else 
					swit++;
			}
		}
		switches = new String[swit];
		index = new String[word];
		words = new String[word];
		swit = 0;
		word = 0;
		for(int i = start; i < args.length; i++) {
			String arg = args[i];
			if(arg.charAt(0) == '-' && arg.length() > 1)
				if(arg.charAt(1) == '-') {
					index[word  ] = arg;
					words[word++] = args[++i];
				} else
					switches[swit++] = arg;
		}
	}
	
	/**
	 * Creates an arguments object from a String array
	 * <br>
	 * Accepts:
	 * <ul>
	 * 	<li>A string array</li>
	 * </ul>
	 * Errors:
	 * <br>
	 * Throws a NullPointerException if <code>args</code> is <code>null</code>
	 */
	public Arguments(String[] args)
	{
		//Prescan
		int word = 0;
		int swit = 0;
		for(String s : args)	
		{
			if(s.charAt(0) == '-' && s.length() > 1)
			{
				if(s.charAt(1) == '-')
					word++;
				else 
					swit++;
			}
		}
		switches = new String[swit];
		index = new String[word];
		words = new String[word];
		swit = 0;
		word = 0;
		for(int i = 0; i < args.length; i++) {
			String arg = args[i];
			if(arg.charAt(0) == '-' && arg.length() > 1)
				if(arg.charAt(1) == '-') {
					index[word  ] = arg;
					words[word++] = args[++i];
				} else
					switches[swit++] = arg;
		}
	}
	
	/**
	 * Returns an internal string array containing all switch arguments
	 * <br>
	 * <br>
	 * Modifying this array may result in undefined behavior
	 */
	public String[] getSwitches()
	{
		return this.switches;
	}
	
	/**
	 * Returns an internal string array containing the index for the complex arguments
	 * <br>
	 * <br>
	 * Modifying this array may result in undefined behavior
	 */
	public String[] getWordArgIndex()
	{
		return this.index;
	}
	
	/**
	 * Returns an internal string array containing the data for the complex arguments
	 * <br>
	 * <br>
	 * Modifying this array may result in undefined behavior
	 */
	public String[] getWords()
	{
		return this.words;
	}
	
	/**
	 * Returns whether the argument given has been switched
	 * <br>
	 * Accepts
	 * <ul>
	 *  <li>A string containing the arg you wish to check for</li>
	 * </ul>
	 * This function is safe
	 */
	public boolean isSwitched(String arg)
	{
		for(String s : switches)
			if(s.equals(arg))
				return true;
		return false;
	}
	
	/**
	 * Returns whether a complex argument under the given name has been passed
	 * <br>
	 * Accepts
	 * <ul>
	 *  <li>A string containing the arg you wish to check for</li>
	 * </ul>
	 * This function is safe
	 */
	public boolean hasArg(String arg)
	{
		for(String s : index)
			if(s.equals(arg))
				return true;
		return false;
	}
	
	/**
	 * Returns the value of a complex argument
	 * <br>
	 * Accepts
	 * <ul>
	 *  <li>A string containing the arg you wish to obtain the value of</li>
	 * </ul>
	 * This function is safe
	 * <br>
	 * <br>
	 * This function returns a String containing the value. If your argument is not found, <code>null</code> is returned
	 */
	public String getArg(String arg)
	{
		for(int i = 0; i < index.length; i++)
			if(index[i].equals(arg))
				return words[i];
		return null;
	}
	
	/**
	 * Checks for any arguments not contained in the <code>switches</code> or <code>values</code> arrays
	 * <br>
	 * <br>
	 * Accepts
	 * <ul>
	 * 	<li>A string array representing all the possible switch arguments</li>
	 * 	<li>A string array representing all the possible complex arguments</li>
	 * </ul>
	 * Errors:
	 * <br>
	 * Throws a NullPointerException if either argument is <code>null</code>
	 * <br>
	 * <br>
	 * Returns:
	 * <br>
	 * <b>true</b> if an erronous argument is found and <b>false</b> if none is found
	 */
	public boolean outOfBounds(String[] switches, String[] values)
	{
		for(String swval : this.switches) {
			boolean nf = true;
			for(String swtest : switches) 
				if(swtest.equals(swval)) {
					nf = false;
					break;
				}
			if(nf)
				return true;
		}
		for(String coval : this.index) {
			boolean nf = true;
			for(String cotest : values) 
				if(cotest.equals(coval)) {
					nf = false;
					break;
				}
			if(nf)
				return true;
		}
		return false;
	}

}
