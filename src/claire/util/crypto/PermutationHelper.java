package claire.util.crypto;

import claire.util.memory.Bits;

public class PermutationHelper {
	
	public static final long permute(long l, byte[] permute)
	{
		long l2 = 0;
		for(int i = 0; i < 64; i++)
			if((l & Bits.BIT64_TABLE[i]) != 0) 
				l2 |= Bits.BIT64_TABLE[permute[i]];
		return l2;
	}
	
	public static final long permute(long l, byte[] permute, int len)
	{
		long l2 = 0;
		for(int i = 0; i < len; i++)
			if((l & Bits.BIT64_TABLE[i]) != 0) 
				l2 |= Bits.BIT64_TABLE[permute[i]];
		return l2;
	}

}
