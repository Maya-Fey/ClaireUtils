package claire.util.crypto.secret.kdf;

import claire.util.memory.array.DynamicIndexedRegistry;

public final class KDFRegistry {
	
	private KDFRegistry() {}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final DynamicIndexedRegistry<KDFFactory<?>> reg = (DynamicIndexedRegistry<KDFFactory<?>>) new DynamicIndexedRegistry(KDFFactory.class, 10);
	
	public static void addKDF(String s, KDFFactory<?> fac)
	{
		reg.add(fac, s);
	}
	
	public static KDFFactory<?> getKDF(String s)
	{
		return reg.getByName(s);
	}
	
	static {
		addKDF("HBKDF", HBKDF.factory);
		addKDF("PBKDF2", PBKDF2.factory);
	}

}
