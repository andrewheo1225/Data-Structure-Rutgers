
 class LinkedStackOfStrings {

    private Node first = null;  // reference to the first node of the LL 

    private class Node {     // inner class Node
        private String item; // reference to a String object      
        private Node next;   // reference to a Node object
    }
     
    public boolean isEmpty() {  
        return first == null;  // if first is null, empty stack
    }
    // 4 assignments => O(1)
    public void push(String item) { // inserting a new node (contains item) in the beginning of the LL
        Node oldFirst = first;
        first = new Node();      
        first.item = item;     
        first.next = oldFirst;
    }
    // 2 assignments => O(1)
    public String pop() { // removing the first node of the LL
        String item = first.item; // saving the reference to String item
        first = first.next; // removes the first node of the LL
        return item;
    }

    public static void main (String[] args) {

        // test client
        LinkedStackOfStrings stack = new LinkedStackOfStrings();
        stack.push("I");
        stack.push("have");
        stack.push("a");
        stack.push("dream");
        stack.push("!");

        while (!stack.isEmpty()) {
            String value = stack.pop();
            System.out.println(value);
        }
    }
}
