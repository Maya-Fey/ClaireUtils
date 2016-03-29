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
import claire.util.crypto.cipher.key.stream.KeyIA;
import claire.util.crypto.cipher.key.stream.KeyIBAA;
import claire.util.crypto.cipher.key.stream.KeyRC4;
import claire.util.crypto.cipher.key.stream.KeyRC4_DROP;
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
import claire.util.crypto.cipher.primitive.stream.IA;
import claire.util.crypto.cipher.primitive.stream.IBAA;
import claire.util.crypto.cipher.primitive.stream.RC4;
import claire.util.crypto.hash.primitive.BLAKE224;
import claire.util.crypto.hash.primitive.BLAKE256;
import claire.util.crypto.hash.primitive.BLAKE384;
import claire.util.crypto.hash.primitive.BLAKE512;
import claire.util.crypto.hash.primitive.BMW224;
import claire.util.crypto.hash.primitive.BMW256;
import claire.util.crypto.hash.primitive.BMW384;
import claire.util.crypto.hash.primitive.BMW512;
import claire.util.crypto.hash.primitive.CRC16;
import claire.util.crypto.hash.primitive.CRC32;
import claire.util.crypto.hash.primitive.CRC8;
import claire.util.crypto.hash.primitive.MD2;
import claire.util.crypto.hash.primitive.MD4;
import claire.util.crypto.hash.primitive.MD5;
import claire.util.crypto.hash.primitive.SHA1;
import claire.util.crypto.hash.primitive.SHA2_224;
import claire.util.crypto.hash.primitive.SHA2_256;
import claire.util.logging.Log;
import claire.util.math.counters.IntCounter;
import claire.util.math.counters.LongCounter;

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
		Log.info.println("Testing claire.util.crypto.cipher.key.stream.KeyRC4_DROP");
		reg += KeyRC4_DROP.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.stream.KeyIA");
		reg += KeyIA.test();
		Log.info.println("Testing claire.util.crypto.cipher.key.stream.KeyIBAA");
		reg += KeyIBAA.test();
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
		Log.info.println("Testing stream");
		Log.info.println("Testing claire.util.crypto.cipher.primitive.stream.RC4");
		reg += RC4.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.stream.RC4.RC4State");
		reg += RC4.testState();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.stream.RC4_DROP");
		reg += RC4.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.stream.RC4_DROP.RC4_DROPState");
		reg += RC4.testState();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.stream.IA");
		reg += IA.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.stream.IA.IAState");
		reg += IA.testState();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.stream.IBAA");
		reg += IBAA.test();
		Log.info.println("Testing claire.util.crypto.cipher.primitive.stream.IBAA.IBAAState");
		reg += IBAA.testState();
		Log.info.println("Testing ../hash");
		Log.info.println("Testing claire.util.crypto.hash.primitive.BLAKE224");
		reg += BLAKE224.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.BLAKE256");
		reg += BLAKE256.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.BLAKE384");
		reg += BLAKE384.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.BLAKE512");
		reg += BLAKE512.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.BMW224");
		reg += BMW224.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.BMW256");
		reg += BMW256.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.BMW384");
		reg += BMW384.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.BMW512");
		reg += BMW512.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.CRC8");
		reg += CRC8.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.CRC16");
		reg += CRC16.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.CRC32");
		reg += CRC32.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.MD2");
		reg += MD2.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.MD4");
		reg += MD4.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.MD5");
		reg += MD5.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.SHA1");
		reg += SHA1.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.SHA2_224");
		reg += SHA2_224.test();
		Log.info.println("Testing claire.util.crypto.hash.primitive.SHA2_256");
		reg += SHA2_256.test();
		Log.info.println("Testing ../math");
		Log.info.println("Testing counters");
		Log.info.println("Testing claire.util.math.counters.IntCounter");
		IntCounter.test();
		Log.info.println("Testing claire.util.math.counters.LongCounter");
		LongCounter.test();
		if(reg > 0)
			Log.crit.println(reg + " regressions detected!");
		else
			Log.info.println("Success! No regressions detected!");
	}

}
