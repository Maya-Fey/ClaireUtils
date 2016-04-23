package claire.util.math;

import claire.util.standards.IInteger;

public class PrimitiveRootGenerator<Int extends IInteger<Int>> {

	private final Int t1;
	private final Int t2;
	
	private Int t3;
	private Int prime;
	
	public PrimitiveRootGenerator(Int sample) 
	{
		this.t1 = sample.createDeepClone();
		this.t2 = sample.createDeepClone();
	}
	
	public void setPrime(Int prime)
	{
		this.prime = prime;
		t1.setTo(prime);
		t1.decrement();
		t1.p_divide(2);
	}
	
	public Int nextRoot(int start)
	{
		while(true)
		{
			t2.setTo(start);
			MathHelper.p_modular_exponent(t2, t1, prime);
			if(t2.equals(1)) {
				start++;
				continue;
			}
			t2.setTo(start);
			MathHelper.p_modular_exponent(t2, 2, prime);
			if(t2.equals(1)) {
				start++;
				continue;
			}
			break;
		}
		t2.setTo(start);
		return t2.createDeepClone();
	}
	
	public Int getRoot(Int strongPrime)
	{
		t1.setTo(strongPrime);
		t1.decrement();
		t1.p_divide(2);
		int start = 2;
		while(true)
		{
			t2.setTo(start);
			MathHelper.p_modular_exponent(t2, t1, strongPrime);
			if(t2.equals(1)) {
				start++;
				continue;
			}
			t2.setTo(start);
			MathHelper.p_modular_exponent(t2, 2, strongPrime);
			if(t2.equals(1)) {
				start++;
				continue;
			}
			break;
		}
		t2.setTo(start);
		return t2.createDeepClone();
	}
	
	public Int getRoot(Int strongPrime, int start)
	{
		t1.setTo(strongPrime);
		t1.decrement();
		t1.p_divide(2);
		while(true)
		{
			t2.setTo(start);
			MathHelper.p_modular_exponent(t2, t1, strongPrime);
			if(t2.equals(1)) {
				start++;
				continue;
			}
			t2.setTo(start);
			MathHelper.p_modular_exponent(t2, 2, strongPrime);
			if(t2.equals(1)) {
				start++;
				continue;
			}
			break;
		}
		t2.setTo(start);
		return t2.createDeepClone();
	}
	
	public Int getRoot(Int strongPrime, Int start)
	{
		if(t3 == null)
			t3 = start.createDeepClone();
		else
			t3.setTo(start);
		t1.setTo(strongPrime);
		t1.decrement();
		t1.p_divide(2);
		while(true)
		{
			t2.setTo(t3);
			MathHelper.p_modular_exponent(t2, t1, strongPrime);
			if(t2.equals(1)) {
				t3.increment();
				continue;
			}
			t2.setTo(start);
			MathHelper.p_modular_exponent(t2, 2, strongPrime);
			if(t2.equals(1)) {
				t3.increment();
				continue;
			}
			break;
		}
		t2.setTo(start);
		return t2.createDeepClone();
	}

}
