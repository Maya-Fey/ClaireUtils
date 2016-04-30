package claire.util.crypto.exchange.dh;

import java.io.IOException;

import claire.util.io.Factory;
import claire.util.io.IOUtils;
import claire.util.math.UInt;
import claire.util.standards.IPersistable;
import claire.util.standards.IUUID;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class DHParams 
	   implements IPersistable<DHParams>,
	   			  IUUID<DHParams> {

	private final UInt modulus;
	private final UInt generator;
	
	public DHParams(UInt generator, UInt modulus) 
	{
		this.generator = generator;
		this.modulus = modulus;
	}
	
	public UInt getModulus()
	{
		return modulus;
	}
	
	public UInt getGenerator()
	{
		return generator;
	}
	
	public boolean sameAs(DHParams obj)
	{
		return this.modulus.equals(obj.modulus) && this.generator.equals(obj.generator);
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.DHPARAMS;
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeIntArr(generator.getArr());
		stream.writeIntArr(modulus.getArr());
	}

	public void export(byte[] bytes, int offset)
	{
		offset = IOUtils.writeArr(generator.getArr(), bytes, offset);
		IOUtils.writeArr(modulus.getArr(), bytes, offset);
	}
	
	public int exportSize()
	{
		return (modulus.getIntLen() * 4) + (generator.getIntLen() * 4) + 8;
	}

	public Factory<DHParams> factory()
	{
		return factory;
	}
	
	public static final DHParamsFactory factory = new DHParamsFactory();
	
	private static final class DHParamsFactory extends Factory<DHParams>
	{
		public DHParamsFactory()
		{
			super(DHParams.class);
		}

		public DHParams resurrect(byte[] data, int start) throws InstantiationException
		{
			UInt gen = new UInt(IOUtils.readIntArr(data, start)); start += gen.getIntLen() * 4 + 4;
			UInt mod = new UInt(IOUtils.readIntArr(data, start));
			return new DHParams(gen, mod);
		}

		public DHParams resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new DHParams(new UInt(stream.readIntArr()), new UInt(stream.readIntArr()));
		}
	}
}
