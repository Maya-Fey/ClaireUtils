package claire.util.test;

import claire.util.encoding.CString;
import claire.util.logging.Log;
import claire.util.test.core.TestBits;
import claire.util.test.crypto.TestCipher;
import claire.util.test.crypto.TestHash;
import claire.util.test.crypto.TestModes;
import claire.util.test.crypto.TestPublic;
import claire.util.test.encoding.Encodings;
import claire.util.test.io.PersistableTest;
import claire.util.test.math.TestMath;
import claire.util.test.memory.buffer.TestStreamPairs;

public class Test {
	
	public static boolean PASSED = true;
	
	public static final void reportError(CString error)
	{
		PASSED = false;
		Log.err.println(error);
	}
	
	public static final void reportError(String error)
	{
		PASSED = false;
		Log.err.println(error);
	}
	
	public static final void runTests() throws Exception
	{
		Log.info.println("Testing core functionality...");
		TestBits.runTests();
		Log.info.println("Testing math...");
		TestMath.testMath();
		Log.info.println("Testing I/O");
		Log.info.println("Testing streams...");
		TestStreamPairs.runTests();
		Log.info.println("Testing persistability...");
		PersistableTest.testPersistables();
		Log.info.println("Testing crypto suite...");
		Log.info.println("Testing hash functions...");
		TestHash.runTests();
		Log.info.println("Testing symmetric ciphers...");
		TestCipher.runTests();
		Log.info.println("Testing public-key cryptosystems");
		TestPublic.testSystems();
		Log.info.println("Testing cipher modes");
		TestModes.testModes();
		Log.info.println("Testing encodings...");
		Encodings.runTests();
		if(PASSED)
			Log.info.println("Tests passed without error!");
		else
			Log.crit.println("Tests completed with errors.");
	}

}
