package claire.util.crypto.secret.kdf;

public interface IKDF {
	
	void derive(byte[] data, int start0, int len0, byte[] salt, int start1, int len1, byte[] key, int start2, int keylen);

}
