package claire.util.memory.array;

public class D2_1Array {
	
	public static class Integer {
		
		private final int[][] arr;
		private final int length;
		private final int tlength;
		
		public Integer(int[][] arr)
		{
			this.arr = arr;
			this.length = arr[0].length;
			this.tlength = this.length * this.arr.length;
		}
		
		public int get(int pos)
		{
			int pos1 = 0;
			int pos2 = pos;
			while(pos2 >= this.length) {
				pos2 -= this.length;
				pos1++;
			}
			return this.arr[pos1][pos2];
		}
		
		public void set(int pos, int b)
		{
			int pos1 = 0;
			int pos2 = pos;
			while(pos2 >= this.length) {
				pos2 -= this.length;
				pos1++;
			}
			this.arr[pos1][pos2] = b;
		}
		
		public int length()
		{
			return this.tlength;
		}
		
	}
	
	public static class Byte {
		
		private final byte[][] arr;
		private final int length;
		private final int tlength;
		
		public Byte(byte[][] arr)
		{
			this.arr = arr;
			this.length = arr[0].length;
			this.tlength = this.length * this.arr.length;
		}
		
		public byte get(int pos)
		{
			int pos1 = 0;
			int pos2 = pos;
			while(pos2 >= this.length) {
				pos2 -= this.length;
				pos1++;
			}
			return this.arr[pos1][pos2];
		}
		
		public void set(int pos, byte b)
		{
			int pos1 = 0;
			int pos2 = pos;
			while(pos2 >= this.length) {
				pos2 -= this.length;
				pos1++;
			}
			this.arr[pos1][pos2] = b;
		}
		
		public int length()
		{
			return this.tlength;
		}
		
	}
	
	public static class Bool {
		
		private final boolean[][] arr;
		private final int length;
		private final int tlength;
		
		public Bool(boolean[][] arr)
		{
			this.arr = arr;
			this.length = arr[0].length;
			this.tlength = this.length * this.arr.length;
		}
		
		public boolean get(int pos)
		{
			int pos1 = 0;
			int pos2 = pos;
			while(pos2 >= this.length) {
				pos2 -= this.length;
				pos1++;
			}
			return this.arr[pos1][pos2];
		}
		
		public void set(int pos, boolean b)
		{
			int pos1 = 0;
			int pos2 = pos;
			while(pos2 >= this.length) {
				pos2 -= this.length;
				pos1++;
			}
			this.arr[pos1][pos2] = b;
		}
		
		public int length()
		{
			return this.tlength;
		}
		
	}

}
