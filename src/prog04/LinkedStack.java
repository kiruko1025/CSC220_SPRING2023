package prog04;

import java.util.EmptyStackException;

/** Class to implement interface StackInterface<E> as a linked list.
 *   @author vjm
 * */

public class LinkedStack<E> implements StackInterface<E> {

    /** This Node class is the building block for a singly-linked list.

	We call it a Node instead of an Entry because it has only one
	data field, instead of a name (key) and number (value) field.

	Each Node has a next pointer to the next Node in the list.

	Since we declare it inside LinkedStack, we can just call it
	Node instead of StackNode and we do not need a second file.
    */
    private class Node {
	// Data Fields
	/** The reference to the data. */
	private final E data;

	/** The reference to the next node. */
	private Node next;

	// Constructors
	/** Creates a new node with a null next field.
	    @param data The data stored
	*/
	private Node (E data) {
	    this.data = data;
	    next = null; // Necessary in C++ but not in Java.
	}
    } //end class Node

    // Data Fields
    /** The reference to the top stack node. */
    private Node top = null;

    /** Pushes an item onto the top of the stack and returns the item
	pushed.
	@param obj The object to be inserted.
	@return The object inserted.
    */
    public E push (E obj) {
	Node newNode = new Node(obj);
	newNode.next = top;
	top = newNode;
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
		return top.data;
	}

	/**
	 * Returns the object at the top of the stack and removes it.
	 * post: The stack is one item smaller.
	 *
	 * @return The object at the top of the stack.
	 * @throws EmptyStackException if stack is empty.
	 */
	public E pop() {
		if (empty())
			throw new EmptyStackException();
		Node poped = top;
		top = top.next;
		return poped.data;
	}

	/**
	 * Returns true if the stack is empty; otherwise, returns false.
	 *
	 * @return true if the stack is empty.
	 */
	public boolean empty() {
		return top==null;
	}

	/**** EXERCISE ****/
}
