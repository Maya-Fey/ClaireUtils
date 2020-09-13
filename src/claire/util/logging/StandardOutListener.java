package claire.util.logging;

public class StandardOutListener 
	   implements ILogListener {

	public void println(String s)
	{
		System.out.println(s);
	}

}
