package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.BMW_Base_32.BMW_32State;
import claire.util.io.Factory;
import claire.util.math.counters.IntCounter;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

abstract class BMW_Base_32<Hash extends BMW_Base_32<Hash>>
	  	 extends MerkleHash<BMW_32State, Hash> {
	
	protected final int[] STATE = new int[16];
	protected final int[] counters = new int[2];
	
	private final int[] BLOCK = new int[16];
	private final int[] WORK = new int[32];
	
	private final IntCounter counter = new IntCounter(counters);
	
	private static final int[] last = 
	{
		0xaaaaaaa0, 0xaaaaaaa1, 0xaaaaaaa2, 0xaaaaaaa3,
		0xaaaaaaa4, 0xaaaaaaa5, 0xaaaaaaa6, 0xaaaaaaa7,
		0xaaaaaaa8, 0xaaaaaaa9, 0xaaaaaaaa, 0xaaaaaaab,
		0xaaaaaaac, 0xaaaaaaad, 0xaaaaaaae, 0xaaaaaaaf
	};

	public BMW_Base_32(int out) 
	{
		super(64, out);
		reset();
	}

	protected abstract int[] getIV();
	protected abstract void output(byte[] out, int start);
	
	public void reset()
	{
		super.reset();
		System.arraycopy(this.getIV(), 0, STATE, 0, 16);
		counter.reset();
	}
	
	private void compress(int[] IN)
	{
		WORK[0] = ((((  IN[5 ] ^ STATE[5 ]) - (IN[7 ] ^ STATE[7 ]) + (IN[10] ^ STATE[10])
				   +   (IN[13] ^ STATE[13]) + (IN[14] ^ STATE[14])) >>> 1)
				   ^ (((IN[5 ] ^ STATE[5 ]) - (IN[7 ] ^ STATE[7 ]) + (IN[10] ^ STATE[10])
				   +   (IN[13] ^ STATE[13]) + (IN[14] ^ STATE[14])) << 3)
				   ^ Bits.rotateLeft(((IN[5] ^ STATE[5]) - (IN[7] ^ STATE[7])
				   +   (IN[10] ^ STATE[10]) + (IN[13] ^ STATE[13])
				   +   (IN[14] ^ STATE[14])), 4)
				   ^ Bits.rotateLeft(((IN[5] ^ STATE[5]) - (IN[7] ^ STATE[7])
				   +   (IN[10] ^ STATE[10]) + (IN[13] ^ STATE[13])
				   +   (IN[14] ^ STATE[14])), 19))
				   + STATE[1 ];
		WORK[1] = ((((  IN[6 ] ^ STATE[6 ]) - (IN[8 ] ^ STATE[8 ]) + (IN[11] ^ STATE[11])
				   +   (IN[14] ^ STATE[14]) - (IN[15] ^ STATE[15])) >>> 1)
				   ^ (((IN[6 ] ^ STATE[6 ]) - (IN[8 ] ^ STATE[8 ]) + (IN[11] ^ STATE[11])
				   +   (IN[14] ^ STATE[14]) - (IN[15] ^ STATE[15])) << 2)
				   ^ Bits.rotateLeft(((IN[6] ^ STATE[6]) - (IN[8] ^ STATE[8])
				   +   (IN[11] ^ STATE[11]) + (IN[14] ^ STATE[14])
				   -   (IN[15] ^ STATE[15])), 8)
			       ^ Bits.rotateLeft(((IN[6] ^ STATE[6]) - (IN[8] ^ STATE[8])
				   +   (IN[11] ^ STATE[11]) + (IN[14] ^ STATE[14])
				   -   (IN[15] ^ STATE[15])), 23))
				   + STATE[2 ];
		WORK[2] = ((((  IN[0 ] ^ STATE[0 ]) + (IN[7 ] ^ STATE[7 ]) + (IN[9] ^ STATE[9])
				   -   (IN[12] ^ STATE[12]) + (IN[15] ^ STATE[15])) >>> 2)
				   ^ (((IN[0 ] ^ STATE[0 ]) + (IN[7 ] ^ STATE[7 ]) + (IN[9] ^ STATE[9])
				   -   (IN[12] ^ STATE[12]) + (IN[15] ^ STATE[15])) << 1)
			       ^ Bits.rotateLeft(((IN[0] ^ STATE[0]) + (IN[7] ^ STATE[7])
				   +   (IN[9 ] ^ STATE[9 ]) - (IN[12] ^ STATE[12])
				   +   (IN[15] ^ STATE[15])), 12)
			       ^ Bits.rotateLeft(((IN[0] ^ STATE[0]) + (IN[7] ^ STATE[7])
				   +   (IN[9 ] ^ STATE[9 ]) - (IN[12] ^ STATE[12])
				   +   (IN[15] ^ STATE[15])), 25))
				   + STATE[3 ];
		WORK[3] = ((((  IN[0 ] ^ STATE[0 ]) - (IN[1 ] ^ STATE[1 ]) + (IN[8] ^ STATE[8])
				   -   (IN[10] ^ STATE[10]) + (IN[13] ^ STATE[13])) >>> 2)
				   ^ (((IN[0 ] ^ STATE[0 ]) - (IN[1 ] ^ STATE[1 ]) + (IN[8] ^ STATE[8])
			       -   (IN[10] ^ STATE[10]) + (IN[13] ^ STATE[13])) << 2)
			       ^ Bits.rotateLeft(((IN[0] ^ STATE[0]) - (IN[1] ^ STATE[1])
			       +   (IN[8 ] ^ STATE[8 ]) - (IN[10] ^ STATE[10])
				   +   (IN[13] ^ STATE[13])), 15)
				   ^ Bits.rotateLeft(((IN[0] ^ STATE[0]) - (IN[1] ^ STATE[1])
				   +   (IN[8 ] ^ STATE[8 ]) - (IN[10] ^ STATE[10])
				   +   (IN[13] ^ STATE[13])), 29))
				   + STATE[4 ];
		WORK[4] = ((((  IN[1 ] ^ STATE[1 ]) + (IN[2 ] ^ STATE[2 ]) + (IN[9] ^ STATE[9])
				   -   (IN[11] ^ STATE[11]) - (IN[14] ^ STATE[14])) >>> 1)
				   ^  ((IN[1 ] ^ STATE[1 ]) + (IN[2 ] ^ STATE[2 ]) + (IN[9] ^ STATE[9])
				   -   (IN[11] ^ STATE[11]) - (IN[14] ^ STATE[14]))) + STATE[5];
		WORK[5] = (((  (IN[3 ] ^ STATE[3 ]) - (IN[2 ] ^ STATE[2 ]) + (IN[10] ^ STATE[10])
				   -   (IN[12] ^ STATE[12]) + (IN[15] ^ STATE[15])) >>> 1)
				   ^ (((IN[3 ] ^ STATE[3 ]) - (IN[2 ] ^ STATE[2 ]) + (IN[10] ^ STATE[10])
				   -   (IN[12] ^ STATE[12]) + (IN[15] ^ STATE[15])) << 3)
				   ^ Bits.rotateLeft(((IN[3] ^ STATE[3]) - (IN[2] ^ STATE[2])
				   +   (IN[10] ^ STATE[10]) - (IN[12] ^ STATE[12])
				   +   (IN[15] ^ STATE[15])), 4)
				   ^ Bits.rotateLeft(((IN[3] ^ STATE[3]) - (IN[2] ^ STATE[2])
				   +   (IN[10] ^ STATE[10]) - (IN[12] ^ STATE[12])
				   +   (IN[15] ^ STATE[15])), 19))
				   + STATE[6 ];
		WORK[6] = ((((  IN[4 ] ^ STATE[4 ]) - (IN[0 ] ^ STATE[0 ]) - (IN[3] ^ STATE[3])
				   -   (IN[11] ^ STATE[11]) + (IN[13] ^ STATE[13])) >>> 1)
				   ^ (((IN[4 ] ^ STATE[4 ]) - (IN[0 ] ^ STATE[0 ]) - (IN[3] ^ STATE[3])
                   -   (IN[11] ^ STATE[11]) + (IN[13] ^ STATE[13])) << 2)
                   ^ Bits.rotateLeft(((IN[4] ^ STATE[4]) - (IN[0] ^ STATE[0])
				   -   (IN[3 ] ^ STATE[3 ]) - (IN[11] ^ STATE[11])
				   + (  IN[13] ^ STATE[13])), 8)
				   ^ Bits.rotateLeft(((IN[4] ^ STATE[4]) - (IN[0] ^ STATE[0])
				   - (  IN[3 ] ^ STATE[3 ]) - (IN[11] ^ STATE[11])
				   + (  IN[13] ^ STATE[13])), 23))
				   + STATE[7];
		WORK[7] = ((((  IN[1 ] ^ STATE[1 ]) - (IN[4 ] ^ STATE[4 ]) - (IN[5] ^ STATE[5])
				   -   (IN[12] ^ STATE[12]) - (IN[14] ^ STATE[14])) >>> 2)
				   ^ (((IN[1 ] ^ STATE[1 ]) - (IN[4 ] ^ STATE[4 ]) - (IN[5] ^ STATE[5])
			       -   (IN[12] ^ STATE[12]) - (IN[14] ^ STATE[14])) << 1)
			       ^ Bits.rotateLeft(((IN[1] ^ STATE[1]) - (IN[4] ^ STATE[4])
                   -   (IN[5 ] ^ STATE[5 ]) - (IN[12] ^ STATE[12])
                   -   (IN[14] ^ STATE[14])), 12)
                   ^ Bits.rotateLeft(((IN[1] ^ STATE[1]) - (IN[4] ^ STATE[4])
                   -   (IN[5 ] ^ STATE[5 ]) - (IN[12] ^ STATE[12])
                   -   (IN[14] ^ STATE[14])), 25))
                   + STATE[8];
		WORK[8] = ((((  IN[2 ] ^ STATE[2 ]) - (IN[5 ] ^ STATE[5 ]) - (IN[6] ^ STATE[6])
				   +   (IN[13] ^ STATE[13]) - (IN[15] ^ STATE[15])) >>> 2)
				   ^ (((IN[2 ] ^ STATE[2 ]) - (IN[5 ] ^ STATE[5 ]) - (IN[6] ^ STATE[6])
			       +   (IN[13] ^ STATE[13]) - (IN[15] ^ STATE[15])) << 2)
			       ^ Bits.rotateLeft(((IN[2] ^ STATE[2]) - (IN[5] ^ STATE[5])
				   -   (IN[6 ] ^ STATE[6 ]) + (IN[13] ^ STATE[13])
				   -   (IN[15] ^ STATE[15])), 15)
				   ^ Bits.rotateLeft(((IN[2] ^ STATE[2]) - (IN[5] ^ STATE[5])
				   -   (IN[6 ] ^ STATE[6 ]) + (IN[13] ^ STATE[13])
				   -   (IN[15] ^ STATE[15])), 29))
				   + STATE[9 ];
		WORK[9] = ((((  IN[0 ] ^ STATE[0 ]) - (IN[3 ] ^ STATE[3 ]) + (IN[6] ^ STATE[6])
				   -   (IN[7 ] ^ STATE[7 ]) + (IN[14] ^ STATE[14])) >>> 1)
				   ^  ((IN[0 ] ^ STATE[0 ]) - (IN[3 ] ^ STATE[3 ]) + (IN[6] ^ STATE[6])
                   -   (IN[7 ] ^ STATE[7 ]) + (IN[14] ^ STATE[14]))) + STATE[10];
		WORK[10] = (((( IN[8 ] ^ STATE[8 ]) - (IN[1 ] ^ STATE[1 ]) - (IN[4] ^ STATE[4])
				   -   (IN[7 ] ^ STATE[7 ]) + (IN[15] ^ STATE[15])) >>> 1)
				   ^ (((IN[8 ] ^ STATE[8 ]) - (IN[1 ] ^ STATE[1 ]) - (IN[4] ^ STATE[4])
			       -   (IN[7 ] ^ STATE[7 ]) + (IN[15] ^ STATE[15])) << 3)
			       ^ Bits.rotateLeft(((IN[8] ^ STATE[8]) - (IN[1] ^ STATE[1])
				   -   (IN[4 ] ^ STATE[4 ]) - (IN[7 ] ^ STATE[7 ])
				   +   (IN[15] ^ STATE[15])), 4)
				   ^ Bits.rotateLeft(((IN[8] ^ STATE[8]) - (IN[1] ^ STATE[1])
				   -   (IN[4 ] ^ STATE[4 ]) - (IN[7 ] ^ STATE[7 ])
				   +   (IN[15] ^ STATE[15])), 19))
				   + STATE[11];
		WORK[11] = (((( IN[8 ] ^ STATE[8 ]) - (IN[0 ] ^ STATE[0 ]) - (IN[2] ^ STATE[2])
				   -   (IN[5 ] ^ STATE[5 ]) + (IN[9 ] ^ STATE[9 ])) >>> 1)
				   ^ (((IN[8 ] ^ STATE[8 ]) - (IN[0 ] ^ STATE[0 ]) - (IN[2] ^ STATE[2])
			       -   (IN[5 ] ^ STATE[5 ]) + (IN[9 ] ^ STATE[9 ])) << 2)
			       ^ Bits.rotateLeft(((IN[8] ^ STATE[8]) - (IN[0] ^ STATE[0])
                   -   (IN[2 ] ^ STATE[2 ]) - (IN[5 ] ^ STATE[5 ])
                   +   (IN[9 ] ^ STATE[9 ])), 8)
                   ^ Bits.rotateLeft(((IN[8] ^ STATE[8]) - (IN[0] ^ STATE[0])
				   -   (IN[2 ] ^ STATE[2 ]) - (IN[5 ] ^ STATE[5])
				   +   (IN[9 ] ^ STATE[9 ])), 23))
				   + STATE[12];
		WORK[12] = (((( IN[1 ] ^ STATE[1 ]) + (IN[3 ] ^ STATE[3 ]) - (IN[6] ^ STATE[6])
				   -   (IN[9 ] ^ STATE[9 ]) + (IN[10] ^ STATE[10])) >>> 2)
				   ^ (((IN[1 ] ^ STATE[1 ]) + (IN[3 ] ^ STATE[3 ]) - (IN[6] ^ STATE[6])
			       -   (IN[9 ] ^ STATE[9 ]) + (IN[10] ^ STATE[10])) << 1)
			       ^ Bits.rotateLeft(((IN[1] ^ STATE[1]) + (IN[3] ^ STATE[3])
				   -   (IN[6 ] ^ STATE[6 ]) - (IN[9 ] ^ STATE[9 ])
				   +   (IN[10] ^ STATE[10])), 12)
				   ^ Bits.rotateLeft(((IN[1] ^ STATE[1]) + (IN[3] ^ STATE[3])
				   -   (IN[6 ] ^ STATE[6 ]) - (IN[9 ] ^ STATE[9 ])
				   +   (IN[10] ^ STATE[10])), 25))
				   + STATE[13];
		WORK[13] = (((( IN[2 ] ^ STATE[2 ]) + (IN[4 ] ^ STATE[4 ]) + (IN[7] ^ STATE[7])
				   +   (IN[10] ^ STATE[10]) + (IN[11] ^ STATE[11])) >>> 2)
				   ^ (((IN[2 ] ^ STATE[2 ]) + (IN[4 ] ^ STATE[4 ]) + (IN[7] ^ STATE[7])
			       +   (IN[10] ^ STATE[10]) + (IN[11] ^ STATE[11])) << 2)
			       ^ Bits.rotateLeft(((IN[2] ^ STATE[2]) + (IN[4] ^ STATE[4])
                   +   (IN[7 ] ^ STATE[7 ]) + (IN[10] ^ STATE[10])
                   +   (IN[11] ^ STATE[11])), 15)
                   ^ Bits.rotateLeft(((IN[2] ^ STATE[2]) + (IN[4] ^ STATE[4])
				   +   (IN[7 ] ^ STATE[7 ]) + (IN[10] ^ STATE[10])
				   +   (IN[11] ^ STATE[11])), 29))
				   + STATE[14];
		WORK[14] = (((( IN[3 ] ^ STATE[3 ]) - (IN[5 ] ^ STATE[5 ]) + (IN[8] ^ STATE[8])
				   -   (IN[11] ^ STATE[11]) - (IN[12] ^ STATE[12])) >>> 1)
				   ^  ((IN[3 ] ^ STATE[3 ]) - (IN[5 ] ^ STATE[5 ]) + (IN[8] ^ STATE[8])
			       -   (IN[11] ^ STATE[11]) - (IN[12] ^ STATE[12]))) + STATE[15];
		WORK[15] = (((( IN[12] ^ STATE[12]) - (IN[4 ] ^ STATE[4 ]) - (IN[6] ^ STATE[6])
				   -   (IN[9 ] ^ STATE[9 ]) + (IN[13] ^ STATE[13])) >>> 1)
				   ^ (((IN[12] ^ STATE[12]) - (IN[4 ] ^ STATE[4 ]) - (IN[6] ^ STATE[6])
				   -   (IN[9 ] ^ STATE[9 ]) + (IN[13] ^ STATE[13])) << 3)
				   ^ Bits.rotateLeft(((IN[12] ^ STATE[12]) - (IN[4] ^ STATE[4])
				   -   (IN[6 ] ^ STATE[6 ]) - (IN[9 ] ^ STATE[9 ])
				   +   (IN[13] ^ STATE[13])), 4)
				   ^ Bits.rotateLeft(((IN[12] ^ STATE[12]) - (IN[4] ^ STATE[4])
                   -   (IN[6 ] ^ STATE[6 ]) - (IN[9 ] ^ STATE[9 ])
                   +   (IN[13] ^ STATE[13])), 19))
                   + STATE[0 ];
		WORK[16] = (((WORK[0 ] >>> 1) ^ (WORK[0 ] << 2)
				  ^  	 Bits.rotateLeft(WORK[0 ], 8 ) ^ Bits.rotateLeft(WORK[0 ], 23))
				  + ((WORK[1 ] >>> 2) ^ (WORK[1 ] << 1)
				  ^ 	 Bits.rotateLeft(WORK[1 ], 12) ^ Bits.rotateLeft(WORK[1 ], 25))
				  + ((WORK[2 ] >>> 2) ^ (WORK[2 ] << 2)
				  ^      Bits.rotateLeft(WORK[2 ], 15) ^ Bits.rotateLeft(WORK[2 ], 29))
				  + ((WORK[3 ] >>> 1) ^ (WORK[3 ] << 3)
				  ^  	 Bits.rotateLeft(WORK[3 ], 4 ) ^ Bits.rotateLeft(WORK[3 ], 19))
				  + ((WORK[4 ] >>> 1) ^ (WORK[4 ] << 2)
			      ^ 	 Bits.rotateLeft(WORK[4 ], 8 ) ^ Bits.rotateLeft(WORK[4 ], 23))
			      + ((WORK[5 ] >>> 2) ^ (WORK[5 ] << 1)
			      ^ 	 Bits.rotateLeft(WORK[5 ], 12) ^ Bits.rotateLeft(WORK[5 ], 25))
			      + ((WORK[6 ] >>> 2) ^ (WORK[6 ] << 2)
			      ^ 	 Bits.rotateLeft(WORK[6 ], 15) ^ Bits.rotateLeft(WORK[6 ], 29))
			      + ((WORK[7 ] >>> 1) ^ (WORK[7 ] << 3)
			      ^ 	 Bits.rotateLeft(WORK[7 ], 4 ) ^ Bits.rotateLeft(WORK[7 ], 19))
			      + ((WORK[8 ] >>> 1) ^ (WORK[8 ] << 2)
                  ^ 	 Bits.rotateLeft(WORK[8 ], 8 ) ^ Bits.rotateLeft(WORK[8 ], 23))
                  + ((WORK[9 ] >>> 2) ^ (WORK[9 ] << 1)
                  ^ 	 Bits.rotateLeft(WORK[9 ], 12) ^ Bits.rotateLeft(WORK[9 ], 25))
                  + ((WORK[10] >>> 2) ^ (WORK[10] << 2)
			      ^ 	 Bits.rotateLeft(WORK[10], 15) ^ Bits.rotateLeft(WORK[10], 29))
			      + ((WORK[11] >>> 1) ^ (WORK[11] << 3)
                  ^ 	 Bits.rotateLeft(WORK[11], 4 ) ^ Bits.rotateLeft(WORK[11], 19))
				  + ((WORK[12] >>> 1) ^ (WORK[12] << 2)
                  ^ 	 Bits.rotateLeft(WORK[12], 8 ) ^ Bits.rotateLeft(WORK[12], 23))
			      + ((WORK[13] >>> 2) ^ (WORK[13] << 1)
			      ^ 	 Bits.rotateLeft(WORK[13], 12) ^ Bits.rotateLeft(WORK[13], 25))
			      + ((WORK[14] >>> 2) ^ (WORK[14] << 2)
			      ^ 	 Bits.rotateLeft(WORK[14], 15) ^ Bits.rotateLeft(WORK[14], 29))
			      + ((WORK[15] >>> 1) ^ (WORK[15] << 3)
			      ^ 	 Bits.rotateLeft(WORK[15], 4 ) ^ Bits.rotateLeft(WORK[15], 19))
			      + ((Bits.rotateLeft(IN[0 ], 1) + Bits.rotateLeft(IN[3], 4)
			      -   Bits.rotateLeft(IN[10], 11) + (16 * 0x05555555)) ^ STATE[7]));
		WORK[17] = (((WORK[1] >>> 1) ^ (WORK[1 ] << 2)
				 ^ Bits.rotateLeft(WORK[1 ], 8 ) ^ Bits.rotateLeft(WORK[1 ], 23))
				 + ((WORK[2 ] >>> 2) ^ (WORK[2 ] << 1)
				 ^ Bits.rotateLeft(WORK[2 ], 12) ^ Bits.rotateLeft(WORK[2 ], 25))
				 + ((WORK[3 ] >>> 2) ^ (WORK[3 ] << 2)
				 ^ Bits.rotateLeft(WORK[3 ], 15) ^ Bits.rotateLeft(WORK[3 ], 29))
				 + ((WORK[4 ] >>> 1) ^ (WORK[4] << 3)
				 ^ Bits.rotateLeft(WORK[4 ], 4 ) ^ Bits.rotateLeft(WORK[4 ], 19))
				 + ((WORK[5 ] >>> 1) ^ (WORK[5 ] << 2)
				 ^ Bits.rotateLeft(WORK[5 ], 8 ) ^ Bits.rotateLeft(WORK[5 ], 23))
				 + ((WORK[6 ] >>> 2) ^ (WORK[6 ] << 1)
				 ^ Bits.rotateLeft(WORK[6 ], 12) ^ Bits.rotateLeft(WORK[6 ], 25))
				 + ((WORK[7 ] >>> 2) ^ (WORK[7 ] << 2)
				 ^ Bits.rotateLeft(WORK[7 ], 15) ^ Bits.rotateLeft(WORK[7 ], 29))
				 + ((WORK[8 ] >>> 1) ^ (WORK[8 ] << 3)
				 ^ Bits.rotateLeft(WORK[8 ], 4 ) ^ Bits.rotateLeft(WORK[8 ], 19))
				 + ((WORK[9 ] >>> 1) ^ (WORK[9 ] << 2)
				 ^ Bits.rotateLeft(WORK[9 ], 8 ) ^ Bits.rotateLeft(WORK[9 ], 23))
				 + ((WORK[10] >>> 2) ^ (WORK[10] << 1)
				 ^ Bits.rotateLeft(WORK[10], 12) ^ Bits.rotateLeft(WORK[10], 25))
				 + ((WORK[11] >>> 2) ^ (WORK[11] << 2)
				 ^ Bits.rotateLeft(WORK[11], 15) ^ Bits.rotateLeft(WORK[11], 29))
				 + ((WORK[12] >>> 1) ^ (WORK[12] << 3)
				 ^ Bits.rotateLeft(WORK[12], 4 ) ^ Bits.rotateLeft(WORK[12], 19))
				 + ((WORK[13] >>> 1) ^ (WORK[13] << 2)
				 ^ Bits.rotateLeft(WORK[13], 8 ) ^ Bits.rotateLeft(WORK[13], 23))
				 + ((WORK[14] >>> 2) ^ (WORK[14] << 1)
				 ^ Bits.rotateLeft(WORK[14], 12) ^ Bits.rotateLeft(WORK[14], 25))
				 + ((WORK[15] >>> 2) ^ (WORK[15] << 2)
				 ^ Bits.rotateLeft(WORK[15], 15) ^ Bits.rotateLeft(WORK[15], 29))
				 + ((WORK[16] >>> 1) ^ (WORK[16] << 3)
				 ^ Bits.rotateLeft(WORK[16], 4 ) ^ Bits.rotateLeft(WORK[16], 19))
				 + ((Bits.rotateLeft(IN[1 ], 2) + Bits.rotateLeft(IN[4], 5)
				 -   Bits.rotateLeft(IN[11], 12) + (17 * 0x05555555)) ^ STATE[8]));
		WORK[18] =  (WORK[2 ] + Bits.rotateLeft(WORK[3 ], 3 )
				 +   WORK[4 ] + Bits.rotateLeft(WORK[5 ], 7 )
				 +   WORK[6 ] + Bits.rotateLeft(WORK[7 ], 13)
				 +   WORK[8 ] + Bits.rotateLeft(WORK[9 ], 16)
				 +   WORK[10] + Bits.rotateLeft(WORK[11], 19)
				 +   WORK[12] + Bits.rotateLeft(WORK[13], 23)
				 +   WORK[14] + Bits.rotateLeft(WORK[15], 27)
				 + ((WORK[16] >>> 1) ^ WORK[16]) + ((WORK[17] >>> 2) ^ WORK[17])
				 + ((Bits.rotateLeft(IN[2 ], 3) + Bits.rotateLeft(IN[5], 6)
			     -   Bits.rotateLeft(IN[12], 13)
			     + (18 * 0x05555555)) ^ STATE[9]));
		WORK[19] =  (WORK[3 ] + Bits.rotateLeft(WORK[4 ], 3 )
				 +   WORK[5 ] + Bits.rotateLeft(WORK[6 ], 7 )
				 +   WORK[7 ] + Bits.rotateLeft(WORK[8 ], 13)
				 +   WORK[9 ] + Bits.rotateLeft(WORK[10], 16)
				 +   WORK[11] + Bits.rotateLeft(WORK[12], 19)
				 +   WORK[13] + Bits.rotateLeft(WORK[14], 23)
				 +   WORK[15] + Bits.rotateLeft(WORK[16], 27)
				 + ((WORK[17] >>> 1) ^ WORK[17]) + ((WORK[18] >>> 2) ^ WORK[18])
				 + ((Bits.rotateLeft(IN[3 ], 4) + Bits.rotateLeft(IN[6], 7)
			     -   Bits.rotateLeft(IN[13], 14)
			     + (19 * 0x05555555)) ^ STATE[10]));
		WORK[20] =  (WORK[4 ] + Bits.rotateLeft(WORK[5 ], 3 )
				 +   WORK[6 ] + Bits.rotateLeft(WORK[7 ], 7 ) 
				 +   WORK[8 ] + Bits.rotateLeft(WORK[9 ], 13)
				 +   WORK[10] + Bits.rotateLeft(WORK[11], 16)
				 +   WORK[12] + Bits.rotateLeft(WORK[13], 19)
				 +   WORK[14] + Bits.rotateLeft(WORK[15], 23)
				 +   WORK[16] + Bits.rotateLeft(WORK[17], 27)
				 + ((WORK[18] >>> 1) ^ WORK[18]) + ((WORK[19] >>> 2) ^ WORK[19])
				 + ((Bits.rotateLeft(IN[4 ], 5) + Bits.rotateLeft(IN[7], 8)
			     -   Bits.rotateLeft(IN[14], 15)
			     + (20 * 0x05555555)) ^ STATE[11]));
		WORK[21] =  (WORK[5 ] + Bits.rotateLeft(WORK[6 ], 3 )
				 +   WORK[7 ] + Bits.rotateLeft(WORK[8 ], 7 )
				 +   WORK[9 ] + Bits.rotateLeft(WORK[10], 13)
				 +   WORK[11] + Bits.rotateLeft(WORK[12], 16)
				 +   WORK[13] + Bits.rotateLeft(WORK[14], 19)
				 +   WORK[15] + Bits.rotateLeft(WORK[16], 23)
				 +   WORK[17] + Bits.rotateLeft(WORK[18], 27)
				 + ((WORK[19] >>> 1) ^ WORK[19]) + ((WORK[20] >>> 2) ^ WORK[20])
				 + ((Bits.rotateLeft(IN[5 ], 6) + Bits.rotateLeft(IN[8], 9)
			     -   Bits.rotateLeft(IN[15], 16)
			     + (21 * 0x05555555)) ^ STATE[12]));
		WORK[22] =  (WORK[6 ] + Bits.rotateLeft(WORK[7 ], 3 )
				 +   WORK[8 ] + Bits.rotateLeft(WORK[9 ], 7 )
				 +   WORK[10] + Bits.rotateLeft(WORK[11], 13)
				 +   WORK[12] + Bits.rotateLeft(WORK[13], 16)
				 +   WORK[14] + Bits.rotateLeft(WORK[15], 19)
				 +   WORK[16] + Bits.rotateLeft(WORK[17], 23)
				 +   WORK[18] + Bits.rotateLeft(WORK[19], 27)
				 + ((WORK[20] >>> 1) ^ WORK[20]) + ((WORK[21] >>> 2) ^ WORK[21])
				+ ((Bits.rotateLeft(IN[6 ], 7) + Bits.rotateLeft(IN[9], 10)
				 -  Bits.rotateLeft(IN[0 ], 1)
				 + (22 * 0x05555555)) ^ STATE[13]));
		WORK[23] =  (WORK[7 ] + Bits.rotateLeft(WORK[8 ], 3 )
				 +   WORK[9 ] + Bits.rotateLeft(WORK[10], 7 )
				 +   WORK[11] + Bits.rotateLeft(WORK[12], 13)
				 +   WORK[13] + Bits.rotateLeft(WORK[14], 16)
				 +   WORK[15] + Bits.rotateLeft(WORK[16], 19)
				 +   WORK[17] + Bits.rotateLeft(WORK[18], 23)
				 +   WORK[19] + Bits.rotateLeft(WORK[20], 27)
				 + ((WORK[21] >>> 1) ^ WORK[21]) + ((WORK[22] >>> 2) ^ WORK[22])
				 + ((Bits.rotateLeft(IN[7 ], 8) + Bits.rotateLeft(IN[10], 11)
				 -   Bits.rotateLeft(IN[1 ], 2)
				 + (23 * 0x05555555)) ^ STATE[14]));
		WORK[24] =  (WORK[8 ] + Bits.rotateLeft(WORK[9 ], 3 )
				 +   WORK[10] + Bits.rotateLeft(WORK[11], 7 )
				 +   WORK[12] + Bits.rotateLeft(WORK[13], 13)
				 +   WORK[14] + Bits.rotateLeft(WORK[15], 16)
				 +   WORK[16] + Bits.rotateLeft(WORK[17], 19)
				 +   WORK[18] + Bits.rotateLeft(WORK[19], 23)
				 +   WORK[20] + Bits.rotateLeft(WORK[21], 27)
				 + ((WORK[22] >>> 1) ^ WORK[22]) + ((WORK[23] >>> 2) ^ WORK[23])
				 + ((Bits.rotateLeft(IN[8 ], 9) + Bits.rotateLeft(IN[11], 12)
				 -   Bits.rotateLeft(IN[2 ], 3)
				 + (24 * 0x05555555)) ^ STATE[15]));
		WORK[25] =  (WORK[9] + Bits.rotateLeft(WORK[10], 3)
				 +   WORK[11] + Bits.rotateLeft(WORK[12], 7)
				 +   WORK[13] + Bits.rotateLeft(WORK[14], 13)
				 +   WORK[15] + Bits.rotateLeft(WORK[16], 16)
				 +   WORK[17] + Bits.rotateLeft(WORK[18], 19)
				 +   WORK[19] + Bits.rotateLeft(WORK[20], 23)
				 +   WORK[21] + Bits.rotateLeft(WORK[22], 27)
				 + ((WORK[23] >>> 1) ^ WORK[23]) + ((WORK[24] >>> 2) ^ WORK[24])
				 + ((Bits.rotateLeft(IN[9 ], 10) + Bits.rotateLeft(IN[12], 13)
				 -   Bits.rotateLeft(IN[3 ], 4)
				 + (25 * 0x05555555)) ^ STATE[0]));
		WORK[26] =  (WORK[10] + Bits.rotateLeft(WORK[11], 3 )
				 +   WORK[12] + Bits.rotateLeft(WORK[13], 7 )
				 +   WORK[14] + Bits.rotateLeft(WORK[15], 13)
				 +   WORK[16] + Bits.rotateLeft(WORK[17], 16)
				 +   WORK[18] + Bits.rotateLeft(WORK[19], 19)
				 +   WORK[20] + Bits.rotateLeft(WORK[21], 23)
				 +   WORK[22] + Bits.rotateLeft(WORK[23], 27)
				 + ((WORK[24] >>> 1) ^ WORK[24]) + ((WORK[25] >>> 2) ^ WORK[25])
				 + ((Bits.rotateLeft(IN[10], 11) + Bits.rotateLeft(IN[13], 14)
				 -   Bits.rotateLeft(IN[4 ], 5)
				 + (26 * 0x05555555)) ^ STATE[1]));
		WORK[27] =  (WORK[11] + Bits.rotateLeft(WORK[12], 3 )
				 +   WORK[13] + Bits.rotateLeft(WORK[14], 7 )
				 +   WORK[15] + Bits.rotateLeft(WORK[16], 13)
				 +   WORK[17] + Bits.rotateLeft(WORK[18], 16)
				 +   WORK[19] + Bits.rotateLeft(WORK[20], 19)
				 +   WORK[21] + Bits.rotateLeft(WORK[22], 23)
				 +   WORK[23] + Bits.rotateLeft(WORK[24], 27)
				 + ((WORK[25] >>> 1) ^ WORK[25]) + ((WORK[26] >>> 2) ^ WORK[26])
				 + ((Bits.rotateLeft(IN[11], 12) + Bits.rotateLeft(IN[14], 15)
				 -   Bits.rotateLeft(IN[5 ], 6)
				 + (27 * 0x05555555)) ^ STATE[2]));
		WORK[28] =  (WORK[12] + Bits.rotateLeft(WORK[13], 3 )
				 +   WORK[14] + Bits.rotateLeft(WORK[15], 7 )
				 +   WORK[16] + Bits.rotateLeft(WORK[17], 13)
				 +   WORK[18] + Bits.rotateLeft(WORK[19], 16)
				 +   WORK[20] + Bits.rotateLeft(WORK[21], 19)
				 +   WORK[22] + Bits.rotateLeft(WORK[23], 23)
				 +   WORK[24] + Bits.rotateLeft(WORK[25], 27)
				 + ((WORK[26] >>> 1) ^ WORK[26]) + ((WORK[27] >>> 2) ^ WORK[27])
				 + ((Bits.rotateLeft(IN[12], 13) + Bits.rotateLeft(IN[15], 16)
				 -   Bits.rotateLeft(IN[6 ], 7)
				 + (28 * 0x05555555)) ^ STATE[3]));
		WORK[29] =  (WORK[13] + Bits.rotateLeft(WORK[14], 3 )
				 +   WORK[15] + Bits.rotateLeft(WORK[16], 7 )
				 +   WORK[17] + Bits.rotateLeft(WORK[18], 13)
				 +   WORK[19] + Bits.rotateLeft(WORK[20], 16)
				 +   WORK[21] + Bits.rotateLeft(WORK[22], 19)
				 +   WORK[23] + Bits.rotateLeft(WORK[24], 23)
				 +   WORK[25] + Bits.rotateLeft(WORK[26], 27)
				 + ((WORK[27] >>> 1) ^ WORK[27]) + ((WORK[28] >>> 2) ^ WORK[28])
				 + ((Bits.rotateLeft(IN[13], 14) + Bits.rotateLeft(IN[0], 1)
				 -   Bits.rotateLeft(IN[7 ], 8)
				 + (29 * 0x05555555)) ^ STATE[4]));
		WORK[30] =  (WORK[14] + Bits.rotateLeft(WORK[15], 3 )
				 +   WORK[16] + Bits.rotateLeft(WORK[17], 7 )
				 +   WORK[18] + Bits.rotateLeft(WORK[19], 13)
				 +   WORK[20] + Bits.rotateLeft(WORK[21], 16)
				 +   WORK[22] + Bits.rotateLeft(WORK[23], 19)
				 +   WORK[24] + Bits.rotateLeft(WORK[25], 23)
				 +   WORK[26] + Bits.rotateLeft(WORK[27], 27)
				 + ((WORK[28] >>> 1) ^ WORK[28]) + ((WORK[29] >>> 2) ^ WORK[29])
				 + ((Bits.rotateLeft(IN[14], 15) + Bits.rotateLeft(IN[1], 2)
				 -   Bits.rotateLeft(IN[8 ], 9)
				 + (30 * 0x05555555)) ^ STATE[5]));
		WORK[31] =  (WORK[15] + Bits.rotateLeft(WORK[16], 3 )
				 +   WORK[17] + Bits.rotateLeft(WORK[18], 7 )
				 +   WORK[19] + Bits.rotateLeft(WORK[20], 13)
				 +   WORK[21] + Bits.rotateLeft(WORK[22], 16)
				 +   WORK[23] + Bits.rotateLeft(WORK[24], 19)
				 +   WORK[25] + Bits.rotateLeft(WORK[26], 23)
				 +   WORK[27] + Bits.rotateLeft(WORK[28], 27)
				 + ((WORK[29] >>> 1) ^ WORK[29]) + ((WORK[30] >>> 2) ^ WORK[30])
				 + ((Bits.rotateLeft(IN[15], 16) + Bits.rotateLeft(IN[2], 3)
				 -   Bits.rotateLeft(IN[9 ], 10)
				 + (31 * 0x05555555)) ^ STATE[6]));
		int xl = WORK[16] ^ WORK[17] ^ WORK[18] ^ WORK[19]
			   ^ WORK[20] ^ WORK[21] ^ WORK[22] ^ WORK[23];
		
		int xh = xl ^ WORK[24] ^ WORK[25] ^ WORK[26] ^ WORK[27]
			        ^ WORK[28] ^ WORK[29] ^ WORK[30] ^ WORK[31];
		STATE[0 ] = ((xh <<  5 ) ^ (WORK[16] >>> 5 ) ^ IN[0]) + (xl ^ WORK[24] ^ WORK[0]);
		STATE[1 ] = ((xh >>> 7 ) ^ (WORK[17] <<  8 ) ^ IN[1]) + (xl ^ WORK[25] ^ WORK[1]);
		STATE[2 ] = ((xh >>> 5 ) ^ (WORK[18] <<  5 ) ^ IN[2]) + (xl ^ WORK[26] ^ WORK[2]);
		STATE[3 ] = ((xh >>> 1 ) ^ (WORK[19] <<  5 ) ^ IN[3]) + (xl ^ WORK[27] ^ WORK[3]);
		STATE[4 ] = ((xh >>> 3 ) ^ (WORK[20] <<  0 ) ^ IN[4]) + (xl ^ WORK[28] ^ WORK[4]);
		STATE[5 ] = ((xh <<  6 ) ^ (WORK[21] >>> 6 ) ^ IN[5]) + (xl ^ WORK[29] ^ WORK[5]);
		STATE[6 ] = ((xh >>> 4 ) ^ (WORK[22] <<  6 ) ^ IN[6]) + (xl ^ WORK[30] ^ WORK[6]);
		STATE[7 ] = ((xh >>> 11) ^ (WORK[23] <<  2 ) ^ IN[7])
			+ (xl ^ WORK[31] ^ WORK[7]);
		STATE[8 ] = Bits.rotateLeft(STATE[4], 9 ) + (xh ^ WORK[24] ^ IN[8 ])
			+ ((xl <<  8) ^ WORK[23] ^ WORK[8]);
		STATE[9 ] = Bits.rotateLeft(STATE[5], 10) + (xh ^ WORK[25] ^ IN[9 ])
			+ ((xl >>> 6) ^ WORK[16] ^ WORK[9]);
		STATE[10] = Bits.rotateLeft(STATE[6], 11) + (xh ^ WORK[26] ^ IN[10])
			+ ((xl <<  6) ^ WORK[17] ^ WORK[10]);
		STATE[11] = Bits.rotateLeft(STATE[7], 12) + (xh ^ WORK[27] ^ IN[11])
			+ ((xl <<  4) ^ WORK[18] ^ WORK[11]);
		STATE[12] = Bits.rotateLeft(STATE[0], 13) + (xh ^ WORK[28] ^ IN[12])
			+ ((xl >>> 3) ^ WORK[19] ^ WORK[12]);
		STATE[13] = Bits.rotateLeft(STATE[1], 14) + (xh ^ WORK[29] ^ IN[13])
			+ ((xl >>> 4) ^ WORK[20] ^ WORK[13]);
		STATE[14] = Bits.rotateLeft(STATE[2], 15) + (xh ^ WORK[30] ^ IN[14])
			+ ((xl >>> 7) ^ WORK[21] ^ WORK[14]);
		STATE[15] = Bits.rotateLeft(STATE[3], 16) + (xh ^ WORK[31] ^ IN[15])
			+ ((xl >>> 2) ^ WORK[22] ^ WORK[15]);
	}
	
	public void processNext(byte[] bytes, int offset)
	{
		this.processNext(bytes, offset, true);
	}

	public void processNext(byte[] bytes, int offset, boolean count)
	{
		if(count)
			counter.add(512);
		Bits.bytesToInts(bytes, offset, BLOCK, 0, 16);
		compress(BLOCK);
	}

	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		counter.add(pos << 3);
		Arrays.fill(remaining, pos, 64, (byte) 0);
		remaining[pos] = (byte) 0x80;
		if(pos >= 56)
		{
			processNext(remaining, 0);
			Arrays.fill(remaining, (byte) 0);
		}
		Bits.intToBytes(counters[0], remaining, 56);
		Bits.intToBytes(counters[1], remaining, 60);
		processNext(remaining, 0);
		System.arraycopy(STATE, 0, BLOCK, 0, 16);
		System.arraycopy(last, 0, STATE, 0, 16);
		compress(BLOCK);
		output(out, start);
		reset();
	}
	
	public BMW_32State getState()
	{
		return new BMW_32State(this);
	}

	public void updateState(BMW_32State state)
	{
		state.update(this);
	}

	public void loadCustom(BMW_32State state)
	{
		System.arraycopy(state.counters, 0, this.counters, 0, 2);
		System.arraycopy(state.state, 0, this.STATE, 0, 16);
	}
	
	public static final BMW_32StateFactory sfactory = new BMW_32StateFactory();
	
	protected static final class BMW_32State extends MerkleState<BMW_32State, BMW_Base_32<? extends BMW_Base_32<?>>>
	{

		protected int[] state;
		protected int[] counters;
		
		public BMW_32State(BMW_Base_32<? extends BMW_Base_32<?>> hash) 
		{
			super(hash);
		}
		
		public BMW_32State(byte[] bytes, int pos)
		{
			super(bytes, pos);
		}

		public Factory<BMW_32State> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.BMW32STATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeInts(state);
			os.writeInts(counters);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.intsToBytes(state, 0, bytes, start, 16); start += 64;
			Bits.intsToBytes(counters, 0, bytes, start, 2);
		}

		protected void addCustom(IIncomingStream is) throws IOException
		{
			state = is.readInts(16);
			counters = is.readInts(2);
		}
		
		protected void addCustom(byte[] bytes, int start)
		{
			state = new int[16];
			counters = new int[2];
			Bits.bytesToInts(bytes, start, state, 0, 16); start += 64;
			Bits.bytesToInts(bytes, start, counters, 0, 2);
		}

		protected void addCustom(BMW_Base_32<? extends BMW_Base_32<?>> hash)
		{
			state = ArrayUtil.copy(hash.STATE);
			counters = ArrayUtil.copy(hash.counters);
		}

		protected void updateCustom(BMW_Base_32<? extends BMW_Base_32<?>> hash)
		{
			System.arraycopy(this.state, 0, hash.STATE, 0, 16);
			System.arraycopy(this.counters, 0, hash.counters, 0, 2);
		}

		protected void eraseCustom()
		{
			Arrays.fill(state, 0);
			Arrays.fill(counters, 0);
			state = null;
			counters = null;
		}

		protected boolean compareCustom(BMW_32State state)
		{	
			return ArrayUtil.equals(counters, state.counters) && ArrayUtil.equals(this.state, state.state);
		}

		protected int customSize()
		{
			return 72;
		}
		
	}
	
	protected static final class BMW_32StateFactory extends MerkleStateFactory<BMW_32State, BMW_Base_32<? extends BMW_Base_32<?>>>
	{

		protected BMW_32StateFactory() 
		{
			super(BMW_32State.class, 64);
		}

		protected BMW_32State construct(byte[] bytes, int pos)
		{
			return new BMW_32State(bytes, pos);
		}
		
	}

}
