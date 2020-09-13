package claire.util.crypto.secret.kdf;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.HashRegistry;
import claire.util.memory.Bits;
import claire.util.standards.crypto.IHash;

public class HBKDF<Hash extends IHash<Hash, ?>> 
	   implements IKDF {
	
	private final Hash hash;
	
	private final byte[] i = new byte[4];
	private final byte[] t;
	private final int iter;
	
	public HBKDF(Hash hash, int iter)
	{
		this.hash = hash;
		this.iter = iter - 1;
		t = new byte[hash.outputLength()];
	}
	
	public void derive(byte[] data, int start0, int len0, byte[] salt, int start1, int len1, byte[] key, int start2, int keylen)
	{
		int i = 0, j;
		while(keylen > hash.outputLength()) {
			Bits.BigEndian.intToBytes(i++, this.i, 0);
			hash.add(data, start0, len0);
			hash.add(salt, start1, len1);
			hash.add(this.i, 0, 4);
			hash.finish(t);
			j = iter;
			while(j-- > 0) {
				hash.add(t);
				hash.finish(t);
			}
			System.arraycopy(t, 0, key, start2, hash.outputLength());
			start2 += hash.outputLength();
			keylen -= hash.outputLength();
		}
		
		Bits.BigEndian.intToBytes(i, this.i, 0);
		hash.add(data, start0, len0);
		hash.add(salt, start1, len1);
		hash.add(this.i, 0, 4);
		hash.finish(t);
		j = iter;
		while(j-- > 0) {
			hash.add(t);
			hash.finish(t);
		}
		System.arraycopy(t, 0, key, start2, keylen);
	}
	
	public static final HBKDFFactory factory = new HBKDFFactory();
	
	@SuppressWarnings("rawtypes")
	public static final class HBKDFFactory
						extends KDFFactory<HBKDF> {

		@SuppressWarnings("unchecked")
		public HBKDF build(CryptoString str) throws InstantiationException
		{
			if(str.args() < 2) 
				throw new java.lang.InstantiationException("HBKDF requires a hash function and number of iterations");
			else if(str.args() > 2)
				throw new java.lang.InstantiationException("Too many arguments; only a hash function and number of iterations are required");
			IHash hash = (IHash) HashRegistry.getHash(str.nextArg().toString());
			if(hash == null)
				throw new java.lang.InstantiationException("Error: Selected hash not found");
			int iter = str.nextArg().toInt();
			if(iter < 1) 
				throw new java.lang.InstantiationException("Error: at least one iteration is required");
			if(iter > 100000000)
				throw new java.lang.InstantiationException("Error: 100,000,000 is an excessive iteration count");
			return new HBKDF(hash, iter);
		}
		
	}

}
