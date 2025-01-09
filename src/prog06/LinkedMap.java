package prog06;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractSet;
import java.util.Set;
import java.util.Iterator;

public class LinkedMap <K extends Comparable<K>, V>
    extends AbstractMap<K, V> {

    protected class Entry implements Map.Entry<K, V> {
	K key;
	V value;
	Entry previous, next;
    
	Entry (K key, V value) {
	    this.key = key;
	    this.value = value;
	}
    
	public K getKey () { return key; }
	public V getValue () { return value; }
	public V setValue (V newValue) {
	    V oldValue = value;
	    value = newValue;
	    return oldValue;
	}

	public String toString () {
	    return "{" + key + "=" + value + "}";
	}
    }
  
    protected Entry first, last;
  
    /**
     * Find the earliest Entry e with e.key ">=" key.
     * @param key The Key to be found.
     * @return The Entry e with e.key ">=" key
     * or null if there isn't one.
     */
    protected Entry find (K key) {
	// EXERCISE
	// Look at size() method.
	///
		int count = 0;
	for (Entry entry = first; entry != null; entry = entry.next)
	{
		if (entry.key.compareTo(key) >= 0) {return entry;}
		count++;
	}
	if (count > 30) {System.out.println("count = " + count);}
	///
	return null; // Did not find the entry.
    }    
  
    /**
     * Determine if the Entry returned by find is the one we are looking
     * for.
     * @param entry The Entry returned by find.
     * @param key The Key to be found.
     * @return true if find found the entry with that key
     * or false otherwise
     */
    protected boolean found (Entry entry, K key) {
	// EXERCISE
	// Fix this.
	///
	return entry != null && entry.key.equals(key);
	///
    }

    public boolean containsKey (Object keyAsObject) {
	K key = (K) keyAsObject;
	Entry entry = find(key);
	return found(entry, key);
    }
  
    public V get (Object keyAsObject) {
	// EXERCISE
	// Look at containsKey.
	// If Entry with key was found, return its value.
	///
	K key = (K) keyAsObject;
	Entry entry = find(key);
	if (found(entry, key)) {return entry.value;}
	///
	return null;
    }
  
    /**
     * Add newEntry just before nextEntry or as last Entry if
     * nextEntry is null.
     * @param nextEntry Entry after newEntry or null if there isn't one.
     * @param newEntry The new Entry to be inserted previous to nextEntry.
     */
    protected void add (Entry nextEntry, Entry newEntry) {
		// EXERCISE
		Entry previousEntry = null;
		///
		// Set previousEntry.  Two cases.

		if (nextEntry != null) {
			previousEntry = nextEntry.previous;
		} else {
			previousEntry = last;
		}
		// Set previousEntry.next or first to newEntry.

		if (previousEntry != null) {
			previousEntry.next = newEntry;
		} else {
			first = newEntry;
		}


		// Set nextEntry.previous or last to newEntry.

		if (nextEntry != null) {
			nextEntry.previous = newEntry;
		} else {
			last = newEntry;
		}


		// Set newEntry.previous and newEntry.next.

		newEntry.previous = previousEntry;
		newEntry.next = nextEntry;


	}



    public V put (K key, V value) {
	Entry entry = find(key);
	// EXERCISE
	///
	// Handle the case that the key is already there.
	// Save yourself typing:  setValue returns the old value!
	if (found(entry, key)) {
	    return entry.setValue(value);
	}
	///
	// key was not found:
	else{
		add(entry, new Entry(key, value));
	}

	return null;
    }      

    protected void remove (Entry entry) {
	// EXERCISE
	///
		if (entry.previous != null) {
			entry.previous.next = entry.next;
		} else {
			first = entry.next;
		}

		if (entry.next != null) {
			entry.next.previous = entry.previous;
		} else {
			last = entry.previous;
		}
	///
    }

    public V remove (Object keyAsObject) {
	// EXERCISE
	// Use find, but make sure you got the right Entry!
	// If you do, then remove it and return its value.
	///
	K key = (K) keyAsObject;
	Entry entry = find(key);
	if (found(entry, key)) {
	    remove(entry);
	    return entry.value;
	}
	///
	return null;
    }
    protected class Iter implements Iterator<Map.Entry<K, V>> {
	// EXERCISE
	///
	Entry entry = first;
	///

    
	public boolean hasNext () { 
	    // EXERCISE
	    ///
		if (entry != null) {return true;}
	    return false; // wrong
	    ///
	}
    
	public Map.Entry<K, V> next () {
	    // EXERCISE
	    // Entry implements Map.Entry<K, V> so you return an
	    // Entry, not its value.
	    Entry ret = null;
	    ///
	    // Set ret to the return value.
		ret = entry;
		entry = entry.next;
	    // Move forward.

	    ///
	    return ret;
	}
    
	public void remove () {
	    throw new UnsupportedOperationException();
	}
    }
  
    public int size () {
	int count = 0;
	for (Entry entry = first; entry != null; entry = entry.next)
	    count++;
	return count;
    }

    protected class Setter extends AbstractSet<Map.Entry<K, V>> {
	public Iterator<Map.Entry<K, V>> iterator () {
	    return new Iter();
	}
    
	public int size () { return LinkedMap.this.size(); }
    }
  
    public Set<Map.Entry<K, V>> entrySet () { return new Setter(); }
  
    static void test (Map<String, Integer> map) {
	if (false) {
	    map.put("Victor", 50);
	    map.put("Irina", 45);
	    map.put("Lisa", 47);
    
	    for (Map.Entry<String, Integer> pair : map.entrySet())
		System.out.println(pair.getKey() + " " + pair.getValue());
    
	    System.out.println(map.put("Irina", 55));

	    for (Map.Entry<String, Integer> pair : map.entrySet())
		System.out.println(pair.getKey() + " " + pair.getValue());

	    System.out.println(map.remove("Irina"));
	    System.out.println(map.remove("Irina"));
	    System.out.println(map.get("Irina"));
    
	    for (Map.Entry<String, Integer> pair : map.entrySet())
		System.out.println(pair.getKey() + " " + pair.getValue());
	}
	else {
	    String[] keys = { "Vic", "Ira", "Sue", "Zoe", "Bob", "Ann", "Moe" };
	    for (int i = 0; i < keys.length; i++) {
		System.out.print("put(" + keys[i] + ", " + i + ") = ");
		System.out.println(map.put(keys[i], i));
		System.out.println(map);
		System.out.print("put(" + keys[i] + ", " + -i + ") = ");
		System.out.println(map.put(keys[i], -i));
		System.out.println(map);
		System.out.print("get(" + keys[i] + ") = ");
		System.out.println(map.get(keys[i]));
		System.out.print("remove(" + keys[i] + ") = ");
		System.out.println(map.remove(keys[i]));
		System.out.println(map);
		System.out.print("get(" + keys[i] + ") = ");
		System.out.println(map.get(keys[i]));
		System.out.print("remove(" + keys[i] + ") = ");
		System.out.println(map.remove(keys[i]));
		System.out.println(map);
		System.out.print("put(" + keys[i] + ", " + i + ") = ");
		System.out.println(map.put(keys[i], i));
		System.out.println(map);
	    }
	    for (int i = keys.length; --i >= 0;) {
		System.out.print("remove(" + keys[i] + ") = ");
		System.out.println(map.remove(keys[i]));
		System.out.println(map);
		System.out.print("put(" + keys[i] + ", " + i + ") = ");
		System.out.println(map.put(keys[i], i));
		System.out.println(map);
	    }
	}
    }

    public static void main (String[] args) {
	Map<String, Integer> map = new LinkedMap<String, Integer>();
	test(map);
    }
}
