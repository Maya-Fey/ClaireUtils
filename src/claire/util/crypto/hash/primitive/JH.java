package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.HashFactory;
import claire.util.standards.crypto.IHash;

public class JH {

	public IHash<?, ?> getJH(int size) 
	{
		switch(size)
		{
			case 224:
				return new JH224();
			case 256:
				return new JH256();
			case 384:
				return new JH384();
			case 512:
				return new JH512();
			default:
				throw new java.lang.IllegalArgumentException("JH only supports digest sizes of 224, 256, 384, or 512 bits.");
		}
	}
	
	public static HashFactory<JHCore<?>> factory()
	{
		return factory;
	}
	
	public static final JHFactory factory = new JHFactory();
	
	public static final class JHFactory extends HashFactory<JHCore<?>>
	{

		public JHCore<?> build(CryptoString str)
		{
			switch(str.nextArg().toString())
			{
				case "224":
					return new JH224();
				case "256":
					return new JH256();
				case "384":
					return new JH384();
				case "512":
					return new JH512();
				default:
					throw new java.lang.NullPointerException();
			}
		}
		
	}

}
