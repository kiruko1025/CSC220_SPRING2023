package prog09;
import java.util.*;
import java.io.*;

public class BTree extends AbstractMap<String, String> {
    final int CAPACITY;
    ArrayMap file;
    int level = 0;

    public BTree(int CAPACITY) {
	this.CAPACITY = CAPACITY;
	file = new ArrayMap(CAPACITY, FileMap.newFileName());
    }

    public int size () { return file.size(); }

    public boolean containsKey (Object keyAsObject) {
	return file.containsKey(keyAsObject);
    }
  
    public String get (Object keyAsObject) {
	return file.get(keyAsObject);
    }

    public String put (String key, String value) {
	String oldValue = null;
	// EXERCISE
	///
	// Look at containsKey and get.
	// What should oldValue be set to?
		oldValue = file.put(key, value);
	// Write out the file.
		file.write();
	///
	if (file.size >= file.entries.length) {
	    // EXERCISE
	    ///
	    // increment level
		level++;
	    // Create a new FileMap at the new level.
	    // Use FileMap.newFileName() as its name.
	    // Use CAPACITY and level.
	    FileMap newFile = new FileMap(CAPACITY, FileMap.newFileName(), level);

	    // Add a new Entry (new ArrayMap.Entry) to newFile whose
	    // key is the minimum key in file and whose value is
	    // file's file name.  (Hint: file "knows" its file name.)
		ArrayMap.Entry entry = new ArrayMap.Entry(file.entries[0].key, file.fileName);
		newFile.add(0, entry);

	    // Now newFile is an FileMap with a single file that has 4
	    // entries (file.size==4).  How should we fix this?  Look
	    // at FileMap.put!

		newFile.split(0);
	    // Write out the new file.
		newFile.write();
	    ///
		file = newFile;
	}
	return oldValue;
    }

    public String remove (Object keyAsObject) {
	String value = null;
	//EXERCISE
	///
	// Remove the key from file and save the value.
		value = file.remove(keyAsObject);
	// Write out the file.
		file.write();
	///
	// 

	    // fileMap is the same thing as file, but we know it is
	    // type FileMap because level is positive.
	    if (level > 0) {
		FileMap fileMap = (FileMap) file;
			if (fileMap.size == 1){
				file = fileMap.getFile(0);
				level--;
			}
		}

	    // EXERCISE
	    ///
	    // fileMap has only one file.
	    // Set file to that file.






		// Decrement level.

	    ///
	return value;
    }

    void addEntries(List<Map.Entry<String, String>> list) {
	if (file instanceof FileMap) {
	    FileMap map2 = (FileMap) file;
	    map2.addEntries(list);
	}
	else {
	    for (int i = 0; i < file.size; i++)
		list.add((Map.Entry<String, String>) file.entries[i]);
	}
    }

    Iterator<Map.Entry<String, String>> myIterator () {
	List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>();
	addEntries(list);
	return list.iterator();
    }

    int mySize () {
	List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>();
	addEntries(list);
	return list.size();
    }	

    protected class Setter extends AbstractSet<Map.Entry<String, String>> {
	public Iterator<Map.Entry<String, String>> iterator () {
	    return myIterator();
	}
    
	public int size () {
	    return mySize(); 
	}
    }

    public Set<Map.Entry<String, String>> entrySet () { return new Setter(); }

    void print () {
	if (level == 0) {
	    for (int i = 0; i < file.size; i++) {
		System.out.println(file.entries[i].key);
		System.out.println(file.entries[i].value);
	    }
	}
	else
	    ((FileMap) file).print();
    }

    void putTest (String key, String value) {
	System.out.println("put(" + key + ", " + value + ") = " + put(key, value));
	if (!get(key).equals(value))
	    System.out.println("ERROR: get(" + key + ") = " + get(key));
	print();
    }

    void removeTest (String key) {
	String v = get(key);
	String value = remove(key);
	if (!v.equals(value))
	    System.out.print("ERROR: ");
	System.out.println("remove(" + key + ") = " + value);
	value = remove(key);
	if (value != null)
	    System.out.println("ERROR: remove(" + key + ") = " + value);
	print();
    }

    public static void main (String[] args) {
	BTree tree = new BTree(4);

	tree.putTest("a", 0 + "");
	tree.putTest("b", 1 + "");
	tree.putTest("c", 2 + "");
	tree.putTest("d", 3 + "");
	tree.putTest("e", 4 + "");
	tree.putTest("f", 5 + "");
	tree.putTest("g", 6 + "");
	tree.putTest("h", 7 + "");
	tree.putTest("i", 8 + "");
	tree.removeTest("a");
    }
}

