package prog08;

import java.io.*;
import java.util.*;

public class TestExternalSort {
    static class StringScanner implements ExternalSort.EScanner<String> {
	class Iter implements Iterator<String> {
	    Scanner in;

	    Iter (String fileName) {
		try {
		    in = new Scanner(new File(fileName));
		} catch (Exception e) {
		    System.out.println(e);
		}
	    }

	    public boolean hasNext () {
		return in.hasNextLine();
	    }

	    public String next () {
		return in.nextLine();
	    }
	}

	public Iterator<String> iterator (String fileName) {
	    return new Iter(fileName);
	}
    }

    public static void main (String[] args) {
	ExternalSort sorter = new ExternalSort<String>(new StringScanner());
	sorter.sort("numbers.txt", "sorted.txt");
    }
}


	  
