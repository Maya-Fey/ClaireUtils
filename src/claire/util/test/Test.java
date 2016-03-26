package claire.util.test;

import claire.util.crypto.cipher.key.block.KeyAES;
import claire.util.crypto.cipher.key.block.KeyBlowfish;
import claire.util.crypto.cipher.key.block.KeyCAST5;
import claire.util.crypto.cipher.key.block.KeyCAST6;
import claire.util.crypto.cipher.key.block.KeyDES;
import claire.util.crypto.cipher.key.block.KeyFEAL;
import claire.util.crypto.cipher.key.block.KeyFEAL_CI;
import claire.util.crypto.cipher.key.block.KeyGOST;
import claire.util.crypto.cipher.key.block.KeyRC2;
import claire.util.crypto.cipher.key.block.KeyRC5;
import claire.util.crypto.cipher.key.block.KeyRC6;
import claire.util.crypto.cipher.key.block.KeySEED;
import claire.util.crypto.cipher.key.block.KeySkipjack;
import claire.util.crypto.cipher.key.block.KeyTEA;
import claire.util.crypto.cipher.key.block.KeyXTEA;
import claire.util.crypto.cipher.key.block.KeyXXTEA;
import claire.util.crypto.cipher.key.stream.KeyRC4;
import claire.util.crypto.cipher.primitive.block.AES;
import claire.util.crypto.cipher.primitive.block.Blowfish;
import claire.util.crypto.cipher.primitive.block.CAST5;
import claire.util.crypto.cipher.primitive.block.CAST6;
import claire.util.crypto.cipher.primitive.block.FEAL;
import claire.util.crypto.cipher.primitive.block.FEAL_CI;
import claire.util.crypto.cipher.primitive.block.GOST;
import claire.util.crypto.cipher.primitive.block.RC2;
import claire.util.crypto.cipher.primitive.block.RC5_16;
import claire.util.crypto.cipher.primitive.block.RC5_32;
import claire.util.crypto.cipher.primitive.block.RC5_64;
import claire.util.crypto.cipher.primitive.block.RC6;
import claire.util.crypto.cipher.primitive.block.SEED;
import claire.util.crypto.cipher.primitive.block.Skipjack;
import claire.util.crypto.cipher.primitive.block.TEA;
import claire.util.crypto.cipher.primitive.block.XTEA;
import claire.util.crypto.cipher.primitive.block.XXTEA;
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
		Log.info.println("Testing block");
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeyAES");
		reg += KeyAES.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeyBlowfish");
		reg += KeyBlowfish.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeyCAST5");
		reg += KeyCAST5.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeyCAST6");
		reg += KeyCAST6.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeyGOST");
		reg += KeyGOST.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeyRC2");
		reg += KeyRC2.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeyRC5");
		reg += KeyRC5.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeyRC6");
		reg += KeyRC6.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeySEED");
		reg += KeySEED.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeySkipjack");
		reg += KeySkipjack.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeyTEA");
		reg += KeyTEA.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeyXTEA");
		reg += KeyXTEA.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeyXXTEA");
		reg += KeyXXTEA.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeyDES");
		reg += KeyDES.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeyFEAL");
		reg += KeyFEAL.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.block.KeyFEAL_CI");
		reg += KeyFEAL_CI.test();
		Log.info.println("Testing stream");
		Log.info.println("Testing claire.util.crypto.cipher.key.stream.KeyRC4");
		reg += KeyRC4.test();
		Log.info.println("Testing ../primitive");
		Log.info.println("Testing block");
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.AES");
		reg += AES.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.Blowfish");
		reg += Blowfish.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.CAST5");
		reg += CAST5.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.CAST6");
		reg += CAST6.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.GOST");
		reg += GOST.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.RC2");
		reg += RC2.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.RC5_16");
		reg += RC5_16.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.RC5_32");
		reg += RC5_32.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.RC5_64");
		reg += RC5_64.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.RC6");
		reg += RC6.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.SEED");
		reg += SEED.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.Skipjack");
		reg += Skipjack.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.TEA");
		reg += TEA.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.XTEA");
		reg += XTEA.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.XXTEA");
		reg += XXTEA.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.FEAL");
		reg += FEAL.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.block.FEAL_CI");
		reg += FEAL_CI.test();
		if(reg > 0)
			Log.crit.println(reg + " regressions detected!");
		else
			Log.info.println("Success! No regressions detected!");
	}

}
