package prog06;
import java.util.Map;
import java.util.Random;

public class SkipMap<K extends Comparable<K>, V> extends LinkedMap<K, V> {
    boolean debug = false;
    // SkipMap containing half the elements chosen at random.
    SkipMap<K, Entry> skip;
    // Coin flipping code.
    Random random = new Random(1);
    /** Flip a coin.
     * @return true if you flip heads.
     */
    boolean heads () {
	return random.nextInt() % 2 == 0;
    }

    protected void add (Entry nextEntry, Entry newEntry) {
	super.add(nextEntry, newEntry);

	// EXERCISE
	// Flip a coin.  If you flip heads, put newEntry into skip, using
	// its own key as key.  Don't forget to allocate skip if it hasn't
	// been allocated yet.
	///
	if (heads()) {
	    if (skip == null) {
		skip = new SkipMap<K, Entry>();
	    }
	    skip.put(newEntry.key, newEntry);
	}
	///
    }

    protected Entry find (K key) {
	// If there is no skip list, use LinkedMap find.
	if (skip == null) {return super.find(key);}
	// Find the first Entry in skip that is >= key.
	SkipMap<K, Entry>.Entry skipEntry = skip.find(key);
	Entry entry = null;

	if (skipEntry != null) {
	    entry = skipEntry.getValue();
	    // EXERCISE
	    // entry.key ">=" key but
	    // it might not be the earliest Entry >= key.
	    // Because that one was skipped (got tails).
	    // "Move entry back" until it is the first Entry >= key.
	    ///
				int count = 0;
			    while (entry.previous != null && entry.previous.key.compareTo(key) >= 0) {
					count++;
				entry = entry.previous;
			    }
		if (count > 30) {System.out.println("count = " + count);}
	}

	else {
	    // EXERCISE
	    // Starting from last and working backwards.
	    // Set entry to the earliest Entry in the list >= key
	    // or null if there is no entry >= key.
	    // WARNING:  last.key may be < key or >= key.
	    // What should you set entry to if last.key "<" key?
	    ///
	    entry = last;

		if (entry.key.compareTo(key) < 0) {
			entry = null;
		}
		else {
			int count = 0;
			while (entry.previous != null && entry.previous.key.compareTo(key) >= 0) {
				count++;
				entry = entry.previous;
			}
			if (count > 30) {System.out.println("count = " + count);}

		}
	    ///
		}
	if (debug) {
	    if (entry != super.find(key))
		System.out.println("SkipMap.find is incorrect!");
	}
	return entry;
	
    }

    protected void remove (Entry entry) {
	super.remove(entry);
	// EXERCISE
	// Remove the key of entry from skip.  (Use public remove.)
	///
		if (skip != null){
			skip.remove(entry.getKey());}
		if (skip != null && skip.first == null && skip.last == null) {
			skip = null;
		}
	}
	///


    public String toString () {
	if (skip == null)
	    return super.toString();
	return skip.toString() + "\n" + super.toString();
    }

    public static void main (String[] args) {
	Map<String, Integer> map = new SkipMap<String, Integer>();
	test(map);
    }
}

