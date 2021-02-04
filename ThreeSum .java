import java.util.Random;

 class ThreeSum {
    
    /*
     * Enumerate the triples that sum to zero
     */ 
    public static int count(int[] a) { 
        int n = a.length;
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                for (int k = j+1; k < n; k++) {
                    if (a[i] + a[j] + a[k] == 0) {
                        cnt++;
                    }
                }
            }
        }
        return cnt;
   }

    public static void main(String[] args) {

       // populate an array with n random integers
      int n = Integer.parseInt(args[0]);
      int[] a = new int[n];
      Random r = new Random();
      for (int i=0; i< n; i++) {
          a[i] = r.nextInt();
      }
       // Find count
      StdOut.println(count(a));
   }
}