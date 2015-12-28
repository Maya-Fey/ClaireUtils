package claire.util.crypto.hash;

import java.io.File;
import java.io.IOException;

import claire.util.io.IOUtils;
import claire.util.standards.crypto.IHash;

public class HashFunction {
	
	private final IHash hash;
	
	public HashFunction(IHash hash)
	{
		this.hash = hash;
	}
	
	public void addFile(File file) throws IOException
	{
		byte[] bytes = IOUtils.toBytes(file);
		hash.add(bytes);
	}

}
