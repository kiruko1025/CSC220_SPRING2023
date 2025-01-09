package prog08;

import java.io.*;
import java.util.*;

public class ExternalSort<E extends Comparable<E>> {
    public interface EScanner<E> {
	Iterator<E> iterator (String fileName);
    }

    EScanner eScanner;

    public ExternalSort (EScanner<E> eScanner) {
	this.eScanner = eScanner;
    }

    public Iterator<E> openIn (String fileName) {
	try {
	    return eScanner.iterator(fileName);
	} catch (Exception e) {
	    System.out.println(e);
	}
	return null;
    }
  
    PrintWriter openOut (String fileName) {
	try {
	    return new PrintWriter(new File(fileName));
	} catch (Exception e) {
	    System.out.println(e);
	}
	return null;
    }

    // Read this to make sure you understand file I/O.
    void copy (String fromFile, String toFile) {
	Iterator<E> in = openIn(fromFile);
	PrintWriter out = openOut(toFile);

	while (in.hasNext()) {
	    out.println(in.next());
	}
	out.close();
    }

    E next (Iterator<E> in, int nMax, int n) {
	if (n < nMax && in.hasNext())
	    return in.next();
	return null;
    }

    // Merge the first n elements of in0 with the first n elements of
    // in1 and write the output to out.
    // in0 or in1 may have less than n records.
    // DO NOT allocate an array!
    // Use nextLine as shown.
    void merge (int n, Iterator<E> in0, Iterator<E> in1, PrintWriter out) {
	int n0 = 0, n1 = 0;
	E e0 = next(in0, n, n0++);
	E e1 = next(in1, n, n1++);
	while (e0 != null && e1 != null) {
	    // EXERCISE:

	    // Write out e0 or e1 and get the next element from that Iterator.
	    // Use next as shown above.

			    if (e0.compareTo(e1) <= 0) {
				out.println(e0);
				e0 = next(in0, n, n0++);
				}
				else {
				out.println(e1);
				e1 = next(in1, n, n1++);
				}

	}

	// EXERCISE
	// Write out the rest of the remaining amount of the group of n.

		while (e0 != null) {
			out.println(e0);
			e0 = next(in0, n, n0++);
		}
		while (e1 != null) {
			out.println(e1);
			e1 = next(in1, n, n1++);
		}
    }

    public void sort (String inFile, String outFile) {
	int size = 0;

	int i = 0;
	int j = 0;
    
	// Split the input into two temporary file.
	{
	    Iterator<E> in = openIn(inFile);
	    PrintWriter[] outs = { openOut("tempa" + i), openOut("tempb" + i) };
	    while (in.hasNext()) {
		E e = in.next();
		outs[j].println(e);
		j = 1 - j;
		size++;
	    }
	    outs[0].close();
	    outs[1].close();
	}

	// Merge groups of 1, 2, 4, 8, ...
	int n = 1;
	while (2 * n < size) {
	    // int ii = 1-i;
	    int ii = i+1;
	    Iterator[] ins = { openIn("tempa" + i), openIn("tempb" + i) };
	    PrintWriter[] outs = { openOut("tempa" + ii), openOut("tempb" + ii) };
	    while (ins[0].hasNext() || ins[1].hasNext()) {
		merge(n, ins[0], ins[1], outs[j]);
		j = 1 - j;
	    }
	    outs[0].close();
	    outs[1].close();
      
	    i = ii;
	    n *= 2;
	}

	// Merge the two (sorted) temporary files.
	{
	    Iterator[] ins = { openIn("tempa" + i), openIn("tempb" + i) };
	    PrintWriter out = openOut(outFile);
	    merge(n, ins[0], ins[1], out);
	    out.close();
	}
    }
}
