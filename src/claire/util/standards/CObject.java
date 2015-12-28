package claire.util.standards;

public interface CObject<Type extends CObject<Type>> 
	   extends IDeepClonable<Type>,
	   		   IReferrable<Type>,
	   		   IPersistable<Type>,
	   		   IUUID<Type> {
	
}
