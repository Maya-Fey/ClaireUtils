package claire.util.crypto.publickey.rsa;

import java.io.IOException;
import java.math.BigInteger;

import claire.util.io.Factory;
import claire.util.math.MathHelper;
import claire.util.math.UInt;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class RSAFastPrivateKey 
	   extends RSAPrivateKey<RSAFastPrivateKey> {
	
	private UInt[] scratch;
	
	private final UInt mod;
	private final UInt[] mul;
	private final UInt[] inv;
	private final UInt[] exp;
	private final UInt[] primes;
	
	private int len;
	private int plen;
	
	public RSAFastPrivateKey(UInt mod, UInt exp, UInt[] primes, int len)
	{
		this.plen = primes.length;
		this.primes = primes;
		UInt[] mula = new UInt[plen - 1];
		UInt[] inva = new UInt[plen - 1];
		UInt[] expa = new UInt[plen];
		int rlen = plen - 1;
		mula[0] = primes[0].createDeepClone();
		mula[0].upsize(mod.getIntLen());
		for(int i = 1; i < rlen; i++)
			mula[i] = mula[i - 1].multiply(primes[i]);
		for(int i = 0; i < rlen; i++)
			inva[i] = MathHelper.u_modular_inverse(mula[i], primes[i + 1]);
		for(UInt u : primes)
			u.decrement();
		for(int i = 0; i < plen; i++)
			expa[i] = exp.modulo(primes[i]);
		for(UInt u : primes)
			u.increment();
		for(UInt u : primes)
			System.out.print(u.toCString().toString() + " : ");
		System.out.println();
		for(UInt u : mula)
			System.out.print(u.toCString().toString() + " : ");
		System.out.println();
		for(UInt u : inva)
			System.out.print(u.toCString().toString() + " : ");
		System.out.println();
		for(UInt u : expa)
			System.out.print(u.toCString().toString() + " : ");
		System.out.println();
		this.mod = mod;
		this.len = len;
		this.mul = mula;
		this.inv = inva;
		this.exp = expa;
	}
	
	public RSAFastPrivateKey(UInt[] mul, UInt[] inv, UInt[] exp, UInt[] primes, UInt mod, int len)
	{
		this.mod = mod;
		this.mul = mul;
		this.inv = inv;
		this.exp = exp;
		this.len = len;
		this.primes = primes;
		this.plen = exp.length;
	}
	
	public RSAFastPrivateKey createDeepClone()
	{
		return new RSAFastPrivateKey(ArrayUtil.deepCopy(mul), 
									 ArrayUtil.deepCopy(inv),
									 ArrayUtil.deepCopy(exp),
									 ArrayUtil.deepCopy(primes),
									 mod.createDeepClone(),
									 len);
	}
	
	private void alloc()
	{
		if(scratch == null)
		{
			scratch = new UInt[plen];
			for(int i = 0; i < plen; i++) 
				scratch[i] = mod.createDeepClone();			
		}
	}

	public static BigInteger bigint(UInt u)
	{
		return new BigInteger(new String(u.toChars()));
	}
	
	public void crypt(UInt msg)
	{
		alloc();
		for(int i = 0; i < plen; i++) {
			scratch[i].setTo(msg);
			MathHelper.p_modular_exponent_sure(scratch[i], exp[i], primes[i]);
		}
		for(int i = 1; i < plen; i++) {
			if(scratch[0].isGreaterThan(scratch[i])) {
				msg.setTo(scratch[0]);
				msg.p_subtract(scratch[i]);
				msg.p_multiply(inv[i - 1]);
				msg.p_modulo(primes[i]);
				if(msg.isNonZero()) {
					scratch[i].setTo(primes[i]);
					scratch[i].p_subtract(msg);
				} else
					scratch[i].zero();
				scratch[i].p_multiply(mul[i - 1]);
				scratch[0].p_add(scratch[i]);
			} else {
				scratch[i].p_subtract(scratch[0]);
				scratch[i].p_multiply(inv[i - 1]);
				scratch[i].p_modulo(primes[i]);
				scratch[i].p_multiply(mul[i - 1]);
				scratch[0].p_add(scratch[i]);
			}
		}
		msg.setTo(scratch[0]);		
	}
	
	protected int getLen()
	{
		return this.len;
	}
	
	protected int modLen()
	{
		return this.mod.getIntLen();
	}
	
	public int messageSize()
	{
		return this.len << 2;
	}

	public int signatureSize()
	{
		return mod.getIntLen() << 2;
	}

	public int plaintextSize()
	{
		return this.len << 2;
	}

	public int ciphertextSize()
	{
		return mod.getIntLen() << 2;
	}

	public void erase()
	{
		mod.zero();
		for(UInt u : mul)
			u.zero();
		for(UInt u : inv)
			u.zero();
		for(UInt u : exp)
			u.zero();
		len = 0;
		plen = 0;
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.RSAFASTPRIV;
	}

	public boolean sameAs(RSAFastPrivateKey obj)
	{
		if(len == obj.len && plen == obj.plen) 
			if(mod.equals(obj.mod))
			{
				for(int i = 0; i < plen - 1; i++)
					if(obj.mul[i].doesNotEqual(mul[i]))
						return false;
				for(int i = 0; i < plen - 1; i++)
					if(obj.inv[i].doesNotEqual(inv[i]))
						return false;
				for(int i = 0; i < plen; i++)
					if(obj.exp[i].doesNotEqual(exp[i]))
						return false;
				return true;
			}
		return false;
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeInt(len);
		stream.writeInt(plen);
		stream.persist(mod);
		for(UInt u : mul)
			stream.persist(u);
		for(UInt u : inv)
			stream.persist(u);
		for(UInt u : exp)
			stream.persist(u);
		for(UInt u : primes)
			stream.persist(u);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.intToBytes(len, bytes, offset);
		Bits.intToBytes(plen, bytes, offset += 4);
		mod.export(bytes, offset += 4);
		offset += mod.exportSize();
		for(UInt u : mul) {
			u.export(bytes, offset);
			offset += u.exportSize();
		}
		for(UInt u : inv) {
			u.export(bytes, offset);
			offset += u.exportSize();
		}
		for(UInt u : exp) {
			u.export(bytes, offset);
			offset += u.exportSize();
		}
		for(UInt u : primes) {
			u.export(bytes, offset);
			offset += u.exportSize();
		}
	}

	public int exportSize()
	{		
		return 8 + mod.exportSize() + (exp.length * exp[0].exportSize()) + (mul.length * mul[0].exportSize()) + (inv.length * inv[0].exportSize()) + (primes.length * primes[0].exportSize());
	}

	public Factory<RSAFastPrivateKey> factory()
	{
		return factory;
	}
	
	public static final RSAPrivateKeyFactory factory = new RSAPrivateKeyFactory();

	private static final class RSAPrivateKeyFactory 
						 extends Factory<RSAFastPrivateKey> {

		private RSAPrivateKeyFactory() 
		{
			super(RSAFastPrivateKey.class);
		}

		public RSAFastPrivateKey resurrect(byte[] data, int start) throws InstantiationException
		{
			int len = Bits.intFromBytes(data, start);
			int plen = Bits.intFromBytes(data, start += 4);
			UInt mod = UInt.factory.resurrect(data, start += 4);
			start += mod.exportSize();
			UInt[] mul = new UInt[plen - 1];
			UInt[] inv = new UInt[plen - 1];
			UInt[] exp = new UInt[plen];
			UInt[] primes = new UInt[plen];
			for(int i = 0; i < mul.length; i++) {
				mul[i] = UInt.factory.resurrect(data, start);
				start += mul[i].exportSize();
			}
			for(int i = 0; i < inv.length; i++) {
				inv[i] = UInt.factory.resurrect(data, start);
				start += inv[i].exportSize();
			}
			for(int i = 0; i < exp.length; i++) {
				exp[i] = UInt.factory.resurrect(data, start);
				start += exp[i].exportSize();
			}
			for(int i = 0; i < primes.length; i++) {
				primes[i] = UInt.factory.resurrect(data, start);
				start += primes[i].exportSize();
			}
			return new RSAFastPrivateKey(mul, inv, exp, primes, mod, len);
		}

		public RSAFastPrivateKey resurrect(IIncomingStream stream)throws InstantiationException, IOException
		{
			int len = stream.readInt();
			int plen = stream.readInt();
			UInt[] mul = new UInt[plen - 1];
			UInt[] inv = new UInt[plen - 1];
			UInt[] exp = new UInt[plen];
			UInt[] primes = new UInt[plen];
			UInt mod = stream.resurrect(UInt.factory);
			for(int i = 0; i < mul.length; i++)
				mul[i] = stream.resurrect(UInt.factory);
			for(int i = 0; i < inv.length; i++)
				inv[i] = stream.resurrect(UInt.factory);
			for(int i = 0; i < exp.length; i++)
				exp[i] = stream.resurrect(UInt.factory);
			for(int i = 0; i < primes.length; i++)
				primes[i] = stream.resurrect(UInt.factory);
			return new RSAFastPrivateKey(mul, inv, exp, primes, mod, len);
		}
		
		
	}

}