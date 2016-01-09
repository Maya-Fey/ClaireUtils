package claire.util.test.crypto;

import claire.util.encoding.CString;
import claire.util.encoding.Hex;
import claire.util.logging.Log;
import claire.util.memory.util.ArrayBuilder;
import claire.util.standards.crypto.IHash;
import claire.util.test.Test;

public class HashTest {
	
	private final byte[] confirm;
	private final byte[] value;
	
	private final IHash hash;
	private final String hashName;
	
	public HashTest(String hashName, IHash hash, byte[] value, byte[] expected)
	{
		this.hash = hash;
		this.hashName = hashName;
		this.value = value;
		this.confirm = expected;
	}
	
	public void run()
	{
		byte[] bytes = hash.hash(value);
		if(confirm.length == bytes.length) {
			for(int i = 0; i < confirm.length; i++)
				if(confirm[i] != bytes[i]) {
					Test.reportError(hashName + " did not give expected output for " + new CString(value, "UTF-8"));
					Log.info.println(Hex.toHex(confirm));
					Log.info.println(Hex.toHex(bytes));
					break;
				}
		} else {
			Test.reportError(hashName + " did not give expected output length for " + new CString(value));
			Log.info.println(Hex.toHex(confirm));
			Log.info.println(Hex.toHex(bytes));
		}	
	}
	
	public static final class TestBuilder
	{
		private final IHash type;
		private final String name;
		
		private final ArrayBuilder<HashTest> arr = new ArrayBuilder<HashTest>(HashTest.class, 4);
		
		public TestBuilder(IHash type, String name)
		{
			this.name = name;
			this.type = type;
		}
		
		public void addTest(byte[] in, byte[] out)
		{
			arr.addElement(new HashTest(name, type, in, out));
		}
		
		public void addTest(CString in, CString out)
		{
			arr.addElement(new HashTest(name, type, in.toString().getBytes(), Hex.fromHex(out)));
		}
		
		public HashTest[] finish()
		{
			return arr.build();
		}
		
	}

}
