package claire.util.test.crypto;

import claire.util.crypto.cipher.CBC_Cipher;
import claire.util.crypto.cipher.CFB_Cipher;
import claire.util.crypto.cipher.ECB_Cipher;
import claire.util.crypto.cipher.OFB_Cipher;
import claire.util.crypto.cipher.PCBC_Cipher;
import claire.util.crypto.cipher.key.KeyRC6;
import claire.util.crypto.cipher.primitive.RC6;
import claire.util.crypto.rng.RandUtils;
import claire.util.crypto.rng.primitive.FastXorShift;
import claire.util.encoding.Hex;
import claire.util.logging.Log;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.crypto.ICipherMode;
import claire.util.test.Test;

public final class TestModes {

	public static void testModes()
	{
		RC6 cipher = new RC6();
		cipher.genKey(new FastXorShift(54));
		CBC_Cipher<KeyRC6, RC6> cbc = new CBC_Cipher<KeyRC6, RC6>(cipher);
		Log.info.println("Testing CBC...");
		testMode(cbc);
		ECB_Cipher<KeyRC6, RC6> ecb = new ECB_Cipher<KeyRC6, RC6>(cipher);
		Log.info.println("Testing ECB...");
		testMode(ecb);
		CFB_Cipher<KeyRC6, RC6> cfb = new CFB_Cipher<KeyRC6, RC6>(cipher);
		Log.info.println("Testing CFB...");
		testMode(cfb);
		OFB_Cipher<KeyRC6, RC6> ofb = new OFB_Cipher<KeyRC6, RC6>(cipher);
		Log.info.println("Testing OFB...");
		testMode(ofb);
		PCBC_Cipher<KeyRC6, RC6> pcbc = new PCBC_Cipher<KeyRC6, RC6>(cipher);
		Log.info.println("Testing PCBC...");
		testMode(pcbc);
	}
	
	private static void testMode(ICipherMode<?> mode)
	{
		byte[] bytes = new byte[mode.plaintextSize() * 3];
		RandUtils.fillArr(bytes, new FastXorShift(54));
		byte[] cipher = ArrayUtil.copy(bytes);
		mode.encryptBlock(cipher, 0);
		mode.encryptBlock(cipher, mode.plaintextSize());	
		mode.encryptBlock(cipher, mode.plaintextSize() * 2);
		mode.decryptBlock(cipher, 0);
		mode.decryptBlock(cipher, mode.plaintextSize());	
		mode.decryptBlock(cipher, mode.plaintextSize() * 2);
		if(!ArrayUtil.equals(bytes, cipher)) {
			System.out.println(Hex.toHex(bytes));
			System.out.println(Hex.toHex(cipher));
			Test.reportError("Encryption followed by decryption did not yield the same value.");
		}
		mode.reset();
		mode.encryptBlock(cipher, 0);
		mode.encryptBlock(cipher, mode.plaintextSize());	
		mode.encryptBlock(cipher, mode.plaintextSize() * 2);
		mode.reset();
		byte[] temp = ArrayUtil.copy(cipher);
		mode.encryptBlock(bytes, 0, cipher, 0);
		mode.encryptBlock(bytes, mode.plaintextSize(), cipher, mode.plaintextSize());
		mode.encryptBlock(bytes, mode.plaintextSize() * 2, cipher, mode.plaintextSize() * 2);
		if(!ArrayUtil.equals(cipher, temp)) {
			System.out.println(Hex.toHex(cipher));
			System.out.println(Hex.toHex(temp));
			Test.reportError("Offsetted Encryption is not consistent with no offset.");
		}
		mode.decryptBlock(cipher, 0, temp, 0);
		mode.decryptBlock(cipher, mode.plaintextSize(), temp, mode.plaintextSize());
		mode.decryptBlock(cipher, mode.plaintextSize() * 2, temp, mode.plaintextSize() * 2);
		
		if(!ArrayUtil.equals(bytes, temp)) {
			System.out.println(Hex.toHex(bytes));
			System.out.println(Hex.toHex(temp));
			Test.reportError("Offsetted Encryption followed by offsetted decryption did not yield the same value.");
		}
		
	}
}
