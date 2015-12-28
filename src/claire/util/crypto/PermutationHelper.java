package claire.util.crypto;

public class PermutationHelper {
	
	private static final long[] POSITIONS = 
		{
			                   1L,                    2L,                   
			                   4L,                    8L, 
			                  16L,                   32L, 
		                      64L,                  128L,  
		                     256L,                  512L,
		                    1024L,                 2048L, 
			                4096L,                 8192L,
			               16384L,                32768L,               
			               65536L,               131072L,  
			              262144L,               524288L,             
			             1048576L,              2097152L,
			             4194304L,              8388608L,   
			            16777216L,             33554432L,            
			            67108864L,            134217728L,           
			           268435456L,            536870912L, 
			          1073741824L,           2147483648L,         
			          4294967296L,           8589934592L,
			         17179869184L,          34359738368L, 
			         68719476736L,         137438953472L,        
			        274877906944L,         549755813888L,
			       1099511627776L,        2199023255552L, 
			       4398046511104L,        8796093022208L,
			      17592186044416L,       35184372088832L,
			      70368744177664L,      140737488355328L, 
			     281474976710656L,      562949953421312L,
			    1125899906842624L,     2251799813685248L,   
			    4503599627370496L,     9007199254740992L, 
			   18014398509481984L,    36028797018963968L, 
			   72057594037927936L,   144115188075855872L, 
			  288230376151711744L,   576460752303423488L, 
			 1152921504606846976L,  2305843009213693952L, 
			 4611686018427387904L, -9223372036854775808L
		};
	
	public static final long permute(long in, byte[] table)
	{
		long l = 0;
		for(int i = table.length; i > 1; l <<= 1) 
			if((in & POSITIONS[table[--i]]) != 0) 
				l++;	
		if((in & POSITIONS[table[0]]) != 0) 
			l++;	
		return l;
	}
	
	public static final class BigEndian
	{
		private static final long[] BEPOSITIONS = 
			{
								 128L, 					 64L, 
								  32L,                   16L, 
								   8L,                    4L, 
								   2L,                    1L, 
							   32768L,                16384L, 
							    8192L,                 4096L, 
							    2048L,                 1024L, 
							     512L,                  256L, 
							 8388608L, 				4194304L, 
							 2097152L, 				1048576L, 
							  524288L, 				 262144L, 
							  131072L, 				  65536L, 
						  2147483648L, 			 1073741824L, 
						   536870912L, 		      268435456L, 
						   134217728L,			   67108864L, 
						    33554432L, 			   16777216L, 
						549755813888L, 		   274877906944L, 
						137438953472L,		    68719476736L, 
						 34359738368L, 		    17179869184L, 
						  8589934592L, 		     4294967296L, 
				     140737488355328L, 		 70368744177664L, 
				      35184372088832L, 		 17592186044416L, 
				       8796093022208L, 		  4398046511104L, 
				       2199023255552L, 		  1099511627776L, 
				   36028797018963968L, 	  18014398509481984L, 
				    9007199254740992L, 	   4503599627370496L, 
				    2251799813685248L, 	   1125899906842624L, 
				     562949953421312L, 		281474976710656L, 
				-9223372036854775808L,  4611686018427387904L, 
				 2305843009213693952L,  1152921504606846976L, 
				  576460752303423488L,   288230376151711744L, 
				  144115188075855872L,    72057594037927936L,
			};
		
		private static final long[] BEBEPOSITIONS = 
			{
			72057594037927936L, 144115188075855872L, 
			288230376151711744L, 576460752303423488L, 
			1152921504606846976L, 2305843009213693952L, 
			4611686018427387904L, -9223372036854775808L, 
			281474976710656L, 562949953421312L, 
			1125899906842624L, 2251799813685248L, 
			4503599627370496L, 9007199254740992L, 
			18014398509481984L, 36028797018963968L, 
			1099511627776L, 2199023255552L, 
			4398046511104L, 8796093022208L, 
			17592186044416L, 35184372088832L, 
			70368744177664L, 140737488355328L, 
			4294967296L, 8589934592L, 
			17179869184L, 34359738368L, 
			68719476736L, 137438953472L, 
			274877906944L, 549755813888L, 
			16777216L, 33554432L, 
			67108864L, 134217728L, 
			268435456L, 536870912L, 
			1073741824L, 2147483648L, 
			65536L, 131072L, 
			262144L, 524288L, 
			1048576L, 2097152L, 
			4194304L, 8388608L, 
			256L, 512L, 
			1024L, 2048L, 
			4096L, 8192L, 
			16384L, 32768L, 
			1L, 2L, 
			4L, 8L, 
			16L, 32L, 
			64L, 128L, 		
			};
		
		public static final long permute(long in, byte[] table)
		{
			long l = 0;
			for(int i = table.length; i > 1; l <<= 1) 
				if((in & POSITIONS[table[--i] - 1]) != 0) 
					l++;	
			if((in & POSITIONS[table[0] - 1]) != 0) 
				l++;	
			return l;
		}
	}

}
