package claire.util.crypto.hash.primitive;

import claire.util.standards.crypto.IHash;

public class JH {

	public IHash<?, ?> getBlake(int size) 
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

}
