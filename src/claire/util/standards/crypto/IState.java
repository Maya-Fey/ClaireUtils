package claire.util.standards.crypto;

import claire.util.standards.IPersistable;
import claire.util.standards.IUUID;

public interface IState<Type extends IState<Type>> 
	   extends IPersistable<Type>,
	   		   IUUID<Type> {
	
	void erase();
	
	int stateID();
	
	static int IA = 0;
	static int IBAA = 1;
	static int ISAAC = 2;
	static int RC4_DROP = 3;
	static int RC4 = 4;
	static int CRC8 = 5;
	static int CRC16 = 6;
	static int CRC32 = 7;
	static int CUBEHASH = 8;
	static int HAVAL = 9;
	static int MD2 = 10;
	static int MD4 = 11;
	static int MD5 = 12;
	static int JH = 13;
	static int SHA1 = 14;
	static int SHA2_32 = 15;
	static int SHA2_64 = 16;
	static int TIGER = 17;
	static int WMDIA = 18;
	static int RIPEMD128 = 19;
	static int RIPEMD160 = 20;
	static int RIPEMD256 = 21;
	static int RIPEMD320 = 22;
	static int LONGSTATE = 23;
	static int MERSENNESTATE = 24;
	static int BMW_32 = 25;
	static int BMW_64 = 26;
	static int BLAKE_32 = 27;
	static int BLAKE_64 = 28;
	static int GROSTL_32 = 29;
	static int GROSTL_64 = 30;

}
