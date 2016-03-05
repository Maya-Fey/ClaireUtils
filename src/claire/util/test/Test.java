package claire.util.test;

import claire.util.crypto.cipher.key.KeyAES;
import claire.util.crypto.cipher.key.KeyBlowfish;
import claire.util.crypto.cipher.key.KeyCAST5;
import claire.util.crypto.cipher.key.KeyCAST6;
import claire.util.crypto.cipher.key.KeyDES;
import claire.util.crypto.cipher.key.KeyFEAL;
import claire.util.crypto.cipher.key.KeyGOST;
import claire.util.crypto.cipher.key.KeyRC2;
import claire.util.crypto.cipher.key.KeyRC5;
import claire.util.crypto.cipher.key.KeyRC6;
import claire.util.crypto.cipher.key.KeySEED;
import claire.util.crypto.cipher.key.KeySkipjack;
import claire.util.crypto.cipher.key.KeyTEA;
import claire.util.crypto.cipher.key.KeyXTEA;
import claire.util.crypto.cipher.key.KeyXXTEA;
import claire.util.crypto.cipher.primitive.AES;
import claire.util.crypto.cipher.primitive.Blowfish;
import claire.util.crypto.cipher.primitive.CAST5;
import claire.util.crypto.cipher.primitive.CAST6;
import claire.util.crypto.cipher.primitive.FEAL;
import claire.util.crypto.cipher.primitive.GOST;
import claire.util.crypto.cipher.primitive.RC2;
import claire.util.crypto.cipher.primitive.RC5_16;
import claire.util.crypto.cipher.primitive.RC5_32;
import claire.util.crypto.cipher.primitive.RC5_64;
import claire.util.crypto.cipher.primitive.RC6;
import claire.util.crypto.cipher.primitive.SEED;
import claire.util.crypto.cipher.primitive.Skipjack;
import claire.util.crypto.cipher.primitive.TEA;
import claire.util.crypto.cipher.primitive.XTEA;
import claire.util.crypto.cipher.primitive.XXTEA;
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
		Log.info.println("Testing claire.util.crypto.cipher.key.KeyAES");
		reg += KeyAES.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.KeyBlowfish");
		reg += KeyBlowfish.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.KeyCAST5");
		reg += KeyCAST5.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.KeyCAST6");
		reg += KeyCAST6.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.KeyGOST");
		reg += KeyGOST.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.KeyRC2");
		reg += KeyRC2.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.KeyRC5");
		reg += KeyRC5.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.KeyRC6");
		reg += KeyRC6.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.KeySEED");
		reg += KeySEED.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.KeySkipjack");
		reg += KeySkipjack.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.KeyTEA");
		reg += KeyTEA.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.KeyXTEA");
		reg += KeyXTEA.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.KeyXXTEA");
		reg += KeyXXTEA.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.KeyDES");
		reg += KeyDES.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.KeyFEAL");
		reg += KeyFEAL.test();
		Log.info.println("Testing primitive");
		Log.info.println("Testing claire.util.crypto.cipher.primitive.AES");
		reg += AES.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.Blowfish");
		reg += Blowfish.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.CAST5");
		reg += CAST5.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.CAST6");
		reg += CAST6.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.GOST");
		reg += GOST.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.RC2");
		reg += RC2.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.RC5_16");
		reg += RC5_16.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.RC5_32");
		reg += RC5_32.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.RC5_64");
		reg += RC5_64.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.RC6");
		reg += RC6.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.SEED");
		reg += SEED.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.Skipjack");
		reg += Skipjack.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.TEA");
		reg += TEA.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.XTEA");
		reg += XTEA.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.XXTEA");
		reg += XXTEA.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.FEAL");
		reg += FEAL.test();
		if(reg > 0)
			Log.crit.println(reg + " regressions detected!");
		else
			Log.info.println("Success! No regressions detected!");
	}

}
