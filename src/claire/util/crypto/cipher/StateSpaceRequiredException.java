package claire.util.crypto.cipher;

@SuppressWarnings("serial")
public class StateSpaceRequiredException extends Exception {

	public StateSpaceRequiredException() 
	{
		super("This cipher requires more then keyspace to function.");
	}

}
