package claire.util.standards;

import claire.util.crypto.hash.primitive.CRC16;
import claire.util.crypto.hash.primitive.CRC32;
import claire.util.crypto.hash.primitive.CRC8;

public interface ISoftID {
	
	byte getSID8(CRC8 crc);
	short getSID16(CRC16 crc);
	int getSID32(CRC32 crc);
	
	default byte getSID8()
	{
		return getSID8(new CRC8());
	}
	
	default short getSID16()
	{
		return getSID16(new CRC16());
	}
	
	default int getSID32()
	{
		return getSID32(new CRC32());
	}
	
	default boolean equalsSoftID(ISoftID obj, int level)
	{
		switch(level)
		{
			case 1:
				return this.getSID8() == obj.getSID8();
			case 2:
				return this.getSID16() == obj.getSID16();
			case 3:
				return this.getSID32() == obj.getSID32();
			default:
				return false;
		}
	}
	
}
