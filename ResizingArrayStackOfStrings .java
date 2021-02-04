 class ResizingArrayStackOfStrings {
    
    private String[] s;
    private int n = 0;
    
    public ResizingArrayStackOfStrings () {
        s = new String[1];
    }
    public boolean isEmpty() {
        return n == 0;
    }
    
    public void push(String item){
        if (n == s.length) {
            resize(2 * s.length);
        }
        s[n++] = item;
    }
    
    private void resize(int capacity) {
        String[] copy = new String[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = s[i];
        }
        s = copy;
    }

    public String pop() {
        return s[--n];
    }
}
