package prog05;
import prog02.GUI;
import prog02.UserInterface;
import prog04.ArrayStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class NotWordle { // NotWordle class
    UserInterface ui; // class variable
    String start;

    List<Node> wordEntries = new ArrayList<Node>();
    NotWordle(UserInterface ui) { // constructor that takes a UserInterface
        this.ui = ui; // and stores it in a class variable
    }

    protected class Node{
        String word;
        Node next;
        public Node(String word){
            this.word = word;
        }
    }

    protected class NodeComparator implements Comparator<Node> {

        String target;
        public NodeComparator(String word) {this.target = word;}
        public int priority (Node node) {
            return startDistance(node) + lettersDifferent(node.word, target);
        }
        public int compare (Node node1, Node node2) {
            return priority(node1) - priority(node2);
        }

    }
    void loadWords(String dictionary){
        try {
            java.util.Scanner scanner = new java.util.Scanner(new java.io.File(dictionary));
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine();
                wordEntries.add(new Node(word));
            }
        } catch (java.io.FileNotFoundException e) {
            ui.sendMessage("File not found: " + dictionary);
        }
    }
    protected static boolean differByOne(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return false;
        }
        int count = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                count++;
            }
        }
        return count == 1;
    }

    protected Node find(String word) {
        for (Node node : wordEntries) {
            if (node.word.equals(word)) {
                return node;
            }
        }
        return null;
    }

    void play(String start, String target) {
        this.start = start;
        while (true) {
            if(this.start.equals(target)){
                ui.sendMessage("You win!");
                return;
            }
            ui.sendMessage("Current word: " + this.start + "\nTarget word: " + target);
            String guess = ui.getInfo("Enter a guess: ");
            if (guess == null) {
                return;
            }
            while (find(guess) == null) {
                ui.sendMessage("That is not a word.");
                guess = ui.getInfo("Re-enter a guess: ");
                if (guess == null) {
                    return;
                }
            }
            while (!differByOne(this.start, guess)) {
                ui.sendMessage("That word differs by more than one letter.");
                guess = ui.getInfo("Re-enter a guess: ");
                if (guess == null) {
                    return;
                }
            }
            this.start = guess;
        }

    }

    void solve(String start, String target){
        this.start = start;
        LinkedQueue<Node> queue = new LinkedQueue<Node>();
        Stack<Node> solution = new Stack<Node>();
        String reversedSolution = "";
        Node startword = find(start);
        queue.offer(startword);
        while(!queue.isEmpty()){
            Node theNode = queue.poll();
            for(Node nextNode : wordEntries){
                if(differByOne(theNode.word, nextNode.word) && nextNode.next == null && !nextNode.word.equals(start)){
                    nextNode.next = theNode;
                    queue.offer(nextNode);
                    if(nextNode.word.equals(target)){
                        while(nextNode != null){
                            solution.push(nextNode);
                            nextNode = nextNode.next;
                        }
                        while(!solution.isEmpty()){
                            reversedSolution += solution.pop().word + "\n";

                        }
                        ui.sendMessage(reversedSolution);

                        return;
                    }
                }
            }
        }
        queue.offer(startword);
    }
    static int lettersDifferent (String word, String target) {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != target.charAt(i)) {
                count++;
            }
        }
        return count;
    }
    int startDistance (Node node) {
        int steps = 0;
        while (node.next != null) {
            steps++;
            node = node.next;
        }
        return steps;
    }


    public static void main(String[] args) {
        UserInterface ui = new GUI("Not Wordle");
        NotWordle game = new NotWordle(ui);
        String dictionary = ui.getInfo("Enter the name of the dictionary file: ");
        if (dictionary == null) {
            return;
        }
        game.loadWords(dictionary);
        String[] commands = {"Human plays.", "Computer plays."};
        int command = 0;
        while (true) {
            command = ui.getCommand(commands);
            if (command == -1) {
                break;
            }
            while(true){
                if(command == 0){
                    String start = ui.getInfo("Enter a starting word: ");
                    if (start == null) {
                        break;
                    }
                    while(game.find(start) == null) {
                        ui.sendMessage("That is not a word.");
                        start = ui.getInfo("Re-enter a starting word: ");
                        if (start == null) {
                            break;
                        }
                    }
                    String target = ui.getInfo("Enter a target word: ");
                    if (target == null) {
                        break;
                    }
                    while(game.find(target) == null) {
                        ui.sendMessage("That is not a word.");
                        target = ui.getInfo("Re-enter a target word: ");
                        if (target == null) {
                            break;
                        }
                    }
                    game.play(start, target);
                } else {
                    String start = ui.getInfo("Enter a starting word: ");
                    if (start == null) {
                        break;
                    }
                    while(game.find(start) == null) {
                        ui.sendMessage("That is not a word.");
                        start = ui.getInfo("Re-enter a starting word: ");
                        if (start == null) {
                            break;
                        }
                    }
                    String target = ui.getInfo("Enter a target word: ");
                    if (target == null) {
                        break;
                    }
                    while(game.find(target) == null) {
                        ui.sendMessage("That is not a word.");
                        target = ui.getInfo("Re-enter a target word: ");
                        if (target == null) {
                            break;
                        }
                    }
                    game.solve(start, target);
                }
            }
        }
    }
}