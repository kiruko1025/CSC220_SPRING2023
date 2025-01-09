package prog09;
import java.util.*;
import java.io.*;

public class ArrayMap extends AbstractMap<String, String> {
    static class Entry
	implements Map.Entry<String, String> {

	String key;
	String value;
    
	Entry (String key, String value) {
	    this.key = key;
	    this.value = value;
	}
    
	public String getKey () { return key; }
	public String getValue () { return value; }
	public String setValue (String newValue) {
	    String oldValue = value;
	    value = newValue;
	    return oldValue;
	}
    }

    Entry[] entries;
    int size = 0;
    String fileName;

    ArrayMap (int CAPACITY, String fileName) {
	this.entries = new Entry[CAPACITY];
	this.fileName = fileName;
    }

    void reallocate () {
	System.err.println("reallocating " + entries.length);
	Entry[] newEntries = new Entry[2 * entries.length];
	System.arraycopy(entries, 0, newEntries, 0, size);
	entries= newEntries;
    }    

    public int size () { return size; }

    /**
     * Find an entry in entries.
     *
     * @param key The key to be found
     * @return The least index an entry with entry.key >= key or size
     * if there is no such entry.
     */
    int find (String key) {
	// EXERCISE
	///
		int low = 0;
		int high = size;
		while (low < high) {
			int mid = (low + high) / 2;
			if (key.compareTo(entries[mid].key) <= 0)
				high = mid;
			else
				low = mid + 1;
		}
	
		return low; // fix this
	///
    }

    /**
     * Determine if key is found.
     *
     * @param index Possible index for the key.
     * @param key The key to be found.
     * @return true if index is correct for key; false, otherwise.
     */
    boolean found (int index, String key) {
	// EXERCISE
	///
		return index < size && entries[index].key.equals(key);

	/// 
    }

    public boolean containsKey (Object keyAsObject) {
	String key = (String) keyAsObject;
	int index = find(key);
	return found(index, key);
    }
  
    public String get (Object keyAsObject) {
	String key = (String) keyAsObject;
	int index = find(key);
	if (found(index, key))
	    return entries[index].value;
	return null;
    }

    /**
     * Add an entry to entries.
     *
     * @param index    The index at which to add the entry to entries.
     * @param newEntry The new entry to add.
     */
    void add (int index, Entry newEntry) {
	if (size == entries.length){
	    reallocate();}
	// EXERCISE
	///
		for (int i = size; i > index; i--)
			entries[i] = entries[i - 1];
		entries[index] = newEntry;
		size++;


	///
    }

    public String put (String key, String value) {
	int index = find(key);
	if (found(index, key))
	    return entries[index].setValue(value);
	add(index, new Entry(key, value));
	return null;
    }

    /**
     * Remove an entry from entries.
     *
     * @param index The index in entries of the entry to remove.
     * @return The Entry that was just removed.
     */
    Entry remove (int index) {
	Entry entry = entries[index];
	// EXERCISE
	///

	for (int i = index; i < size - 1; i++)
			entries[i] = entries[i + 1];
		size--;


	///
	return entry;
    }

    public String remove (Object keyAsObject) {
	String key = (String) keyAsObject;
	int index = find(key);
	if (found(index, key))
	    return remove(index).value;
	return null;
    }

    public boolean read () {
	try {
	    Scanner scanner = new Scanner(new File(fileName));
	    size = 0;
	    while (scanner.hasNext()) {
		String key = scanner.next();
		String value = scanner.next();
		add(size, new Entry(key, value));
	    }
	    return true;
	} catch (Exception e) {
	    System.out.println(e);
	}
	return false;
    }
	    
    public boolean write () {
	try {
	    PrintWriter out = new PrintWriter(new FileWriter(fileName));
	    for (int i = 0; i < size; i++) {
		out.println(entries[i].key);
		out.println(entries[i].value);
	    }
	    out.close();
	    return true;
	} catch (Exception e) {
	    System.out.println(e);
	}
	return false;
    }
	    
    protected class Iter implements Iterator<Map.Entry<String,String>> {
	int index = 0;
    
	public boolean hasNext () { 
	    return index < size;
	}
    
	public Map.Entry next () {
	    if (!hasNext())
		throw new NoSuchElementException();
	    return entries[index++];
	}
    
	public void remove () {
	    throw new UnsupportedOperationException();
	}
    }
  
    protected class Setter extends AbstractSet<Map.Entry<String,String>> {
	public Iterator<Map.Entry<String,String>> iterator () {
	    return new Iter();
	}
    
	public int size () { return ArrayMap.this.size(); }
    }
  
    public Set<Map.Entry<String,String>> entrySet () { return new Setter(); }

    public static void main (String[] args) {
	ArrayMap map = new ArrayMap(100, "arraymap.txt");

	/*
	  System.out.println("map = " + map);
	  map.put("b", 1);
	  System.out.println("map = " + map);
	  map.put("a", 0);
	  System.out.println("map = " + map);
	  map.remove("b");
	  System.out.println("map = " + map);
	  map.put("c", 2);
	  System.out.println("map = " + map);
	  map.put("b", 1);
	  System.out.println("map = " + map);
	  map.remove("b");
	  System.out.println("map = " + map);
	  map.put("b", 1);
	  System.out.println("map = " + map);

	  map.put("d", 3);
	  System.out.println("map = " + map);
	  map.put("i", 8);
	  System.out.println("map = " + map);
	  map.put("h", 7);
	  System.out.println("map = " + map);
	  map.put("f", 5);
	  System.out.println("map = " + map);
	  map.put("e", 4);
	  System.out.println("map = " + map);
	  map.put("g", 6);
	  System.out.println("map = " + map);
	*/

	System.out.println("map = " + map);
	System.out.println("put(\"m\", 7) = " + map.put("m", 7 + ""));
	System.out.println("map = " + map);
	System.out.println("get(\"m\") = " + map.get("m"));
	System.out.println("put(\"m\", 9) = " + map.put("m", 9 + ""));
	System.out.println("map = " + map);
	System.out.println("remove(\"m\") = " + map.remove("m"));
	System.out.println("map = " + map);
	System.out.println("remove(\"m\") = " + map.remove("m"));
	System.out.println("map = " + map);
	System.out.println("put(\"m\", 2) = " + map.put("m", 2 + ""));
	System.out.println("map = " + map);
	System.out.println("put(\"g\", 7) = " + map.put("g", 7 + ""));
	System.out.println("map = " + map);
	System.out.println("get(\"g\") = " + map.get("g"));
	System.out.println("put(\"g\", 9) = " + map.put("g", 9 + ""));
	System.out.println("map = " + map);
	System.out.println("remove(\"g\") = " + map.remove("g"));
	System.out.println("map = " + map);
	System.out.println("put(\"s\", 8) = " + map.put("s", 8 + ""));
	System.out.println("map = " + map);
	System.out.println("get(\"s\") = " + map.get("s"));
	System.out.println("put(\"s\", 2) = " + map.put("s", 2 + ""));
	System.out.println("map = " + map);
	System.out.println("remove(\"s\") = " + map.remove("s"));
	System.out.println("map = " + map);
	System.out.println("remove(\"s\") = " + map.remove("s"));
	System.out.println("map = " + map);
	System.out.println("put(\"s\", 0) = " + map.put("s", 0 + ""));
	System.out.println("map = " + map);
	System.out.println("put(\"b\", 3) = " + map.put("b", 3 + ""));
	System.out.println("map = " + map);
	System.out.println("get(\"b\") = " + map.get("b"));
	System.out.println("put(\"b\", 9) = " + map.put("b", 9 + ""));
	System.out.println("map = " + map);
	System.out.println("remove(\"b\") = " + map.remove("b"));
	System.out.println("map = " + map);
	System.out.println("put(\"p\", 2) = " + map.put("p", 2 + ""));
	System.out.println("map = " + map);
	System.out.println("get(\"p\") = " + map.get("p"));
	System.out.println("put(\"p\", 5) = " + map.put("p", 5 + ""));
	System.out.println("map = " + map);
	System.out.println("remove(\"p\") = " + map.remove("p"));
	System.out.println("map = " + map);
	System.out.println("put(\"w\", 4) = " + map.put("w", 4 + ""));
	System.out.println("map = " + map);
	System.out.println("get(\"w\") = " + map.get("w"));
	System.out.println("put(\"w\", 7) = " + map.put("w", 7 + ""));
	System.out.println("map = " + map);
	System.out.println("remove(\"w\") = " + map.remove("w"));
	System.out.println("map = " + map);
	System.out.println("remove(\"w\") = " + map.remove("w"));
	System.out.println("map = " + map);

	map.write();
	map.read();
	System.out.println("map = " + map);
    }
}
