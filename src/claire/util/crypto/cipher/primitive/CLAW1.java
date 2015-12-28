package claire.util.crypto.cipher.primitive;

import claire.util.crypto.cipher.key.KeyCLAW;
import claire.util.standards.IRandom;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.ISymmetric;

public class CLAW1 implements ISymmetric<KeyCLAW> {
	
	private final IHash hash;
	
	private KeyCLAW key;
	
	public CLAW1()
	{
		this.hash = null;
	}
	
	public CLAW1(IHash hash)
	{
		this.hash = hash;
	}
	
	public CLAW1(IHash hash, KeyCLAW key)
	{
		this(hash);
		this.key = key;
	}
	
	public CLAW1(KeyCLAW key)
	{
		this();
		this.key = key;
	}

	public KeyCLAW getKey()
	{
		return this.key;
		
	}
	
	public void setKey(KeyCLAW t)
	{
		this.key = t;
	}

	public void destroyKey()
	{
		this.key.erase();
	}

	@Override
	public int plaintextSize()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int ciphertextSize()
	{
		return 0;
	}

	@Override
	public void decryptBlock(byte[] block, int start)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encryptBlock(byte[] block, int start)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public KeyCLAW newKey(IRandom rand)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void genKey(IRandom rand)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void reset() {}

}
