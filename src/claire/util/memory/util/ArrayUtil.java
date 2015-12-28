package claire.util.memory.util;

import java.lang.reflect.Array;
import java.util.Arrays;

import claire.util.standards.IDeepClonable;
import claire.util.standards.IUUID;

public final class ArrayUtil {
	
	@SuppressWarnings("unchecked")
	public static <T> T[] upsize(T[] array, int add)
	{
		final T[] copy = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + add);
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}
	
	public static boolean[] upsize(boolean[] array, int add)
	{
		final boolean[] copy = new boolean[array.length + add];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}
	
	public static byte[] upsize(byte[] array, int add)
	{
		final byte[] copy = new byte[array.length + add];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}
	
	public static short[] upsize(short[] array, int add)
	{
		final short[] copy = new short[array.length + add];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}
	
	public static char[] upsize(char[] array, int add)
	{
		final char[] copy = new char[array.length + add];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}
	
	public static int[] upsize(int[] array, int add)
	{
		final int[] copy = new int[array.length + add];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}
	
	public static long[] upsize(long[] array, int add)
	{
		final long[] copy = new long[array.length + add];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}
	
	public static boolean[] concat(boolean[] ... arrays)
	{
		int size = 0;
		for(boolean[] arr : arrays)
			size += arr.length;
		boolean[] n = new boolean[size];
		int pos = 0;
		for(boolean[] arr : arrays) {
			pos += arr.length;
			System.arraycopy(arr, 0, n, pos, arr.length);
		}
		return n;
	}
	
	public static byte[] concat(byte[] ... arrays)
	{
		int size = 0;
		for(byte[] arr : arrays)
			size += arr.length;
		byte[] n = new byte[size];
		int pos = 0;
		for(byte[] arr : arrays) {
			pos += arr.length;
			System.arraycopy(arr, 0, n, pos, arr.length);
		}
		return n;
	}
	
	public static short[] concat(short[] ... arrays)
	{
		int size = 0;
		for(short[] arr : arrays)
			size += arr.length;
		short[] n = new short[size];
		int pos = 0;
		for(short[] arr : arrays) {
			pos += arr.length;
			System.arraycopy(arr, 0, n, pos, arr.length);
		}
		return n;
	}
	
	public static char[] concat(char[] ... arrays)
	{
		int size = 0;
		for(char[] arr : arrays)
			size += arr.length;
		char[] n = new char[size];
		int pos = 0;
		for(char[] arr : arrays) {
			pos += arr.length;
			System.arraycopy(arr, 0, n, pos, arr.length);
		}
		return n;
	}
	
	public static int[] concat(int[] ... arrays)
	{
		int size = 0;
		for(int[] arr : arrays)
			size += arr.length;
		int[] n = new int[size];
		int pos = 0;
		for(int[] arr : arrays) {
			pos += arr.length;
			System.arraycopy(arr, 0, n, pos, arr.length);
		}
		return n;
	}
	
	public static long[] concat(long[] ... arrays)
	{
		int size = 0;
		for(long[] arr : arrays)
			size += arr.length;
		long[] n = new long[size];
		int pos = 0;
		for(long[] arr : arrays) {
			pos += arr.length;
			System.arraycopy(arr, 0, n, pos, arr.length);
		}
		return n;
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T[] concat(T[] array1, T[] array2)
	{
		final T[] n = (T[]) Array.newInstance(array1.getClass().getComponentType(), array1.length + array2.length);
		System.arraycopy(array1, 0, n, 0, array1.length);
		System.arraycopy(array2, 0, n, array1.length, array2.length);
		return n;
	}
	
	public static boolean[] concat(boolean[] b, boolean[] b2)
	{
		final boolean[] f = new boolean[b.length + b2.length];
		System.arraycopy(b, 0, f, 0, b.length);
		System.arraycopy(b2, 0, f, b.length, b2.length);
		return f;
	}
	
	public static byte[] concat(byte[] b, byte[] b2)
	{
		final byte[] f = new byte[b.length + b2.length];
		System.arraycopy(b, 0, f, 0, b.length);
		System.arraycopy(b2, 0, f, b.length, b2.length);
		return f;
	}
	
	public static short[] concat(short[] b, short[] b2)
	{
		final short[] f = new short[b.length + b2.length];
		System.arraycopy(b, 0, f, 0, b.length);
		System.arraycopy(b2, 0, f, b.length, b2.length);
		return f;
	}
	
	public static char[] concat(char[] b, char[] b2)
	{
		final char[] f = new char[b.length + b2.length];
		System.arraycopy(b, 0, f, 0, b.length);
		System.arraycopy(b2, 0, f, b.length, b2.length);
		return f;
	}
	
	public static int[] concat(int[] b, int[] b2)
	{
		final int[] f = new int[b.length + b2.length];
		System.arraycopy(b, 0, f, 0, b.length);
		System.arraycopy(b2, 0, f, b.length, b2.length);
		return f;
	}
	
	public static long[] concat(long[] l, long[] l2)
	{
		final long[] f = new long[l.length + l2.length];
		System.arraycopy(l, 0, f, 0, l.length);
		System.arraycopy(l2, 0, f, l.length, l2.length);
		return f;
	}
	
	public static boolean[] toPrimitive(Boolean[] l)
	{
		boolean[] n = new boolean[l.length];
		for(int i = 0; i < l.length; i++)
			n[i] = l[i];
		return n;
	}
	
	public static byte[] toPrimitive(Byte[] l)
	{
		byte[] n = new byte[l.length];
		for(int i = 0; i < l.length; i++)
			n[i] = l[i];
		return n;
	}
	
	public static short[] toPrimitive(Short[] l)
	{
		short[] n = new short[l.length];
		for(int i = 0; i < l.length; i++)
			n[i] = l[i];
		return n;
	}
	
	public static char[] toPrimitive(Character[] l)
	{
		char[] n = new char[l.length];
		for(int i = 0; i < l.length; i++)
			n[i] = l[i];
		return n;
	}
	
	public static int[] toPrimitive(Integer[] l)
	{
		int[] n = new int[l.length];
		for(int i = 0; i < l.length; i++)
			n[i] = l[i];
		return n;
	}
	
	
	public static long[] toPrimitive(Long[] l)
	{
		long[] n = new long[l.length];
		for(int i = 0; i < l.length; i++)
			n[i] = l[i];
		return n;
	}

	public static Boolean[] toHeap(boolean[] arr)
	{
		Boolean[] n = new Boolean[arr.length];
		for(int i = 0; i < arr.length; i++)
			n[i] = arr[i];
		return n;
	}
	
	public static Byte[] toHeap(byte[] arr)
	{
		Byte[] n = new Byte[arr.length];
		for(int i = 0; i < arr.length; i++)
			n[i] = arr[i];
		return n;
	}
	
	public static Short[] toHeap(short[] arr)
	{
		Short[] n = new Short[arr.length];
		for(int i = 0; i < arr.length; i++)
			n[i] = arr[i];
		return n;
	}
	
	public static Character[] toHeap(char[] arr)
	{
		Character[] n = new Character[arr.length];
		for(int i = 0; i < arr.length; i++)
			n[i] = arr[i];
		return n;
	}
	
	public static Integer[] toHeap(int[] arr)
	{
		Integer[] n = new Integer[arr.length];
		for(int i = 0; i < arr.length; i++)
			n[i] = arr[i];
		return n;
	}
	
	public static Long[] toHeap(long[] arr)
	{
		Long[] n = new Long[arr.length];
		for(int i = 0; i < arr.length; i++)
			n[i] = arr[i];
		return n;
	}
	
	public static void shiftRight(byte[] i, int places)
	{
		System.arraycopy(i, 0, i, places, i.length - places);
		Arrays.fill(i, 0, places, (byte) 0); 
	}
	
	public static void shiftRight(short[] i, int places)
	{
		System.arraycopy(i, 0, i, places, i.length - places);
		Arrays.fill(i, 0, places, (short) 0); 
	}
	
	public static void shiftRight(char[] i, int places)
	{
		System.arraycopy(i, 0, i, places, i.length - places);
		Arrays.fill(i, 0, places, (char) 0); 
	}
	
	public static void shiftRight(int[] i, int places)
	{
		System.arraycopy(i, 0, i, places, i.length - places);
		Arrays.fill(i, 0, places, 0); 
	}
	
	public static void shiftRight(long[] i, int places)
	{
		System.arraycopy(i, 0, i, places, i.length - places);
		Arrays.fill(i, 0, places, 0); 
	}
	
	public static void shiftRight(Object[] i, int places)
	{
		System.arraycopy(i, 0, i, places, i.length - places);
		Arrays.fill(i, 0, places, null); 
	}
	
	public static void shiftLeft(byte[] i, int places)
	{
		System.arraycopy(i, places, i, 0, i.length - places);
		Arrays.fill(i, i.length - places, i.length, (byte) 0);
	}
	
	public static void shiftLeft(short[] i, int places)
	{
		System.arraycopy(i, places, i, 0, i.length - places);
		Arrays.fill(i, i.length - places, i.length, (short) 0);
	}
	
	public static void shiftLeft(char[] i, int places)
	{
		System.arraycopy(i, places, i, 0, i.length - places);
		Arrays.fill(i, i.length - places, i.length, (char) 0);
	}
	
	public static void shiftLeft(int[] i, int places)
	{
		System.arraycopy(i, places, i, 0, i.length - places);
		Arrays.fill(i, i.length - places, i.length, 0);
	}
	
	public static void shiftLeft(long[] i, int places)
	{
		System.arraycopy(i, places, i, 0, i.length - places);
		Arrays.fill(i, i.length - places, i.length, 0);
	}
	
	public static void shiftLeft(Object[] i, int places)
	{
		System.arraycopy(i, places, i, 0, i.length - places);
		Arrays.fill(i, i.length - places, i.length, null);
	}
	
	public static int[] terminateEndingZeroes(int[] in)
	{
		return terminateEndingZeroes(in, 0);
	}
	
	public static int[] terminateEndingZeroes(int[] in, int skip)
	{
		int t = in.length;
		for(int i = in.length - 1 - skip; i >= 0; i--)
			if(in[i] == 0)
				t--;
			else
				break;
		if(t != in.length) {
			int[] n = new int[t];
			System.arraycopy(in, 0, n, 0, t);
			return n;
		} else
			return in;
		
	}
	
	public static int[] terminateLeadingZeroes(int[] in)
	{
		int i = 0;
		for(; i < in.length;)
			if(in[i] != 0)
				break;
			else
				i++;
		int[] n = new int[in.length - i];
		System.arraycopy(in, i, n, 0, n.length);
		return n;
	}

	public static <T> void  REVERSE(T[] t)
	{
		for(int i = 0; i < t.length; i++)
		{
			T t1 = t[i];
			t[i] = t[t.length - i];
			t[t.length - i] = t1;
		}
	}
	
	public static <T> T[] ENFORCE_MAXIMUM(T[] in, int length)
	{
		if(in.length == length)
			return in;
		@SuppressWarnings("unchecked")
		final T[] copy = (T[]) Array.newInstance(in.getClass().getComponentType(), length);
		System.arraycopy(in, 0, copy, 0, length);
		return copy;
	}
	
	public static int[] ENFORCE_MAXIMUM(int[] in, int length)
	{
		if(in.length == length)
			return in;
		final int[] copy = new int[length];
		System.arraycopy(in, 0, copy, 0, length);
		return copy;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends IDeepClonable<T>> T[] deepCopy(T[] arr)
	{	
		final T[] copy = (T[]) Array.newInstance(arr.getClass().getComponentType(), arr.length);
		for(int i = 0; i < arr.length; i++)
			copy[i] = arr[i].createDeepClone();
		return copy;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] copy(T[] arr)
	{
		final T[] copy = (T[]) Array.newInstance(arr.getClass().getComponentType(), arr.length);
		System.arraycopy(arr, 0, copy, 0, arr.length);
		return copy;
	}
	
	public static byte[] copy(byte[] in)
	{
		byte[] n = new byte[in.length];
		System.arraycopy(in, 0, n, 0, in.length);
		return n;
	}
	
	public static short[] copy(short[] in)
	{
		short[] n = new short[in.length];
		System.arraycopy(in, 0, n, 0, in.length);
		return n;
	}
	
	public static char[] copy(char[] in)
	{
		char[] n = new char[in.length];
		System.arraycopy(in, 0, n, 0, in.length);
		return n;
	}
	
	public static int[] copy(int[] in)
	{
		int[] n = new int[in.length];
		System.arraycopy(in, 0, n, 0, in.length);
		return n;
	}
	
	public static long[] copy(long[] in)
	{
		long[] n = new long[in.length];
		System.arraycopy(in, 0, n, 0, in.length);
		return n;
	}
	
	public static <T> T[] subArr(T[] arr, int s, int e)
	{
		int length = e - s;
		@SuppressWarnings("unchecked")
		final T[] n = (T[]) Array.newInstance(arr.getClass(), length);
		if(length == 0)
			return n;
		System.arraycopy(arr, s, n, 0, length);
		return n;
	}
	
	public static byte[] subArr(byte[] arr, int s, int e)
	{
		int length = e - s;
		final byte[] n = new byte[length];
		if(length == 0)
			return n;
		System.arraycopy(arr, s, n, 0, length);
		return n;
	}
	
	public static short[] subArr(short[] arr, int s, int e)
	{
		int length = e - s;
		final short[] n = new short[length];
		if(length == 0)
			return n;
		System.arraycopy(arr, s, n, 0, length);
		return n;
	}
	
	public static char[] subArr(char[] arr, int s, int e)
	{
		int length = e - s;
		final char[] n = new char[length];
		if(length == 0)
			return n;
		System.arraycopy(arr, s, n, 0, length);
		return n;
	}
	
	public static int[] subArr(int[] arr, int s, int e)
	{
		int length = e - s;
		final int[] n = new int[length];
		if(length == 0)
			return n;
		System.arraycopy(arr, s, n, 0, length);
		return n;
	}
	
	public static long[] subArr(long[] arr, int s, int e)
	{
		int length = e - s;
		final long[] n = new long[length];
		if(length == 0)
			return n;
		System.arraycopy(arr, s, n, 0, length);
		return n;
	}
	
	public static boolean equals(boolean[] arr1, boolean[] arr2)
	{
		if(arr1.length == arr2.length) {
			for(int i = 0; i < arr1.length; i++) 
				if(arr1[i] ^ arr2[i])
					return false;
			return true;
		} else
			return false;
	}
	
	public static boolean equals(byte[] arr1, byte[] arr2)
	{
		if(arr1.length == arr2.length) {
			for(int i = 0; i < arr1.length; i++) 
				if(arr1[i] != arr2[i])
					return false;
			return true;
		} else
			return false;
	}
	
	public static boolean equals(short[] arr1, short[] arr2)
	{
		if(arr1.length == arr2.length) {
			for(int i = 0; i < arr1.length; i++) 
				if(arr1[i] != arr2[i])
					return false;
			return true;
		} else
			return false;
	}
	
	public static boolean equals(char[] arr1, char[] arr2)
	{
		if(arr1.length == arr2.length) {
			for(int i = 0; i < arr1.length; i++) 
				if(arr1[i] != arr2[i])
					return false;
			return true;
		} else
			return false;
	}
	
	public static boolean equals(int[] arr1, int[] arr2)
	{
		if(arr1.length == arr2.length) {
			for(int i = 0; i < arr1.length; i++) 
				if(arr1[i] != arr2[i])
					return false;
			return true;
		} else
			return false;
	}
	
	public static boolean equals(long[] arr1, long[] arr2)
	{
		if(arr1.length == arr2.length) {
			for(int i = 0; i < arr1.length; i++) 
				if(arr1[i] != arr2[i])
					return false;
			return true;
		} else
			return false;
	}
	
	public static boolean equals(IUUID<?>[] arr1, IUUID<?>[] arr2)
	{
		if(arr1.length == arr2.length) {
			for(int i = 0; i < arr1.length; i++)
				if(!arr1[i].equals(arr2[i]))
					return false;
			return true;
		} else
			return false;
	}
	
	public static void empty(Object[] array)
	{
		Arrays.fill(array, null);
	}
	
	public static void empty(boolean[] array)
	{
		Arrays.fill(array, false);
	}
	
	public static void empty(byte[] array)
	{
		Arrays.fill(array, (byte) 0);
	}
	
	public static void empty(short[] array)
	{
		Arrays.fill(array, (short) 0);
	}
	
	public static void empty(char[] array)
	{
		Arrays.fill(array, (char) 0);
	}
	
	public static void empty(int[] array)
	{
		Arrays.fill(array, 0);
	}
	
	public static void empty(long[] array)
	{
		Arrays.fill(array, 0);
	}

}
