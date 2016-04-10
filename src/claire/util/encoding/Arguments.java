package claire.util.encoding;

public class Arguments {
	
	/*
	 * No need for a fancy map here, the amount of arguments is generally low and 
	 * this code is generally only run at startup.
	 */
	private final String[] switches;
	private final String[] index;
	private final String[] words;
	
	public Arguments(String[] args)
	{
		//Prescan
		int word = 0;
		int swit = 0;
		for(String s : args)	
		{
			if(s.charAt(0) == '-' && s.length() > 1)
			{
				if(s.charAt(1) == '1')
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
	
	public String[] getSwitches()
	{
		return this.switches;
	}
	
	public String[] getWordArgIndex()
	{
		return this.index;
	}
	
	public String[] getWords()
	{
		return this.words;
	}
	
	public boolean isSwitched(String arg)
	{
		for(String s : switches)
			if(s.equals(arg))
				return true;
		return false;
	}
	
	public boolean hasArg(String arg)
	{
		for(String s : index)
			if(s.equals(arg))
				return true;
		return false;
	}
	
	public String getArg(String arg)
	{
		for(int i = 0; i < index.length; i++)
			if(index[i].equals(arg))
				return words[i];
		return null;
	}

}
