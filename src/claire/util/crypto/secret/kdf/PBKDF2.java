package claire.util.crypto.secret.kdf;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.HashRegistry;
import claire.util.crypto.mac.HMAC;
import claire.util.memory.Bits;
import claire.util.standards.crypto.IHash;

public class PBKDF2<Hash extends IHash<Hash, ?>> 
	   implements IKDF {
	
	private final HMAC<?> mac;
	
	private final byte[] i = new byte[4];
	private final byte[] t;
	private final int iter;
	
	public PBKDF2(HMAC<?> hash, int iter)
	{
		this.mac = hash;
		this.iter = iter - 1;
		t = new byte[hash.outputLength()];
	}
	
	public void derive(byte[] data, int start0, int len0, byte[] salt, int start1, int len1, byte[] key, int start2, int keylen)
	{
		int i = 0, j;
		while(keylen > mac.outputLength()) {
			Bits.BigEndian.intToBytes(i++, this.i, 0);
			mac.start();
			mac.add(data, start0, len0);
			mac.add(salt, start1, len1);
			mac.add(this.i, 0, 4);
			mac.finish(t, 0);
			j = iter;
			while(j-- > 0) {
				mac.auth(t, 0, t.length, t, 0);
			}
			System.arraycopy(t, 0, key, start2, mac.outputLength());
			start2 += mac.outputLength();
			keylen -= mac.outputLength();
		}
		
		Bits.BigEndian.intToBytes(i, this.i, 0);
		mac.start();
		mac.add(data, start0, len0);
		mac.add(salt, start1, len1);
		mac.add(this.i, 0, 4);
		mac.finish(t, 0);
		j = iter;
		while(j-- > 0) {
			mac.auth(t, 0, t.length, t, 0);
		}
		System.arraycopy(t, 0, key, start2, keylen);
	}
	
	public static final PBKDF2Factory factory = new PBKDF2Factory();
		
	@SuppressWarnings("rawtypes")
	public static final class PBKDF2Factory
						extends KDFFactory<PBKDF2> {

		@SuppressWarnings("unchecked")
		public PBKDF2 build(CryptoString str) throws InstantiationException
		{
			if(str.args() < 2) 
				throw new java.lang.InstantiationException("PBKDF2 requires a hash function and number of iterations");
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
			return new PBKDF2(new HMAC(hash), iter);
		}
		
	}
}
