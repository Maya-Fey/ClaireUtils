package claire.util.encoding;

import java.io.IOException;

import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.buffer.ByteArrayIncomingStream;
import claire.util.standards.io.IDecoder;
import claire.util.standards.io.IIncomingStream;

public class StringFactory extends Factory<CString> {
	
	private final IDecoder decoder;

	public StringFactory(String encoding) 
	{
		super(CString.class);
		this.decoder = new Decoder(encoding);
	}
	
	public StringFactory()
	{
		this("UTF-16");
	}

	public CString resurrect(byte[] data, int start) 
	{
		int len = Bits.intFromBytes(data, start);
		char[] chars = new char[len];
		ByteArrayIncomingStream is = new ByteArrayIncomingStream(data);
		is.skip(4 + start);
		decoder.setToDecode(is);
		try {
			decoder.readChars(chars);
		} catch (IOException e) {}
		return new CString(chars);
	}

	public CString resurrect(IIncomingStream stream) throws IOException
	{
		decoder.setToDecode(stream);
		return new CString(decoder.readChars(stream.readInt()));
	}

}
