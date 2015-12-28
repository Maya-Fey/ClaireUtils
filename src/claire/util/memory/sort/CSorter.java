package claire.util.memory.sort;

import claire.util.memory.array.CArray;

public class CSorter<T> {
	
	private final Getter<Long, T> getter;
	private CArray<T> array;
	
	public CSorter(Getter<Long, T> getter)
	{
		this.getter = getter;
	}
	
	public CArray<T> sort(CArray<T> array)
	{
		this.array = array;
		quickSort(0, this.array.size() - 1);
		return this.array;
	}
	
	private final void quickSort(int lower, int higher)
	{
		int i = lower;
        int j = higher;
        long pivot = getter.get(array.get((higher + lower)/2));
        while (i <= j) {
            while (getter.get(array.get(i)) < pivot) {
                i++;
            }
            while (getter.get(array.get(j)) > pivot) {
                j--;
            }
            if (i <= j) {
                array.swap(i, j);
                i++;
                j--;
            }
        }
        if (lower < j)
            quickSort(lower, j);
        if (i < higher)
            quickSort(i, higher);
	}

}
