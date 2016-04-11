package claire.util.standards;

public interface ISoftID {
	
	byte getSID8();
	short getSID16();
	int getSID32();
	
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
