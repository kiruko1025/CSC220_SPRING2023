package prog04;

import java.util.EmptyStackException;
import java.util.List;
import java.util.ArrayList;

/** Implementation of the interface StackInterface<E> using a List.
*   @author vjm
*/

public class ListStack<E> implements StackInterface<E> {
  // Data Fields
  /** Storage for stack. */
  List<E> list;

  /** Initialize list to an empty List. */
  public ListStack() {
    list = new ArrayList<E>();
  }

  /** Pushes an item onto the top of the stack and returns the item
      pushed.
      @param obj The object to be inserted.
      @return The object inserted.
   */
  public E push (E obj) {
    list.add(obj);
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
    return list.get(list.size()-1);
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
    return list.remove(list.size()-1);
  }

  /**
   * Returns true if the stack is empty; otherwise, returns false.
   *
   * @return true if the stack is empty.
   */
  public boolean empty() {
    return list.isEmpty();
  }

  /**** EXERCISE ****/
}
