package prog08;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.math.*;

public class TestSorter<E extends Comparable<E>> {
    public static void main (String[] args) {
	tests(new InsertionSort<Integer>());
	tests(new HeapSort<Integer>());
	tests(new QuickSort<Integer>());
	tests(new MergeSort<Integer>());
    }

    public static void tests (Sorter<Integer> sorter) {
	test(sorter, 100);
	test(sorter, 10000);
	test(sorter, 100000);
	test(sorter, 1000000);
	test(sorter, 1000000);
	test(sorter, 1000000);
	test(sorter, 10000000);
	System.out.println();
    }
  
    public static void test (Sorter<Integer> sorter, int n) {
	if (sorter instanceof InsertionSort && n > 100)
	    n /= 100;

	Integer[] array = new Integer[n];
	Random random = new Random(0);
	for (int i = 0; i < n; i++)
	    array[i] = random.nextInt(n);

	TestSorter<Integer> tester = new TestSorter<Integer>();
	tester.test(sorter, array);
    }

    public void test (Sorter<E> sorter, E[] array) {
	System.out.println(sorter + " on array of length " + array.length);

	if (inOrder(array))
	    System.out.println("array is already sorted!");

	E[] copy = array.clone();
	long time1 = System.nanoTime();
	sorter.sort(copy);
	long time2 = System.nanoTime();

	// EXERCISE
	// Print out number of MICROseconds and the constant.

	System.out.println("Time: " + (time2 - time1) / 1000 + " microseconds");

	System.out.print("Constant is:" + (time2 - time1) / 1000 / sorter.O(array.length) + "\n");

	if (!sameElements(array, copy))
	    System.out.println("sorted array does not have the same elements!");

	if (!inOrder(copy))
	    System.out.println("sorted array is not sorted");

	if (array.length < 100) {
	    print(array);
	    print(copy);
	}
    }

    public void print (E[] array) {
	String s = "";
	for (E e : array)
	    s += e + " ";
	System.out.println(s);
    }

    /** Check if array is nondecreasing. */
    public boolean inOrder (E[] array) {
	// EXERCISE

	for (int i = 0; i < array.length - 1; i++) {
	    if (array[i].compareTo(array[i+1]) > 0)
			return false;
	}

	return true;
    }
 
    /* Check if arrays have the same elements. */
    public boolean sameElements (E[] array1, E[] array2) {
	// EXERCISE
	// If the two arrays have different lengths, return false.

		if (array1.length != array2.length)
	    	return false;
    
	// EXERCISE
	// Create a Map from E to Integer, using the HashMap implementation.

		Map<E, Integer> map = new HashMap<E, Integer>();

	// EXERCISE
	// For each element of the first array, if it is not a key in the
	// map, make it map to 1.  If it is already a key, increment
	// the integer it maps to.

		for (E e : array1) {
	    	if (!map.containsKey(e))
				map.put(e, 1);
	    	else
				map.put(e, map.get(e) + 1);
		}





	// EXERCISE
	// For each element of the second array, if it is not a key in
	// the map, return false.  If it maps to zero, return false.
	// Otherwise, decrement the value that it maps to.

		for (E e : array2) {
	    	if (!map.containsKey(e))
				return false;
	    	if (map.get(e) == 0)
				return false;
	    	map.put(e, map.get(e) - 1);
		}






			    
	return true;
    }
}

class InsertionSort<E extends Comparable<E>> implements Sorter<E> {
    public double O (int n) { return Math.pow(n, 2);}// Use pow for this one.

	public void sort (E[] array) {
		for (int n = 0; n < array.length; n++) {
			E data = array[n];
			int i = n;

	    // EXERCISE
	    // while array[i-1] > data move array[i-1] to array[i] and
	    // decrement i

			while(i>0 && array[i-1].compareTo(data) > 0){
				array[i] = array[i-1];
				i--;
			}



	    array[i] = data;
	}
    }

    public void sort (E[] array, int first, int last) {
	for (int n = 0; n < last - first + 1; n++) {
	    int i = first + n;
	    E data = array[i];

	    // EXERCISE
	    // while array[i-1] > data move array[i-1] to array[i] and
	    // decrement i
	    while (i > first && array[i-1].compareTo(data) > 0) {
		array[i] = array[i-1];
		i--;
	    }

	    array[i] = data;
	}
    }
}

class HeapSort<E extends Comparable<E>> implements Sorter<E> {

    public double O (int n) { return n*Math.log(n); }

    private E[] array;
    private int size;

    public void sort (E[] array) {
	this.array = array;
	this.size = array.length;

	for (int i = parent(array.length - 1); i >= 0; i--)
	    swapDown(i);

	while (size > 1) {
	    swap(0, size-1);
	    size--;
	    swapDown(0);
	}
    }

    public void swapDown (int index) {
	// EXERCISE

	// While the element at index is smaller than one of its children,
	// swap it with its larger child.  Use the helper methods provided
	// below: compare, swap, left, right, and isValid.

	while (isValid(left(index)) && compare(index, left(index)) < 0 || isValid(right(index)) && compare(index, right(index)) < 0) {
	    if (isValid(right(index)) && compare(left(index), right(index)) < 0) {
	    			index = swap(index, right(index));
	    }
		else {
	    			index = swap(index, left(index));
		}
		}

    }

    // index = swap(index, left(index)) or
    // index = swap(index, right(index))
    private int swap (int i, int j) {
	E data = array[i];
	array[i] = array[j];
	array[j] = data;
	return j;
    }

    private int compare (int i, int j) { return array[i].compareTo(array[j]); }
    private int left (int i) { return 2 * i + 1; }
    private int right (int i) { return 2 * i + 2; }
    private int parent (int i) { return (i - 1) / 2; }
    private boolean isValid (int i) { return 0 <= i && i < size; }
}

class QuickSort<E extends Comparable<E>> implements Sorter<E> {

    public double O (int n) { return n*Math.log(n); }

    private E[] array;

    private void swap (int i, int j) {
	E data = array[i];
	array[i] = array[j];
	array[j] = data;
    }

    public void sort (E[] array) {
	this.array = array;
	sort(0, array.length-1);
    }

    private void sort (int first, int last) {
	if (first >= last)
	    return;

	E pivot = array[(first + last) / 2];

	int lo = first;
	int hi = last;
	while (lo <= hi) {
	    // EXERCISE
	    // Move lo forward if array[lo] < pivot


	    // Otherwise move hi backward if array[hi] >= pivot


	    // Otherwise swap array[lo] and array[hi] and move both lo and hi.

		if (array[lo].compareTo(pivot) < 0) {
			lo++;
		}
		else if (array[hi].compareTo(pivot) >= 0) {
			hi--;
		}
		else {
			swap(lo, hi);
			lo++;
			hi--;
		}



	}

	for (int i = lo; i <= last; i++)
	    // EXERCISE
	    // If array[i] equals the pivot

		// Swap array[lo] and array[i] and increment first.

		if (array[i].compareTo(pivot) == 0) {
			swap(lo, i);
			lo++;
		}

	sort(first, hi);
	sort(lo, last);
    }
}

class MergeSort<E extends Comparable<E>> implements Sorter<E> {

    public double O (int n) { return n*Math.log(n); }

    private E[] array, extra;

    public void sort (E[] array) {
	this.array = array;
	extra = array.clone();
	sort(0, array.length-1);
    }

    private void sort(int first, int last) {
	if (first >= last)
	    return;

	int middle = (first + last) / 2;
	sort(first, middle);
	sort(middle+1, last);

	int in1 = first; // goes from first to middle in array
	int in2 = middle+1; // goes from middle+1 to last in array
	int out = first; // goes from first to last in extra (array)
	while (in1 <= middle && in2 <= last) {
	    // EXERCISE
	    // Copy the smaller of array[in1] or array[in2] to extra[out]
	    // (in the case of a tie, copy array[in1] to keep it stable)
	    // and increment out and in1 or in2 (the one you copied).

		if (array[in1].compareTo(array[in2]) <= 0) {
			extra[out] = array[in1];
			in1++;
			out++;
		}
		else {
			extra[out] = array[in2];
			in2++;
			out++;
		}



	}

	// EXERCISE
	// Copy the rest of in1 or in2, whichever is not at the end.

	if (in1 <= middle) {
		System.arraycopy(array, in1, extra, out, middle - in1 + 1);
	}
	else if (in2 <= last){
		System.arraycopy(array, in2, extra, out, last - in2 + 1);
	}





	// Move result from extra array back to original array.
	System.arraycopy(extra, first, array, first, last - first + 1);
    }
}
