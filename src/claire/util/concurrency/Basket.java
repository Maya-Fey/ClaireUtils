package claire.util.concurrency;

public class Basket<Type>
{
	
	private final Type[] basket;
	private int pos = 0;
	
	public Basket(Type[] bask)
	{
		this.basket = bask;
	}
	
	public void drop(Type t)
	{
		synchronized(this) {
			basket[pos++] = t;
		}
	}
	
}
