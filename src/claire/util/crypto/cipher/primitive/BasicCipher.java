package claire.util.crypto.cipher.primitive;

import claire.util.crypto.cipher.StateSpaceRequiredException;
import claire.util.standards.IRandom;
import claire.util.standards.crypto.ISymmetric;
import claire.util.standards.crypto.ISymmetricKey;

public abstract class BasicCipher<Type extends ISymmetricKey<Type>> 
				implements ISymmetric<Type> {

	public void encryptBlock(byte[] block, int start) 
	{
		try {
			this.getKey().encryptBlock(block, start);
		} catch (StateSpaceRequiredException e) {
			System.err.println("Error: Compile-time issue not caught by regression tests.");
			throw new java.lang.InternalError();
			//Seriously, this shouldn't happen
		}
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1) 
	{
		try {
			this.getKey().encryptBlock(block, start0, out, start1);
		} catch (StateSpaceRequiredException e) {
			System.err.println("Error: Compile-time issue not caught by regression tests.");
			throw new java.lang.InternalError();
			//Seriously, this shouldn't happen
		}
	}

	public void decryptBlock(byte[] block, int start) 
	{
		try {
			this.getKey().decryptBlock(block, start);
		} catch (StateSpaceRequiredException e) {
			System.err.println("Error: Compile-time issue not caught by regression tests.");
			throw new java.lang.InternalError();
			//Seriously, this shouldn't happen
		}
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1) 
	{
		try {
			this.getKey().decryptBlock(block, start0, out, start1);
		} catch (StateSpaceRequiredException e) {
			System.err.println("Error: Compile-time issue not caught by regression tests.");
			throw new java.lang.InternalError();
			//Seriously, this shouldn't happen
		}
	}

	public void erase()
	{
		this.getKey().erase();
	}
	
	public void genKey(IRandom rand)
	{
		this.setKey(this.newKey(rand));
	}

}
