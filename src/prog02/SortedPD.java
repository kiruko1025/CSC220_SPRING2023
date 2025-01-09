package prog02;

import java.io.*;

/**
 * This is an implementation of PhoneDirectory that uses a sorted
 * array to store the entries.
 *
 * @author 
 */
public class SortedPD extends ArrayBasedPD {

    /** Find an entry in the directory.
     @param name The name to be found
     @return The index of the entry with that name or, if it is not
     there, the index where it should be added.
     */
    protected int find (String name) {
        int low = 0;
        int high = size;
        while (low < high){
            int mid = (low + high)/2;
            int compare = name.compareTo(theDirectory[mid].getName());
            if (compare == 0){
                return mid;
            }
            else if (compare < 0){
                high = mid;
            }
            else{
                low = mid + 1;
            }
        }
        return low;
    }

    /** Determine if name is located at index.
     @param index The index to be checked.
     @param name The name that might be located at that index.
     @return true if a DirectoryEntry with that name is located at
     that index.
     */
    protected boolean found (int index, String name) {
        return  index < size && theDirectory[index].getName().equals(name);
    }

    /** Add an entry to the directory.
     @param index The index at which to add the entry to theDirectory.
     @param newEntry The new entry to add.
     @return The DirectoryEntry that was just added.
     */
    protected DirectoryEntry add (int index, DirectoryEntry newEntry) {
        if (size == theDirectory.length)
            reallocate();
        for (int i = size; i > index; i--){
            theDirectory[i] = theDirectory[i-1];
        }
        theDirectory[index] = newEntry;
        size++;
        return newEntry;
    }

    /** Remove an entry from the directory.
     @param index The index in theDirectory of the entry to remove.
     @return The DirectoryEntry that was just removed.
     */
    protected DirectoryEntry remove (int index) {
        DirectoryEntry entry = theDirectory[index];
        while (index < size-1){
            theDirectory[index] = theDirectory[index+1];
            index++;
        }
        size--;
        return entry;
    }


}
