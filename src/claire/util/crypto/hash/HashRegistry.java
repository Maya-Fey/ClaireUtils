package claire.util.crypto.hash;

import claire.util.crypto.hash.primitive.BLAKEFactory;
import claire.util.crypto.hash.primitive.BMWFactory;
import claire.util.crypto.hash.primitive.CRC16;
import claire.util.crypto.hash.primitive.CRC32;
import claire.util.crypto.hash.primitive.CRC8;
import claire.util.crypto.hash.primitive.CubeHash;
import claire.util.crypto.hash.primitive.GrostlFactory;
import claire.util.crypto.hash.primitive.HAVAL;
import claire.util.crypto.hash.primitive.JHFactory;
import claire.util.crypto.hash.primitive.MD2;
import claire.util.crypto.hash.primitive.MD4;
import claire.util.crypto.hash.primitive.MD5;
import claire.util.crypto.hash.primitive.RIPEMDFactory;
import claire.util.crypto.hash.primitive.SHA1;
import claire.util.crypto.hash.primitive.SHA2Factory;
import claire.util.crypto.hash.primitive.Tiger1;
import claire.util.crypto.hash.primitive.Tiger2;
import claire.util.crypto.hash.primitive.Whirlpool;
import claire.util.crypto.hash.primitive.Whirlpool_0;
import claire.util.crypto.hash.primitive.Whirlpool_T;
import claire.util.memory.array.DynamicIndexedRegistry;

public final class HashRegistry {
	
	private HashRegistry() {}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final DynamicIndexedRegistry<HashFactory<?>> reg = (DynamicIndexedRegistry<HashFactory<?>>) new DynamicIndexedRegistry(HashFactory.class, 10);
	
	public static void addHash(String s, HashFactory<?> fac)
	{
		reg.add(fac, s);
	}
	
	public static HashFactory<?> getHash(String s)
	{
		return reg.getByName(s);
	}
	
	static {
		addHash("BLAKE", BLAKEFactory.instance);
		addHash("BMW", BMWFactory.instance);
		addHash("CRC8", CRC8.factory);
		addHash("CRC16", CRC16.factory);
		addHash("CRC32", CRC32.factory);
		addHash("CubeHash", CubeHash.factory);
		addHash("Grostl", GrostlFactory.instance);
		addHash("HAVAL", HAVAL.factory);
		addHash("JH", JHFactory.instance);
		addHash("MD2", MD2.factory);
		addHash("MD4", MD4.factory);
		addHash("MD5", MD5.factory);
		addHash("RIPEMD", RIPEMDFactory.instance);
		addHash("SHA1", SHA1.factory);
		addHash("SHA2", SHA2Factory.instance);
		addHash("Tiger1", Tiger1.factory);
		addHash("Tiger2", Tiger2.factory);
		addHash("Whirlpool", Whirlpool.factory);
		addHash("Whirlpool0", Whirlpool_0.factory);
		addHash("WhirlpoolT", Whirlpool_T.factory);
	}

}
