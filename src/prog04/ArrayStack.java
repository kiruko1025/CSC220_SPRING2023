package prog04;

import java.util.EmptyStackException;

/** Implementation of the interface StackInterface<E> using an array.
 *   @author vjm
 */

public class ArrayStack<E> implements StackInterface<E> {
    // Data Fields
    /** Storage for stack. */
    E[] theData;

    /** Number of elements in stack. */
    int size = 0;

    private static final int INITIAL_CAPACITY = 4;

    /** Construct an empty stack with the default initial capacity. */
    public ArrayStack () {
        theData = (E[])new Object[INITIAL_CAPACITY];
    }

    /** Pushes an item onto the top of the stack and returns the item
	pushed.
	@param obj The object to be inserted.
	@return The object inserted.
    */
    public E push (E obj) {
        // EXERCISE:  Check if array is full and do something about it.
        // Look at ArrayBasedPD.add
        if(size == theData.length){
            E[] newData = (E[])new Object[theData.length*2];
            System.arraycopy(theData, 0, newData, 0, theData.length);
            theData = newData;
        }


        // Putting the ++ after size means use its current value and then
        // increment it afterwards.
        theData[size++] = obj;
        // Same as:
        // theData[size] = obj;
        // size++;
        return obj;
    }

    /**
     * Returns the object at the top of the stack without removing it.
     * post: The stack remains unchanged.
     *
     * @return The object at the top of the stack.
     * @throws EmptyStackException if stack is empty.
     */
    public E peek() {
        if (empty())
            throw new EmptyStackException();
        return theData[size-1];
    }

    /** Returns the object at the top of the stack and removes it.
	post: The stack is one item smaller.
	@return The object at the top of the stack.
	@throws EmptyStackException if stack is empty.
    */
    public E pop () {
        if (empty())
            throw new EmptyStackException();
        size--;
        return theData[size];

        // EXERCISE

    }

    /**
     * Returns true if the stack is empty; otherwise, returns false.
     *
     * @return true if the stack is empty.
     */
    public boolean empty() {
        return size == 0;
    }

    // EXERCISE
}
