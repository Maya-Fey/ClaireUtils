package claire.util.crypto.cipher.key;

import java.io.IOException;

import claire.util.crypto.cipher.StateSpaceRequiredException;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.CObject;
import claire.util.standards.IRandom;
import claire.util.standards.crypto.ISymmetricKey;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

//This is getting wierd...
public abstract class ByteKey<Type extends ByteKey<Type>>
				implements ISymmetricKey<Type>,
						   CObject<Type> {
	
	private byte[] key;
	private final Class<Type> cLass;
	
	protected ByteKey(Class<Type> cLass, byte[] key)
	{
		this.cLass = cLass;
		int len = this.getLength(key, key.length);
		if(len == key.length)
			this.key = key;
		else {
			this.key = new byte[len];
			System.arraycopy(key, 0, this.key, 0, len);
		}
	}
	
	protected ByteKey(Class<Type> cLass, byte[] key, int size)
	{
		this.key = new byte[this.getLength(key, size)];
		System.arraycopy(key, 0, this.key, 0, size);
		this.cLass = cLass;
	}

	public void erase()
	{
		for(int i = 0; i < key.length; i++)
			key[i] = 0;
	}
	
	public byte[] getBytes() 
	{
		return this.key;
	}
	
	public int keySize()
	{
		return this.key.length;
	}

	public boolean sameAs(Type obj)
	{
		byte[] key = obj.getBytes();
		if(key.length == this.key.length) {
			for(int i = 0; i < key.length; i++)
				if(this.key[i] != key[i])
					return false;
			return true;
		} else
			return false;
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
	
	public void gen(IRandom rand)
	{
		for(int i = 0; i < key.length; i++)
			key[i] = rand.nextByte();
	}
	
	protected abstract int getLength(byte[] bytes, int len);
	protected abstract Type construct(byte[] bytes);
	
	public Type createDeepClone() 
	{
		byte[] n = ArrayUtil.copy(key);
		return this.construct(n);
	}
	
	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeInt(key.length);
		stream.writeBytes(key);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.intToBytes(key.length, bytes, 0);
		System.arraycopy(key, 0, bytes, offset + 4, key.length);
	}

	public int exportSize()
	{
		return key.length + 4;
	}
	
	public Factory<Type> factory()
	{
		return new BasicKeyFactory();
	}
	
	public class BasicKeyFactory extends Factory<Type>
	{

		protected BasicKeyFactory() 
		{
			super(ByteKey.this.cLass);
		}

		public Type resurrect(byte[] data)
		{
			return construct(data);
		}

		public Type resurrect(byte[] data, int start)
		{
			byte[] datanew = new byte[Bits.intFromBytes(data, start)];
			System.arraycopy(data, start + 4, datanew, 0, datanew.length);
			return construct(datanew);
		}

		public Type resurrect(IIncomingStream stream) throws IOException
		{
			return construct(stream.readBytes(stream.readInt()));
		}

	}
	
}
