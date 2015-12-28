package claire.util.test.io;

import java.io.File;

import claire.util.concurrency.Timer;
import claire.util.crypto.publickey.rsa.RSAFastLargeKeyPair;
import claire.util.crypto.publickey.rsa.RSAFastPrivateKey;
import claire.util.crypto.publickey.rsa.RSAFastStandardKeyPair;
import claire.util.crypto.publickey.rsa.RSALargeKeyPair;
import claire.util.crypto.publickey.rsa.RSAMicroPublicKey;
import claire.util.crypto.publickey.rsa.RSAStandardKeyPair;
import claire.util.crypto.publickey.rsa.RSAStandardPrivateKey;
import claire.util.crypto.publickey.rsa.RSAStandardPublicKey;
import claire.util.crypto.rng.RandUtils;
import claire.util.encoding.CString;
import claire.util.io.Factory;
import claire.util.io.FileIncomingStream;
import claire.util.io.FileOutgoingStream;
import claire.util.io.PositionIndex;
import claire.util.logging.Log;
import claire.util.math.UInt;
import claire.util.standards.IPersistable;
import claire.util.standards.IUUID;
import claire.util.test.Test;

public class PersistableTest {
	
	private static final File temp = new File("persist.tmp");
	
	public static final void testPersistables() throws Exception
	{
		temp.createNewFile();
		Log.info.println("Testing CString...");
		CString str = new CString("abcdefghijklmnopqrstuv一般的な領域でのスパイが大量にあります。我已經發現在事物量局部增加");
		testPersistable(str);
		Log.info.println("Testing UInt...");
		UInt uint = new UInt("3412748901274597582390572390478589234758973489573489753489738975348975348975893475893478957348957348975893475689347", 128);
		testPersistable(uint);
		Log.info.println("Testing PositionIndex...");
		int[] ints = new int[400];
		RandUtils.fillArr(ints);
		PositionIndex pos = new PositionIndex(ints, 400, 311);	
		testPersistable(pos);
		Log.info.println("Testing RSA Standard Public Key...");
		RSAStandardPublicKey rsapub = new RSAStandardPublicKey(new UInt("213414732947892342", 32), new UInt("1234235", 24), 32);
		testPersistable(rsapub);
		Log.info.println("Testing RSA Micro Public Key...");
		RSAMicroPublicKey rsapub2 = new RSAMicroPublicKey(new UInt("213414328239048902382", 32), 17, 32);
		testPersistable(rsapub2);
		Log.info.println("Testing RSA Standard Private Key...");
		RSAStandardPrivateKey rsapri = new RSAStandardPrivateKey(new UInt("155413", 32), new UInt("1345234534532534", 24), 32);
		testPersistable(rsapri);
		Log.info.println("Testing RSA Large Key Pair...");
		RSALargeKeyPair rsapair = new RSALargeKeyPair(rsapub, rsapri);
		testPersistable(rsapair);
		Log.info.println("Testing RSA Standard Key Pair...");
		RSAStandardKeyPair rsapair2 = new RSAStandardKeyPair(rsapub2, rsapri);
		testPersistable(rsapair2);
		Log.info.println("Testing RSA Fast Private Key");
		UInt[] mul = new UInt[4];
		UInt[] inv = new UInt[4];
		UInt[] exp = new UInt[5];
		UInt[] pri = new UInt[5];
		for(int i = 0; i < 4; i++) {
			int[] i1 = new int[8];
			int[] i2 = new int[8];
			int[] i3 = new int[8];
			int[] i4 = new int[8];
			RandUtils.fillArr(i1);
			RandUtils.fillArr(i2);
			RandUtils.fillArr(i3);
			RandUtils.fillArr(i4);
			mul[i] = new UInt(i1);
			inv[i] = new UInt(i2);
			exp[i] = new UInt(i3);
			pri[i] = new UInt(i4);
		}
		int[] i1 = new int[8];
		int[] i2 = new int[8];
		RandUtils.fillArr(i1);
		RandUtils.fillArr(i2);
		exp[4] = new UInt(i1);
		pri[4] = new UInt(i2);
		RSAFastPrivateKey key = new RSAFastPrivateKey(mul, inv, exp, pri, new UInt("342345234", 8), 8);
		testPersistable(key);
		Log.info.println("Testing RSA Fast Large Key Pair");
		RSAFastLargeKeyPair pair3 = new RSAFastLargeKeyPair(rsapub, key);
		testPersistable(pair3);
		Log.info.println("Testing RSA Fast Standard Key Pair");
		RSAFastStandardKeyPair pair4 = new RSAFastStandardKeyPair(rsapub2, key);
		testPersistable(pair4);
		Log.info.println("Testing timer...");
		Timer timer = new Timer();
		testPersistable(timer);
		temp.delete();
	}
	
	private static <Type extends IPersistable<Type> & IUUID<Type>> void testPersistable(Type obj) throws Exception
	{
		Factory<Type> factory = obj.factory();
		byte[] bytes = obj.export();
		int slen = obj.exportSize();
		Type test = factory.resurrect(bytes);
		if(slen != test.exportSize()) {
			Test.reportError("Export size inconsistent before and after resurrection.");
		}
		if(!test.equals(obj))
			throw new java.lang.AssertionError("Persisting and resurrecting as raw bytes did not yield the original object.");
		bytes = new byte[bytes.length + 32];
		obj.export(bytes, 32);
		test = factory.resurrect(bytes, 32);
		//System.out.println(test);
		if(!test.equals(obj))
			throw new java.lang.AssertionError("Persisting and resurrecting as bytes with offset did not yield the original object.");
		FileOutgoingStream fos = new FileOutgoingStream(temp);
		obj.export(fos);
		fos.close();
		FileIncomingStream fis = new FileIncomingStream(temp);
		test = factory.resurrect(fis);
		if(!test.equals(obj))
			throw new java.lang.AssertionError("Persisting and resurrecting as strea did not yield the original object.");
		fis.close();
	}

}
