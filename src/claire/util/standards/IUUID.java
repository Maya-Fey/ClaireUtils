package claire.util.standards;

public interface IUUID<Type extends IUUID<Type>> {
	
	/** Used internally by IUUID. Returns a namespace to compare objects */
	int NAMESPACE();
	
	/** Used internally by IUUID, when two objects share a namespace they are casted and
	 * 	this method is called. */
	boolean sameAs(Type obj);
	
	/** IUUID equals. Only returns true if the objects are actually equal, regardless of type
	 * 	or memory position dependent on how the class implements sameAs(Type) */
	// Warranted, namepsace is checked before runtime cast
	@SuppressWarnings("unchecked")
	default boolean equals(IUUID<?> u) 
	{
		if(this.NAMESPACE() == u.NAMESPACE())
		{
			return this.sameAs((Type) u);
		}
		return false;
	}

}
