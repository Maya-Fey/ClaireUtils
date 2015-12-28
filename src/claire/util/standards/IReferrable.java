package claire.util.standards;

import claire.util.memory.util.Pointer;

public interface IReferrable<T> {
	
	@SuppressWarnings("unchecked")
	default IPointer<T> pointer()
	{
		return new Pointer<T>((T) this);
	}

}
