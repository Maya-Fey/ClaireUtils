package claire.util.crypto.cipher.primitive;

import java.util.Arrays;

import claire.util.crypto.cipher.key.KeyCAST5;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IRandom;
import claire.util.standards.crypto.ISymmetric;

public class CAST5 
	   extends CASTBase 
	   implements ISymmetric<KeyCAST5> {
	
	private int[] KROT = new int[16];
	private int[] KMASK = new int[16];
	
	private int R;

	private KeyCAST5 key;
	
	public KeyCAST5 getKey()
	{
		return this.key;
	}

	public void setKey(KeyCAST5 t)
	{
		this.key = t;
		byte[] raw = t.getBytes();
		if(raw.length > 10)
			R = 16;
		else
			R = 12;
		
		byte[] key = new byte[16];
		byte[] mir = new byte[16];
		
		int zA, zB, zC, zD;
        int xA, xB, xC, xD;

        for(int i = 0; i < raw.length; i++) 
        { 
            key[i] = raw[i]; 
        }

        xA = Bits.intFromBytes(key, 0x0);
        xB = Bits.intFromBytes(key, 0x4);
        xC = Bits.intFromBytes(key, 0x8);
        xD = Bits.intFromBytes(key, 0xC);

        zA = xA ^ SCUBE[4][key[0xD] & 0xFF] ^ SCUBE[5][key[0xF] & 0xFF] ^ SCUBE[6][key[0xC] & 0xFF] ^ SCUBE[7][key[0xE] & 0xFF] ^ SCUBE[6][key[0x8] & 0xFF];
        Bits.intToBytes(zA, mir, 0x0);
        zB = xC ^ SCUBE[4][mir[0x0] & 0xFF] ^ SCUBE[5][mir[0x2] & 0xFF] ^ SCUBE[6][mir[0x1] & 0xFF] ^ SCUBE[7][mir[0x3] & 0xFF] ^ SCUBE[7][key[0xA] & 0xFF];
        Bits.intToBytes(zB, mir, 0x4);
        zC = xD ^ SCUBE[4][mir[0x7] & 0xFF] ^ SCUBE[5][mir[0x6] & 0xFF] ^ SCUBE[6][mir[0x5] & 0xFF] ^ SCUBE[7][mir[0x4] & 0xFF] ^ SCUBE[4][key[0x9] & 0xFF];
        Bits.intToBytes(zC, mir, 0x8);
        zD = xB ^ SCUBE[4][mir[0xA] & 0xFF] ^ SCUBE[5][mir[0x9] & 0xFF] ^ SCUBE[6][mir[0xB] & 0xFF] ^ SCUBE[7][mir[0x8] & 0xFF] ^ SCUBE[5][key[0xB] & 0xFF];
        Bits.intToBytes(zD, mir, 0xC);
        KMASK[ 0] = SCUBE[4][mir[0x8] & 0xFF] ^ SCUBE[5][mir[0x9] & 0xFF] ^ SCUBE[6][mir[0x7] & 0xFF] ^ SCUBE[7][mir[0x6] & 0xFF] ^ SCUBE[4][mir[0x2] & 0xFF];
        KMASK[ 1] = SCUBE[4][mir[0xA] & 0xFF] ^ SCUBE[5][mir[0xB] & 0xFF] ^ SCUBE[6][mir[0x5] & 0xFF] ^ SCUBE[7][mir[0x4] & 0xFF] ^ SCUBE[5][mir[0x6] & 0xFF];
        KMASK[ 2] = SCUBE[4][mir[0xC] & 0xFF] ^ SCUBE[5][mir[0xD] & 0xFF] ^ SCUBE[6][mir[0x3] & 0xFF] ^ SCUBE[7][mir[0x2] & 0xFF] ^ SCUBE[6][mir[0x9] & 0xFF];
        KMASK[ 3] = SCUBE[4][mir[0xE] & 0xFF] ^ SCUBE[5][mir[0xF] & 0xFF] ^ SCUBE[6][mir[0x1] & 0xFF] ^ SCUBE[7][mir[0x0] & 0xFF] ^ SCUBE[7][mir[0xC] & 0xFF];

        zA = Bits.intFromBytes(mir, 0x0);
        zB = Bits.intFromBytes(mir, 0x4);
        zC = Bits.intFromBytes(mir, 0x8);
        zD = Bits.intFromBytes(mir, 0xC);
        xA = zC ^ SCUBE[4][mir[0x5] & 0xFF] ^ SCUBE[5][mir[0x7] & 0xFF] ^ SCUBE[6][mir[0x4] & 0xFF] ^ SCUBE[7][mir[0x6] & 0xFF] ^ SCUBE[6][mir[0x0] & 0xFF];
        Bits.intToBytes(xA, key, 0x0);
        xB = zA ^ SCUBE[4][key[0x0] & 0xFF] ^ SCUBE[5][key[0x2] & 0xFF] ^ SCUBE[6][key[0x1] & 0xFF] ^ SCUBE[7][key[0x3] & 0xFF] ^ SCUBE[7][mir[0x2] & 0xFF];
        Bits.intToBytes(xB, key, 0x4);
        xC = zB ^ SCUBE[4][key[0x7] & 0xFF] ^ SCUBE[5][key[0x6] & 0xFF] ^ SCUBE[6][key[0x5] & 0xFF] ^ SCUBE[7][key[0x4] & 0xFF] ^ SCUBE[4][mir[0x1] & 0xFF];
        Bits.intToBytes(xC, key, 0x8);
        xD = zD ^ SCUBE[4][key[0xA] & 0xFF] ^ SCUBE[5][key[0x9] & 0xFF] ^ SCUBE[6][key[0xB] & 0xFF] ^ SCUBE[7][key[0x8] & 0xFF] ^ SCUBE[5][mir[0x3] & 0xFF];
        Bits.intToBytes(xD, key, 0xC);
        KMASK[ 4] = SCUBE[4][key[0x3] & 0xFF] ^ SCUBE[5][key[0x2] & 0xFF] ^ SCUBE[6][key[0xC] & 0xFF] ^ SCUBE[7][key[0xD] & 0xFF] ^ SCUBE[4][key[0x8] & 0xFF];
        KMASK[ 5] = SCUBE[4][key[0x1] & 0xFF] ^ SCUBE[5][key[0x0] & 0xFF] ^ SCUBE[6][key[0xE] & 0xFF] ^ SCUBE[7][key[0xF] & 0xFF] ^ SCUBE[5][key[0xD] & 0xFF];
        KMASK[ 6] = SCUBE[4][key[0x7] & 0xFF] ^ SCUBE[5][key[0x6] & 0xFF] ^ SCUBE[6][key[0x8] & 0xFF] ^ SCUBE[7][key[0x9] & 0xFF] ^ SCUBE[6][key[0x3] & 0xFF];
        KMASK[ 7] = SCUBE[4][key[0x5] & 0xFF] ^ SCUBE[5][key[0x4] & 0xFF] ^ SCUBE[6][key[0xA] & 0xFF] ^ SCUBE[7][key[0xB] & 0xFF] ^ SCUBE[7][key[0x7] & 0xFF];

        xA = Bits.intFromBytes(key, 0x0);
        xB = Bits.intFromBytes(key, 0x4);
        xC = Bits.intFromBytes(key, 0x8);
        xD = Bits.intFromBytes(key, 0xC);
        zA = xA ^ SCUBE[4][key[0xD] & 0xFF] ^ SCUBE[5][key[0xF] & 0xFF] ^ SCUBE[6][key[0xC] & 0xFF] ^ SCUBE[7][key[0xE] & 0xFF] ^ SCUBE[6][key[0x8] & 0xFF];
        Bits.intToBytes(zA, mir, 0x0);
        zB = xC ^ SCUBE[4][mir[0x0] & 0xFF] ^ SCUBE[5][mir[0x2] & 0xFF] ^ SCUBE[6][mir[0x1] & 0xFF] ^ SCUBE[7][mir[0x3] & 0xFF] ^ SCUBE[7][key[0xA] & 0xFF];
        Bits.intToBytes(zB, mir, 0x4);
        zC = xD ^ SCUBE[4][mir[0x7] & 0xFF] ^ SCUBE[5][mir[0x6] & 0xFF] ^ SCUBE[6][mir[0x5] & 0xFF] ^ SCUBE[7][mir[0x4] & 0xFF] ^ SCUBE[4][key[0x9] & 0xFF];
        Bits.intToBytes(zC, mir, 0x8);
        zD = xB ^ SCUBE[4][mir[0xA] & 0xFF] ^ SCUBE[5][mir[0x9] & 0xFF] ^ SCUBE[6][mir[0xB] & 0xFF] ^ SCUBE[7][mir[0x8] & 0xFF] ^ SCUBE[5][key[0xB] & 0xFF];
        Bits.intToBytes(zD, mir, 0xC);
        KMASK[ 8] = SCUBE[4][mir[0x3] & 0xFF] ^ SCUBE[5][mir[0x2] & 0xFF] ^ SCUBE[6][mir[0xC] & 0xFF] ^ SCUBE[7][mir[0xD] & 0xFF] ^ SCUBE[4][mir[0x9] & 0xFF];
        KMASK[ 9] = SCUBE[4][mir[0x1] & 0xFF] ^ SCUBE[5][mir[0x0] & 0xFF] ^ SCUBE[6][mir[0xE] & 0xFF] ^ SCUBE[7][mir[0xF] & 0xFF] ^ SCUBE[5][mir[0xc] & 0xFF];
        KMASK[10] = SCUBE[4][mir[0x7] & 0xFF] ^ SCUBE[5][mir[0x6] & 0xFF] ^ SCUBE[6][mir[0x8] & 0xFF] ^ SCUBE[7][mir[0x9] & 0xFF] ^ SCUBE[6][mir[0x2] & 0xFF];
        KMASK[11] = SCUBE[4][mir[0x5] & 0xFF] ^ SCUBE[5][mir[0x4] & 0xFF] ^ SCUBE[6][mir[0xA] & 0xFF] ^ SCUBE[7][mir[0xB] & 0xFF] ^ SCUBE[7][mir[0x6] & 0xFF];

        zA = Bits.intFromBytes(mir, 0x0);
        zB = Bits.intFromBytes(mir, 0x4);
        zC = Bits.intFromBytes(mir, 0x8);
        zD = Bits.intFromBytes(mir, 0xC);
        xA = zC ^ SCUBE[4][mir[0x5] & 0xFF] ^ SCUBE[5][mir[0x7] & 0xFF] ^ SCUBE[6][mir[0x4] & 0xFF] ^ SCUBE[7][mir[0x6] & 0xFF] ^ SCUBE[6][mir[0x0] & 0xFF];
        Bits.intToBytes(xA, key, 0x0);
        xB = zA ^ SCUBE[4][key[0x0] & 0xFF] ^ SCUBE[5][key[0x2] & 0xFF] ^ SCUBE[6][key[0x1] & 0xFF] ^ SCUBE[7][key[0x3] & 0xFF] ^ SCUBE[7][mir[0x2] & 0xFF];
        Bits.intToBytes(xB, key, 0x4);
        xC = zB ^ SCUBE[4][key[0x7] & 0xFF] ^ SCUBE[5][key[0x6] & 0xFF] ^ SCUBE[6][key[0x5] & 0xFF] ^ SCUBE[7][key[0x4] & 0xFF] ^ SCUBE[4][mir[0x1] & 0xFF];
        Bits.intToBytes(xC, key, 0x8);
        xD = zD ^ SCUBE[4][key[0xA] & 0xFF] ^ SCUBE[5][key[0x9] & 0xFF] ^ SCUBE[6][key[0xB] & 0xFF] ^ SCUBE[7][key[0x8] & 0xFF] ^ SCUBE[5][mir[0x3] & 0xFF];
        Bits.intToBytes(xD, key, 0xC);
        KMASK[12] = SCUBE[4][key[0x8] & 0xFF] ^ SCUBE[5][key[0x9] & 0xFF] ^ SCUBE[6][key[0x7] & 0xFF] ^ SCUBE[7][key[0x6] & 0xFF] ^ SCUBE[4][key[0x3] & 0xFF];
        KMASK[13] = SCUBE[4][key[0xA] & 0xFF] ^ SCUBE[5][key[0xB] & 0xFF] ^ SCUBE[6][key[0x5] & 0xFF] ^ SCUBE[7][key[0x4] & 0xFF] ^ SCUBE[5][key[0x7] & 0xFF];
        KMASK[14] = SCUBE[4][key[0xC] & 0xFF] ^ SCUBE[5][key[0xD] & 0xFF] ^ SCUBE[6][key[0x3] & 0xFF] ^ SCUBE[7][key[0x2] & 0xFF] ^ SCUBE[6][key[0x8] & 0xFF];
        KMASK[15] = SCUBE[4][key[0xE] & 0xFF] ^ SCUBE[5][key[0xF] & 0xFF] ^ SCUBE[6][key[0x1] & 0xFF] ^ SCUBE[7][key[0x0] & 0xFF] ^ SCUBE[7][key[0xD] & 0xFF];

        xA = Bits.intFromBytes(key, 0x0);
        xB = Bits.intFromBytes(key, 0x4);
        xC = Bits.intFromBytes(key, 0x8);
        xD = Bits.intFromBytes(key, 0xC);
        zA = xA ^ SCUBE[4][key[0xD] & 0xFF] ^ SCUBE[5][key[0xF] & 0xFF] ^ SCUBE[6][key[0xC] & 0xFF] ^ SCUBE[7][key[0xE] & 0xFF] ^ SCUBE[6][key[0x8] & 0xFF];
        Bits.intToBytes(zA, mir, 0x0);
        zB = xC ^ SCUBE[4][mir[0x0] & 0xFF] ^ SCUBE[5][mir[0x2] & 0xFF] ^ SCUBE[6][mir[0x1] & 0xFF] ^ SCUBE[7][mir[0x3] & 0xFF] ^ SCUBE[7][key[0xA] & 0xFF];
        Bits.intToBytes(zB, mir, 0x4);
        zC = xD ^ SCUBE[4][mir[0x7] & 0xFF] ^ SCUBE[5][mir[0x6] & 0xFF] ^ SCUBE[6][mir[0x5] & 0xFF] ^ SCUBE[7][mir[0x4] & 0xFF] ^ SCUBE[4][key[0x9] & 0xFF];
        Bits.intToBytes(zC, mir, 0x8);
        zD = xB ^ SCUBE[4][mir[0xA] & 0xFF] ^ SCUBE[5][mir[0x9] & 0xFF] ^ SCUBE[6][mir[0xB] & 0xFF] ^ SCUBE[7][mir[0x8] & 0xFF] ^ SCUBE[5][key[0xB] & 0xFF];
        Bits.intToBytes(zD, mir, 0xC);
        KROT[ 0] = (SCUBE[4][mir[0x8] & 0xFF] ^ SCUBE[5][mir[0x9] & 0xFF] ^ SCUBE[6][mir[0x7] & 0xFF] ^ SCUBE[7][mir[0x6] & 0xFF] ^ SCUBE[4][mir[0x2] & 0xFF])&0x1f;
        KROT[ 1] = (SCUBE[4][mir[0xA] & 0xFF] ^ SCUBE[5][mir[0xB] & 0xFF] ^ SCUBE[6][mir[0x5] & 0xFF] ^ SCUBE[7][mir[0x4] & 0xFF] ^ SCUBE[5][mir[0x6] & 0xFF])&0x1f;
        KROT[ 2] = (SCUBE[4][mir[0xC] & 0xFF] ^ SCUBE[5][mir[0xD] & 0xFF] ^ SCUBE[6][mir[0x3] & 0xFF] ^ SCUBE[7][mir[0x2] & 0xFF] ^ SCUBE[6][mir[0x9] & 0xFF])&0x1f;
        KROT[ 3] = (SCUBE[4][mir[0xE] & 0xFF] ^ SCUBE[5][mir[0xF] & 0xFF] ^ SCUBE[6][mir[0x1] & 0xFF] ^ SCUBE[7][mir[0x0] & 0xFF] ^ SCUBE[7][mir[0xC] & 0xFF])&0x1f;

        zA = Bits.intFromBytes(mir, 0x0);
        zB = Bits.intFromBytes(mir, 0x4);
        zC = Bits.intFromBytes(mir, 0x8);
        zD = Bits.intFromBytes(mir, 0xC);
        xA = zC ^ SCUBE[4][mir[0x5] & 0xFF] ^ SCUBE[5][mir[0x7] & 0xFF] ^ SCUBE[6][mir[0x4] & 0xFF] ^ SCUBE[7][mir[0x6] & 0xFF] ^ SCUBE[6][mir[0x0] & 0xFF];
        Bits.intToBytes(xA, key, 0x0);
        xB = zA ^ SCUBE[4][key[0x0] & 0xFF] ^ SCUBE[5][key[0x2] & 0xFF] ^ SCUBE[6][key[0x1] & 0xFF] ^ SCUBE[7][key[0x3] & 0xFF] ^ SCUBE[7][mir[0x2] & 0xFF];
        Bits.intToBytes(xB, key, 0x4);
        xC = zB ^ SCUBE[4][key[0x7] & 0xFF] ^ SCUBE[5][key[0x6] & 0xFF] ^ SCUBE[6][key[0x5] & 0xFF] ^ SCUBE[7][key[0x4] & 0xFF] ^ SCUBE[4][mir[0x1] & 0xFF];
        Bits.intToBytes(xC, key, 0x8);
        xD = zD ^ SCUBE[4][key[0xA] & 0xFF] ^ SCUBE[5][key[0x9] & 0xFF] ^ SCUBE[6][key[0xB] & 0xFF] ^ SCUBE[7][key[0x8] & 0xFF] ^ SCUBE[5][mir[0x3] & 0xFF];
        Bits.intToBytes(xD, key, 0xC);
        KROT[ 4] = (SCUBE[4][key[0x3] & 0xFF] ^ SCUBE[5][key[0x2] & 0xFF] ^ SCUBE[6][key[0xC] & 0xFF] ^ SCUBE[7][key[0xD] & 0xFF] ^ SCUBE[4][key[0x8] & 0xFF])&0x1f;
        KROT[ 5] = (SCUBE[4][key[0x1] & 0xFF] ^ SCUBE[5][key[0x0] & 0xFF] ^ SCUBE[6][key[0xE] & 0xFF] ^ SCUBE[7][key[0xF] & 0xFF] ^ SCUBE[5][key[0xD] & 0xFF])&0x1f;
        KROT[ 6] = (SCUBE[4][key[0x7] & 0xFF] ^ SCUBE[5][key[0x6] & 0xFF] ^ SCUBE[6][key[0x8] & 0xFF] ^ SCUBE[7][key[0x9] & 0xFF] ^ SCUBE[6][key[0x3] & 0xFF])&0x1f;
        KROT[ 7] = (SCUBE[4][key[0x5] & 0xFF] ^ SCUBE[5][key[0x4] & 0xFF] ^ SCUBE[6][key[0xA] & 0xFF] ^ SCUBE[7][key[0xB] & 0xFF] ^ SCUBE[7][key[0x7] & 0xFF])&0x1f;

        xA = Bits.intFromBytes(key, 0x0);
        xB = Bits.intFromBytes(key, 0x4);
        xC = Bits.intFromBytes(key, 0x8);
        xD = Bits.intFromBytes(key, 0xC);
        zA = xA ^ SCUBE[4][key[0xD] & 0xFF] ^ SCUBE[5][key[0xF] & 0xFF] ^ SCUBE[6][key[0xC] & 0xFF] ^ SCUBE[7][key[0xE] & 0xFF] ^ SCUBE[6][key[0x8] & 0xFF];
        Bits.intToBytes(zA, mir, 0x0);
        zB = xC ^ SCUBE[4][mir[0x0] & 0xFF] ^ SCUBE[5][mir[0x2] & 0xFF] ^ SCUBE[6][mir[0x1] & 0xFF] ^ SCUBE[7][mir[0x3] & 0xFF] ^ SCUBE[7][key[0xA] & 0xFF];
        Bits.intToBytes(zB, mir, 0x4);
        zC = xD ^ SCUBE[4][mir[0x7] & 0xFF] ^ SCUBE[5][mir[0x6] & 0xFF] ^ SCUBE[6][mir[0x5] & 0xFF] ^ SCUBE[7][mir[0x4] & 0xFF] ^ SCUBE[4][key[0x9] & 0xFF];
        Bits.intToBytes(zC, mir, 0x8);
        zD = xB ^ SCUBE[4][mir[0xA] & 0xFF] ^ SCUBE[5][mir[0x9] & 0xFF] ^ SCUBE[6][mir[0xB] & 0xFF] ^ SCUBE[7][mir[0x8] & 0xFF] ^ SCUBE[5][key[0xB] & 0xFF];
        Bits.intToBytes(zD, mir, 0xC);
        KROT[ 8] = (SCUBE[4][mir[0x3] & 0xFF] ^ SCUBE[5][mir[0x2] & 0xFF] ^ SCUBE[6][mir[0xC] & 0xFF] ^ SCUBE[7][mir[0xD] & 0xFF] ^ SCUBE[4][mir[0x9] & 0xFF])&0x1f;
        KROT[ 9] = (SCUBE[4][mir[0x1] & 0xFF] ^ SCUBE[5][mir[0x0] & 0xFF] ^ SCUBE[6][mir[0xE] & 0xFF] ^ SCUBE[7][mir[0xF] & 0xFF] ^ SCUBE[5][mir[0xc] & 0xFF])&0x1f;
        KROT[10] = (SCUBE[4][mir[0x7] & 0xFF] ^ SCUBE[5][mir[0x6] & 0xFF] ^ SCUBE[6][mir[0x8] & 0xFF] ^ SCUBE[7][mir[0x9] & 0xFF] ^ SCUBE[6][mir[0x2] & 0xFF])&0x1f;
        KROT[11] = (SCUBE[4][mir[0x5] & 0xFF] ^ SCUBE[5][mir[0x4] & 0xFF] ^ SCUBE[6][mir[0xA] & 0xFF] ^ SCUBE[7][mir[0xB] & 0xFF] ^ SCUBE[7][mir[0x6] & 0xFF])&0x1f;

        zA = Bits.intFromBytes(mir, 0x0);
        zB = Bits.intFromBytes(mir, 0x4);
        zC = Bits.intFromBytes(mir, 0x8);
        zD = Bits.intFromBytes(mir, 0xC);
        xA = zC ^ SCUBE[4][mir[0x5] & 0xFF] ^ SCUBE[5][mir[0x7] & 0xFF] ^ SCUBE[6][mir[0x4] & 0xFF] ^ SCUBE[7][mir[0x6] & 0xFF] ^ SCUBE[6][mir[0x0] & 0xFF];
        Bits.intToBytes(xA, key, 0x0);
        xB = zA ^ SCUBE[4][key[0x0] & 0xFF] ^ SCUBE[5][key[0x2] & 0xFF] ^ SCUBE[6][key[0x1] & 0xFF] ^ SCUBE[7][key[0x3] & 0xFF] ^ SCUBE[7][mir[0x2] & 0xFF];
        Bits.intToBytes(xB, key, 0x4);
        xC = zB ^ SCUBE[4][key[0x7] & 0xFF] ^ SCUBE[5][key[0x6] & 0xFF] ^ SCUBE[6][key[0x5] & 0xFF] ^ SCUBE[7][key[0x4] & 0xFF] ^ SCUBE[4][mir[0x1] & 0xFF];
        Bits.intToBytes(xC, key, 0x8);
        xD = zD ^ SCUBE[4][key[0xA] & 0xFF] ^ SCUBE[5][key[0x9] & 0xFF] ^ SCUBE[6][key[0xB] & 0xFF] ^ SCUBE[7][key[0x8] & 0xFF] ^ SCUBE[5][mir[0x3] & 0xFF];
        Bits.intToBytes(xD, key, 0xC);
        KROT[12] = (SCUBE[4][key[0x8] & 0xFF] ^ SCUBE[5][key[0x9] & 0xFF] ^ SCUBE[6][key[0x7] & 0xFF] ^ SCUBE[7][key[0x6] & 0xFF] ^ SCUBE[4][key[0x3] & 0xFF])&0x1f;
        KROT[13] = (SCUBE[4][key[0xA] & 0xFF] ^ SCUBE[5][key[0xB] & 0xFF] ^ SCUBE[6][key[0x5] & 0xFF] ^ SCUBE[7][key[0x4] & 0xFF] ^ SCUBE[5][key[0x7] & 0xFF])&0x1f;
        KROT[14] = (SCUBE[4][key[0xC] & 0xFF] ^ SCUBE[5][key[0xD] & 0xFF] ^ SCUBE[6][key[0x3] & 0xFF] ^ SCUBE[7][key[0x2] & 0xFF] ^ SCUBE[6][key[0x8] & 0xFF])&0x1f;
        KROT[15] = (SCUBE[4][key[0xE] & 0xFF] ^ SCUBE[5][key[0xF] & 0xFF] ^ SCUBE[6][key[0x1] & 0xFF] ^ SCUBE[7][key[0x0] & 0xFF] ^ SCUBE[7][key[0xD] & 0xFF])&0x1f;
	}

	public void wipe()
	{
		R = 0;
		Arrays.fill(KROT, 0);
		Arrays.fill(KMASK, 0);
		KROT = KMASK = null;
		this.key = null;
	}

	public int plaintextSize()
	{
		return 8;
	}
	
	public int ciphertextSize()
	{
		return 8;
	}

	public void decryptBlock(byte[] block, int start)
	{
		int A = Bits.intFromBytes(block, start + 4);
		int B = Bits.intFromBytes(block, start + 0);
		int cA = A, cB = B;
		
		int i = R;
		if(i-- == 16) {
			A = cA; B = cB;	cA = B;
			cB = A ^ F1(B, KMASK[i], KROT[i--]);
		}
		
		while(i >= 0)
		{
			A = cA; B = cB;	cA = B;
			cB = A ^ F3(B, KMASK[i], KROT[i--]);

			A = cA; B = cB;	cA = B;
			cB = A ^ F2(B, KMASK[i], KROT[i--]);
			
			A = cA; B = cB;	cA = B;
			cB = A ^ F1(B, KMASK[i], KROT[i--]);
		}
		
		Bits.intToBytes(cA, block, start + 4);
		Bits.intToBytes(cB, block, start + 0);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int A = Bits.intFromBytes(block, start0 + 4);
		int B = Bits.intFromBytes(block, start0    );
		int cA = A, cB = B;
		
		int i = R;
		if(i-- == 16) {
			A = cA; B = cB;	cA = B;
			cB = A ^ F1(B, KMASK[i], KROT[i--]);
		}
		
		while(i >= 0)
		{
			A = cA; B = cB;	cA = B;
			cB = A ^ F3(B, KMASK[i], KROT[i--]);

			A = cA; B = cB;	cA = B;
			cB = A ^ F2(B, KMASK[i], KROT[i--]);
			
			A = cA; B = cB;	cA = B;
			cB = A ^ F1(B, KMASK[i], KROT[i--]);
		}
		
		Bits.intToBytes(cA, out, start1 + 4);
		Bits.intToBytes(cB, out, start1    );
	}

	public void encryptBlock(byte[] block, int start)
	{
		int A = Bits.intFromBytes(block, start + 0);
		int B = Bits.intFromBytes(block, start + 4);
		int cA = A, cB = B;
		
		int i = 0;
		
		while(true)
		{
			A = cA; B = cB;	cA = B;
			cB = A ^ F1(B, KMASK[i], KROT[i++]);
			
			if(i == R)
				break;
			
			A = cA; B = cB;	cA = B;
			cB = A ^ F2(B, KMASK[i], KROT[i++]);
			
			if(i == R)
				break;
			
			A = cA; B = cB;	cA = B;
			cB = A ^ F3(B, KMASK[i], KROT[i++]);
			
			if(i == R)
				break;
		}
		
		Bits.intToBytes(cA, block, start + 0);
		Bits.intToBytes(cB, block, start + 4);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int A = Bits.intFromBytes(block, start0    );
		int B = Bits.intFromBytes(block, start0 + 4);
		int cA = A, cB = B;
		
		int i = 0;
		
		while(true)
		{
			A = cA; B = cB;	cA = B;
			cB = A ^ F1(B, KMASK[i], KROT[i++]);
			
			if(i == R)
				break;
			
			A = cA; B = cB;	cA = B;
			cB = A ^ F2(B, KMASK[i], KROT[i++]);
			
			if(i == R)
				break;
			
			A = cA; B = cB;	cA = B;
			cB = A ^ F3(B, KMASK[i], KROT[i++]);
			
			if(i == R)
				break;
		}
		
		Bits.intToBytes(cA, out, start1 + 0);
		Bits.intToBytes(cB, out, start1 + 4);
	}

	public KeyCAST5 newKey(IRandom rand)
	{
		byte[] n = new byte[16];
		RandUtils.fillArr(n, rand);
		return new KeyCAST5(n);
	}

	public void genKey(IRandom rand)
	{
		this.setKey(newKey(rand));
	}
	
	public void reset() {}

}
