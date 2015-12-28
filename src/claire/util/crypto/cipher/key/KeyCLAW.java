package claire.util.crypto.cipher.key;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.cipher.StateSpaceRequiredException;
import claire.util.io.Factory;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.ISymmetricKey;
import claire.util.standards.io.IOutgoingStream;

public class KeyCLAW 
	   implements ISymmetricKey<KeyCLAW> {
	
	private final byte[] key;
	
	public KeyCLAW(byte[] in)
	{
		this.key = in;
	}

	public void erase()
	{
		Arrays.fill(key, (byte) 0);
	}
	
	public byte[] getSessionKey(IHash func, int len, byte[] session)
	{
		if((func.outputLength() & len) != 0)
			throw new java.lang.ArrayStoreException("Error: Hash length incompatible with desired length");
		byte[] key = new byte[len];
		byte[] prev = new byte[func.outputLength()];
		int pos = 0;
		final int step = func.outputLength();
		while(pos < len)
		{
			func.add(prev);
			func.add(this.key);
			func.add(session);
			func.finish(prev);
			System.arraycopy(prev, 0, key, pos, step);
			pos += step;
		}
		return key;
	}

	public void encryptBlock(byte[] block, int start) throws StateSpaceRequiredException
	{
		throw new StateSpaceRequiredException();
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1) throws StateSpaceRequiredException
	{
		throw new StateSpaceRequiredException();
	}

	public void decryptBlock(byte[] block, int start) throws StateSpaceRequiredException
	{
		throw new StateSpaceRequiredException();
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1) throws StateSpaceRequiredException
	{
		throw new StateSpaceRequiredException();
	}

	@Override
	public void export(IOutgoingStream stream) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void export(byte[] bytes, int offset)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int exportSize()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Factory<KeyCLAW> factory()
	{
		// TODO Auto-generated method stub
		return null;
	}

	

}
