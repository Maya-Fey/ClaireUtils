package claire.util.memory.sort;

public class Sorter<T> {
	
	private final Getter<Integer, T> getter;
	private T[] array;
	
	public Sorter(Getter<Integer, T> getter)
	{
		this.getter = getter;
	}
	
	@SuppressWarnings({ "unchecked" })
	public T[] sort(T ... array)
	{
		this.array = array;
		quickSort(0, this.array.length - 1);
		return this.array;
	}
	
	private final void quickSort(int lower, int higher)
	{
		int i = lower;
        int j = higher;
        int pivot = getter.get(array[(higher + lower)/2]);
        while (i <= j) {
            while (getter.get(array[i]) < pivot) {
                i++;
            }
            while (getter.get(array[j]) > pivot) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j);
                i++;
                j--;
            }
        }
        if (lower < j)
            quickSort(lower, j);
        if (i < higher)
            quickSort(i, higher);
	}
	
	private final void exchangeNumbers(int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

}
