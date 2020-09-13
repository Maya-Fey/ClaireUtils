package claire.util.crypto.cipher.primitive;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import claire.util.crypto.cipher.primitive.block.AES;
import claire.util.crypto.cipher.primitive.block.ARIA;
import claire.util.crypto.cipher.primitive.block.Blowfish;
import claire.util.crypto.cipher.primitive.block.CAST5;
import claire.util.crypto.cipher.primitive.block.CAST6;
import claire.util.crypto.cipher.primitive.block.Camellia;
import claire.util.crypto.cipher.primitive.block.DES;
import claire.util.crypto.cipher.primitive.block.DESX;
import claire.util.crypto.cipher.primitive.block.FEAL;
import claire.util.crypto.cipher.primitive.block.GOST;
import claire.util.crypto.cipher.primitive.block.IDEA;
import claire.util.crypto.cipher.primitive.block.MARS;
import claire.util.crypto.cipher.primitive.block.RC2;
import claire.util.crypto.cipher.primitive.block.RC5_16;
import claire.util.crypto.cipher.primitive.block.RC5_32;
import claire.util.crypto.cipher.primitive.block.RC5_64;
import claire.util.crypto.cipher.primitive.block.RC6;
import claire.util.crypto.cipher.primitive.block.SEED;
import claire.util.crypto.cipher.primitive.block.Simon16;
import claire.util.crypto.cipher.primitive.block.Simon32;
import claire.util.crypto.cipher.primitive.block.Simon64;
import claire.util.crypto.cipher.primitive.block.Skipjack;
import claire.util.crypto.cipher.primitive.block.Speck16;
import claire.util.crypto.cipher.primitive.block.Speck32;
import claire.util.crypto.cipher.primitive.block.Speck64;
import claire.util.crypto.cipher.primitive.block.TDEA;
import claire.util.crypto.cipher.primitive.block.TEA;
import claire.util.crypto.cipher.primitive.block.XTEA;
import claire.util.crypto.cipher.primitive.block.XXTEA;
import claire.util.crypto.cipher.primitive.stream.IA;
import claire.util.crypto.cipher.primitive.stream.IBAA;
import claire.util.crypto.cipher.primitive.stream.RC4;
import claire.util.crypto.cipher.primitive.stream.RC4_DROP;
import claire.util.crypto.cipher.primitive.stream.Salsa;
import claire.util.logging.Log;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.crypto.IStreamCipher;
import claire.util.standards.crypto.ISymmetric;

public class CipherRegistry {
	
	private static final Class<?>[] t0 = new Class[0];
	private static final Object[] a0 = new Object[0];
	
	@SuppressWarnings("unchecked")
	private static Constructor<? extends ISymmetric<?>>[] blockCons = new Constructor[10];
	private static String[] blockNames = new String[10];
	private static int blockPos = 0;
	
	public static void addBlockCipher(String name, Class<? extends ISymmetric<?>> cl)
	{
		if(blockPos == blockNames.length) {
			blockCons = ArrayUtil.upsize(blockCons, 10);
			blockNames = ArrayUtil.upsize(blockNames, 10);
		}
		try {
			blockCons[blockPos  ] = cl.getConstructor(t0);
		} catch (NoSuchMethodException e) {
			Log.warn.println("Warning! Cipher of given name " + name + " failed to add due to invalid constructor signature.");
			Log.debug.printStackTrace(e.getStackTrace());
			e.printStackTrace();
			return;
		} catch (SecurityException e) {
			Log.warn.println("Warning! Cipher of given name " + name + " failed to add due to security constraints.");
			Log.debug.printStackTrace(e.getStackTrace());
			return;
		}
		blockNames[blockPos++] = name;
	}
	
	public static ISymmetric<?> constructBlockCipher(String name) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		for(int i = 0; i < blockPos; i++) 
			if(blockNames[i].equals(name)) 
				return blockCons[i].newInstance(a0);
		return null;
	}
	
	public static String[] getBlockNames()
	{
		return blockNames;
	}
	
	public static int getBlockCiphers()
	{
		return blockPos;
	}
	
	@SuppressWarnings("unchecked")
	private static Constructor<? extends IStreamCipher<?, ?>>[] streamCons = new Constructor[10];
	private static String[] streamNames = new String[10];
	private static int streamPos = 0;
	
	public static void addStreamCipher(String name, Class<? extends IStreamCipher<?, ?>> cl)
	{
		if(streamPos == streamNames.length) {
			streamCons = ArrayUtil.upsize(streamCons, 10);
			streamNames = ArrayUtil.upsize(streamNames, 10);
		}
		try {
			streamCons[streamPos  ] = cl.getConstructor(t0);
		} catch (NoSuchMethodException e) {
			Log.warn.println("Warning! Cipher of given name " + name + " failed to add due to invalid constructor signature.");
			Log.debug.printStackTrace(e.getStackTrace());
			e.printStackTrace();
			return;
		} catch (SecurityException e) {
			Log.warn.println("Warning! Cipher of given name " + name + " failed to add due to security constraints.");
			Log.debug.printStackTrace(e.getStackTrace());
			return;
		}
		streamNames[streamPos++] = name;
	}
	
	public static IStreamCipher<?, ?> constructStreamCipher(String name) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		for(int i = 0; i < streamPos; i++) 
			if(streamNames[i].equals(name)) 
				return streamCons[i].newInstance(a0);
		return null;
	}
	
	public static String[] getStreamNames()
	{
		return streamNames;
	}
	
	public static int getStreamCiphers()
	{
		return streamPos;
	}
	
	static {
		addBlockCipher("AES", AES.class);
		addBlockCipher("ARIA", ARIA.class);
		addBlockCipher("Blowfish", Blowfish.class);
		addBlockCipher("Camellia", Camellia.class);
		addBlockCipher("CAST5", CAST5.class);
		addBlockCipher("CAST6", CAST6.class);
		addBlockCipher("DES", DES.class);
		addBlockCipher("DESX", DESX.class);
		addBlockCipher("FEAL", FEAL.class);
		addBlockCipher("GOST", GOST.class);
		addBlockCipher("IDEA", IDEA.class);
		addBlockCipher("MARS", MARS.class);
		addBlockCipher("RC2", RC2.class);
		addBlockCipher("RC5!16", RC5_16.class);
		addBlockCipher("RC5!32", RC5_32.class);
		addBlockCipher("RC5", RC5_32.class);
		addBlockCipher("RC5!64", RC5_64.class);
		addBlockCipher("RC6", RC6.class);
		addBlockCipher("SEED", SEED.class);
		addBlockCipher("Simon16", Simon16.class);
		addBlockCipher("Simon32", Simon32.class);
		addBlockCipher("Simon64", Simon64.class);
		addBlockCipher("Skipjack", Skipjack.class);
		addBlockCipher("Speck16", Speck16.class);
		addBlockCipher("Speck32", Speck32.class);
		addBlockCipher("Speck64", Speck64.class);
		addBlockCipher("TDEA", TDEA.class);
		addBlockCipher("TEA", TEA.class);
		addBlockCipher("XTEA", XTEA.class);
		addBlockCipher("XXTEA", XXTEA.class);
		
		addStreamCipher("IA", IA.class);
		addStreamCipher("IBAA", IBAA.class);
		addStreamCipher("RC4", RC4.class);
		addStreamCipher("RC4DROP", RC4_DROP.class);
		addStreamCipher("Salsa", Salsa.class);
	}

}
