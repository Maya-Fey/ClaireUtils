package claire.util.crypto.mac;

import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.hash.HashRegistry;
import claire.util.crypto.mac.key.KeyHMAC;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

@SuppressWarnings("rawtypes")
public class HMAC<Hash extends IHash> 
	   implements IMAC<KeyHMAC> {
	
	private final Hash hash;
	
	private IState init, mid;
	
	private final byte[] ikey, okey, t;
	
	private KeyHMAC key;
	
	public HMAC(Hash hash)
	{
		this.hash = hash;
		ikey = new byte[hash.desiredInputLength()];
		okey = new byte[hash.desiredInputLength()];
		t = new byte[hash.outputLength()];
	}
	
	@SuppressWarnings("unchecked")
	public HMAC(CryptoString cstr) throws InstantiationException
	{
		if(cstr.args() < 2) 
			throw new java.lang.InstantiationException("A length and a hash identifier are required to create HMAC key");
		else if(cstr.args() > 3) 
			throw new java.lang.InstantiationException("Too many arguments; only a length in bits and a hash identifier are required");
		cstr.nextArg();
		cstr = cstr.nextLevel();
		HashFactory<?> factory = HashRegistry.getHash(cstr.nextArg().toString());
		hash = (Hash) factory.build(cstr);
		ikey = new byte[hash.desiredInputLength()];
		okey = new byte[hash.desiredInputLength()];
		t = new byte[hash.outputLength()];
	}

	public KeyHMAC getKey()
	{
		return key;
	}

	public void setKey(KeyHMAC t)
	{
		byte[] raw = t.getBytes();
		int len = raw.length;
		if(raw.length < hash.desiredInputLength())
			System.arraycopy(raw, 0, okey, 0, raw.length);
		else {
			hash.add(raw);
			hash.finish(this.t, 0, hash.desiredInputLength());
			len = hash.desiredInputLength();
		}
		Arrays.fill(ikey, (byte) 0x36);
		Arrays.fill(okey, (byte) 0x5c);
		
		for(int i = 0; i < len; i++) {
			ikey[i] ^= this.t[i];
			okey[i] ^= this.t[i];
		}
		
		hash.add(ikey);
		init = hash.getState();
		
		hash.reset();
		hash.add(okey);
		mid = hash.getState();
	}
	
	@SuppressWarnings("unchecked")
	public void auth(byte[] message, int start0, int len, byte[] out, int start1)
	{
		hash.loadState(init); 
		hash.add(message, start0, len);
		hash.finish(t);
		
		hash.loadState(mid);
		hash.add(t);
		hash.finish(out, start1);
	}

	public int outputLength()
	{
		return hash.outputLength();
	}
	
	public KeyFactory<KeyHMAC> keyFactory()
	{
		return KeyHMAC.factory;
	}

	@SuppressWarnings("unchecked")
	public void start()
	{
		hash.loadState(init); 
	}

	public void add(byte[] data, int start, int len)
	{
		hash.add(data, start, len);
	}

	@SuppressWarnings("unchecked")
	public void finish(byte[] out, int start)
	{
		hash.finish(t);
		
		hash.loadState(mid);
		hash.add(t);
		hash.finish(out, start);
	}


}
