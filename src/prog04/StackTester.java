package prog04;
import prog02.GUI;
import java.util.Stack;

public class StackTester {
    public static void main (String[] args) {
	//Stack<String> stack = new Stack<String>();
	 //StackInterface<String> stack = new ArrayStack<String>();
	 StackInterface<String> stack = new LinkedStack<String>();
	// StackInterface<String> stack = new ListStack<String>();
	GUI ui = new GUI("StackTester " + stack);

	String[] commands = { "quit", "empty", "peek", "pop", "push" };
	String item;

	while (true) {
	    int c = ui.getCommand(commands);
	    switch (c) {
	    case -1:
		break;
	    case 0:
		return;
	    case 1:
		try {
		    ui.sendMessage("empty() returns " + stack.empty());
		} catch (Exception e) {
		    ui.sendMessage("empty() threw exception: " + e);
		}
		break;
	    case 2:
		try {
		    ui.sendMessage("peek() returns " + stack.peek());
		} catch (Exception e) {
		    ui.sendMessage("peek() threw exception: " + e);
		}
		break;
	    case 3:
		try {
		    ui.sendMessage("pop() returns " + stack.pop());
		} catch (Exception e) {
		    ui.sendMessage("pop() threw exception: " + e);
		}
		break;
	    case 4:
		item = ui.getInfo("What do you want to push?");
		if (item == null)
		    break;
		try {
		    ui.sendMessage("push(" + item + ") returns " + stack.push(item));
		} catch (Exception e) {
		    ui.sendMessage("push(" + item + ") threw exception: " + e);
		}
		break;
	    };
	}
    }
}

        
        
