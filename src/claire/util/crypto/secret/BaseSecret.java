package claire.util.crypto.secret;

import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.crypto.rng.primitive.ArrayRandomAdapter;
import claire.util.crypto.rng.primitive.StreamCipherRNG;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.IRandom;
import claire.util.standards.crypto.IStreamCipher;

public class BaseSecret {
	
	private final byte[] data;
	
	private byte[] o, o2;
	
	public BaseSecret(byte[] data)
	{
		this.data = data;
	}
	
	public void condense(IHash<?, ?> hash, int rounds, byte[] bytes, int off, int len) 
	{
		byte[] tb = new byte[1];
		if(o == null || o.length < hash.outputLength())
			o = new byte[hash.outputLength()];
		hash.add(tb);
		hash.add(data);
		for(int i = 1; i < rounds; i++) {
			hash.finish(o);
			hash.add(o, 0, hash.outputLength());
		}
		hash.finish(o);
		
		while(len > 0) {
			tb[0]++;
			hash.add(o, 0, hash.outputLength());
			hash.add(tb);
			hash.add(data);
			hash.finish(o);
			System.arraycopy(o, 0, bytes, off, hash.outputLength() > len ? len : hash.outputLength());
			off += hash.outputLength();
			len -= hash.outputLength();
		}
		Arrays.fill(o, (byte) 0);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <Key extends IKey<Key>> IRandom<?, ?> initRNG(IHash<?, ?> hash, int rounds, IStreamCipher<Key, ?> stream, CryptoString s) throws InstantiationException 
	{
		KeyFactory<Key> fac = stream.keyFactory();
		int bytesReq = fac.bytesRequired(s);
		if(o2 == null || o2.length < bytesReq)
			o2 = new byte[bytesReq];
		this.condense(hash, rounds, o2, 0, bytesReq);
		stream.setKey(fac.random(new ArrayRandomAdapter(o2), s)); 
		Arrays.fill(o2, (byte) 0);
		return new StreamCipherRNG(stream);
	}

}
