package claire.util.test;

import claire.util.crypto.cipher.key.KeyAES;
import claire.util.crypto.cipher.key.KeyBlowfish;
import claire.util.crypto.cipher.primitive.AES;
import claire.util.crypto.cipher.primitive.Blowfish;
import claire.util.logging.Log;

public class Test {
	
	public static final void runTests() throws Exception
	{
		int reg = 0;
		Log.info.println("Beginning tests...");
		Log.info.println("Testing claire.util");
		Log.info.println("Testing crypto");
		Log.info.println("Testing cipher");
		Log.info.println("Testing key");
		Log.info.println("Testing KeyAES");
		reg += KeyAES.test();
		Log.info.println("Testing KeyBlowfish");
		reg += KeyBlowfish.test();
		Log.info.println("Testing primitive");
		Log.info.println("Testing AES");
		reg += AES.test();
		Log.info.println("Testing Blowfish");
		reg += Blowfish.test();
		if(reg > 0)
			Log.crit.println(reg + " regressions detected!");
		else
			Log.info.println("Success! No regressions detected!");
	}

}
